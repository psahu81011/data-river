package com.github.psahu81011.persister;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.elasticsearch.action.index.IndexRequest;
import org.junit.Test;


public class ESBulkProcessorTest {

  @Test
  public void test() throws Exception {
    ESRiverConfiguration configuration = new ESRiverConfiguration();
    configuration.setHosts(Lists.newArrayList("localhost"));
    configuration.setClusterName("elasticsearch");
    ESBulkProcessor processor = new ESBulkProcessor(configuration);
    processor.start();
    processor.get().add(new IndexRequest("index").source(ImmutableMap.of("key", "value")));
    processor.get().flush();
    processor.stop();
  }

}