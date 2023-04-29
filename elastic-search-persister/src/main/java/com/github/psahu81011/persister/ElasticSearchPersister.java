package com.github.psahu81011.persister;

import java.util.Map;
import java.util.function.Supplier;

import com.github.psahu81011.river.bean.MessageValue;
import com.github.psahu81011.river.service.IEnricher;
import com.github.psahu81011.river.service.IPersister;
import com.github.psahu81011.river.service.TransformerFactory;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;


public class ElasticSearchPersister implements IPersister {

  private final Supplier<BulkProcessor> bulkProcessor;
  private final Map<String, IndexNameGenerator> indexNameGenerators;

  public ElasticSearchPersister(final Supplier<BulkProcessor> bulkProcessor,
                                final Map<String, IndexNameGenerator> indexNameGenerators) {
    this.bulkProcessor = bulkProcessor;
    this.indexNameGenerators = indexNameGenerators;
  }

  @Override
  public void persist(final MessageValue messageValue, final String riverType) {
    final IEnricher enricher = TransformerFactory.INSTANCE.enricher(riverType);
    final String id = enricher.recordId(messageValue);
    final String indexName = indexNameGenerators.get(riverType)
            .indexName(messageValue, enricher.getRecordCreationDate(messageValue));
    MessageValue.Type type = messageValue.getType();
    switch (type) {
      case insert:
      case update:
        bulkProcessor.get().add(new IndexRequest(indexName).id(id).source(messageValue.getData()));
        break;
      case delete:
        bulkProcessor.get().add(new DeleteRequest(indexName, id));
        break;
    }
  }
}
