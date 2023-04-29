package com.olacabs.olamoney.river.task;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

import com.github.psahu81011.river.bean.MessageValue;
import com.github.psahu81011.river.service.IBackFiller;
import com.github.psahu81011.river.service.IRiverService;
import com.github.psahu81011.river.service.TransformerFactory;
import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BackfillTask extends Task {

  private final IRiverService riverService;

  /**
   * Create a new task with the given name.
   *
   * @param riverService riverService
   */
  public BackfillTask(final IRiverService riverService) {
    super("backfill");
    this.riverService = riverService;
  }

  @Override
  public void execute(Map<String, List<String>> parameters, final PrintWriter output) {
    if (!parameters.containsKey("riverType")) {
      output.println("riverType is mandatory");
      output.flush();
      return;
    }
    final String riverType = parameters.get("riverType").get(0);
    final boolean parallel = parameters.containsKey("parallel");

    final LocalDateTime startDate = LocalDateTime.parse(parameters.get("startDate").get(0), ISO_DATE_TIME);
    final LocalDateTime endDate = LocalDateTime.parse(parameters.get("endDate").get(0), ISO_DATE_TIME);

    final IBackFiller backFiller = TransformerFactory.INSTANCE.backFiller(riverType);
    if (backFiller == null) {
      output.println("No backfiller configured for " + riverType);
      output.flush();
      return;
    }
    try {
      final Stream<MessageValue> iterate = backFiller.streamElements(startDate, endDate, parallel);
      final AtomicLong counter = new AtomicLong();
      final long start = System.currentTimeMillis();
      iterate.peek(messageValue -> riverService.process(messageValue, riverType))
              .peek(messageValue -> counter.incrementAndGet())
              .forEachOrdered(messageValue -> {
                if (counter.get() % 100 == 0) {
                  output.println("Processed " + counter.get() + " items for river " + riverType);
                  output.println("Last item processed is: " + messageValue);
                  output.flush();
                }
              });
      output.println("Processed " + counter.get() + " items for river " + riverType);
      output.println(
              "Finished processing " + counter.get() + " records in " + (System.currentTimeMillis()
                      - start) + "ms");
      output.flush();
    } finally {
      backFiller.cleanup();
    }
  }
}