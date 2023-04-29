package com.github.psahu81011.persister;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.psahu81011.persister.health.ESHealthCheck;
import com.github.psahu81011.persister.impl.DailyIndexNameGenerator;
import com.github.psahu81011.river.config.RiverConfiguration;
import com.github.psahu81011.river.processor.DBStreamProcessor;
import com.github.psahu81011.river.processor.KafkaKraftConsumer;
import com.github.psahu81011.river.service.IPersister;
import com.github.psahu81011.river.service.IRiverService;
import com.github.psahu81011.river.service.TransformerFactory;
import com.github.psahu81011.river.service.impl.RiverServiceDateDecorator;
import com.github.psahu81011.river.service.impl.RiverServiceImpl;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.olacabs.olamoney.river.task.BackfillTask;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;
import io.dropwizard.util.Size;
import io.dropwizard.util.SizeUnit;
import io.dropwizard.validation.MaxDuration;
import io.dropwizard.validation.MaxSize;
import io.dropwizard.validation.MinDuration;
import io.dropwizard.validation.MinSize;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.hibernate.validator.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import static com.github.psahu81011.river.service.IEnricher.dtf;

@EqualsAndHashCode(callSuper = true)
@Data
public class ESRiverConfiguration extends RiverConfiguration {

  @NotEmpty
  private String clusterName;

  @Range(min = 0, max = 100000)
  private int bulkActions = 10000;

  @MinSize(value = 1, unit = SizeUnit.MEGABYTES)
  @MaxSize(value = 1, unit = SizeUnit.GIGABYTES)
  private Size bulkSize = Size.megabytes(128);

  @MinDuration(value = 1)
  @MaxDuration(value = 1, unit = TimeUnit.MINUTES)
  private Duration flushInterval = Duration.seconds(30);

  @NotEmpty
  private List<String> hosts;

  public void build(final Environment environment) {
    Preconditions.checkState(TransformerFactory.INSTANCE != null,
            "Transformer factory must be initialized before building this");
    ImmutableMap.Builder<String, IndexNameGenerator> indexNameGenerators = ImmutableMap.builder();
    final DailyIndexNameGenerator value = new DailyIndexNameGenerator();
    TransformerFactory.INSTANCE.getRiverTypes()
            .forEach(key -> indexNameGenerators.put(key, value));
    build(environment, indexNameGenerators.build());
  }

  public void build(final Environment environment,
                    Map<String, IndexNameGenerator> indexNameGenerators) {
    Preconditions.checkState(TransformerFactory.INSTANCE != null,
            "Transformer factory must be initialized before building this");
    for (final String riverType : TransformerFactory.INSTANCE.getRiverTypes()) {
      Preconditions.checkState(indexNameGenerators.containsKey(riverType),
              "No indexNameGenerator for " + riverType);
    }
    final ObjectMapper objectMapper = environment.getObjectMapper();
    objectMapper.setDateFormat(dtf.get());

    final IPersister iPersister = doBuild(environment, indexNameGenerators);
    final IRiverService riverService = new RiverServiceImpl(iPersister);

    final IRiverService riverServiceDecorator = new RiverServiceDateDecorator(riverService,
            getDateFields());

    environment.admin()
            .addTask(new BackfillTask(riverService));

    final DBStreamProcessor streamProcessor = new DBStreamProcessor(riverServiceDecorator);

    ExecutorService executor = Executors.newFixedThreadPool(getKafkaConsumerConfig().getThreadCount());
    for (int i = 0; i < getKafkaConsumerConfig().getThreadCount(); i++) {
      executor.submit(new KafkaKraftConsumer(streamProcessor, objectMapper, getKafkaConsumerConfig()));
    }
  }

  protected IPersister doBuild(final Environment environment,
                               Map<String, IndexNameGenerator> indexNameGenerators) {
    ESBulkProcessor bulkProcessor = new ESBulkProcessor(this);
    environment.lifecycle()
            .manage(bulkProcessor);
    environment.healthChecks()
            .register("es-health", new ESHealthCheck(bulkProcessor));
    return new ElasticSearchPersister(bulkProcessor, indexNameGenerators);
  }
}