package com.github.psahu81011.river.processor;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.psahu81011.river.bean.MessageKey;
import com.github.psahu81011.river.bean.MessageValue;
import com.github.psahu81011.river.service.IBackFiller;
import com.github.psahu81011.river.service.IEnricher;
import com.github.psahu81011.river.service.IFilter;
import com.github.psahu81011.river.service.IRiverService;
import com.github.psahu81011.river.service.TransformerFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.sql.Timestamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DBStreamProcessorTest {

  @Mock
  private IRiverService riverService;

  private DBStreamProcessor dbStreamProcessor;

  @Test
  public void process() {
    dbStreamProcessor = new DBStreamProcessor(riverService);
    MessageKey key = new MessageKey();
    key.setTable("table");
    key.setDatabase("database");

    MessageValue value = new MessageValue();
    value.setDatabase(key.getDatabase());
    value.setTable(key.getTable());
    value.setCommitId(123L);
    value.setCommitted(true);
    value.setTimestamp(new Timestamp(System.currentTimeMillis()));
    value.setData(Maps.newHashMap());
    value.setOld(Maps.newHashMap());

    TransformerFactory.builder()
            .backFillers(ImmutableMap.of("river1", Mockito.mock(IBackFiller.class)))
            .enrichers(ImmutableMap.of("river1", Mockito.mock(IEnricher.class)))
            .filters(ImmutableMap.of("river1", Mockito.mock(IFilter.class)))
            .build();

    dbStreamProcessor.process(key, value);
    verify(riverService, times(1)).process(value, "river1");

    TransformerFactory.INSTANCE = null;
  }

  @Test
  public void process_error() {
    dbStreamProcessor = new DBStreamProcessor(riverService);
    MessageKey key = new MessageKey();
    key.setTable("table");
    key.setDatabase("database");

    MessageValue value = new MessageValue();
    value.setDatabase(key.getDatabase());
    value.setTable(key.getTable());
    value.setCommitId(123L);
    value.setCommitted(true);
    value.setTimestamp(new Timestamp(System.currentTimeMillis()));
    value.setData(Maps.newHashMap());
    value.setOld(Maps.newHashMap());

    dbStreamProcessor.process(key, value);
    verify(riverService, times(0)).process(value, "river1");
  }

}