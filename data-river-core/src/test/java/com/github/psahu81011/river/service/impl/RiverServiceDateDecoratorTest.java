package com.github.psahu81011.river.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.github.psahu81011.river.bean.MessageValue;
import com.github.psahu81011.river.service.IRiverService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RiverServiceDateDecoratorTest {

  @Mock
  private IRiverService delegate;

  private RiverServiceDateDecorator decorator;

  @Test
  public void process_data() {
    decorator = new RiverServiceDateDecorator(delegate, Sets.newHashSet("created_at"));
    MessageValue value = new MessageValue();
    value.setData(new HashMap<>(
            ImmutableMap.of("created_at", "2016-04-01 00:00:00", "updated_at", new Date())));
    decorator.process(value, "river1");

    final Map<String, Object> data = value.getData();
    assertNotNull(data);
    assertSame(data.get("created_at").getClass(), Date.class);
    assertSame(data.get("updated_at").getClass(), Date.class);
  }

  @Test
  public void process_data_invalid() {
    decorator = new RiverServiceDateDecorator(delegate, Sets.newHashSet("created_at"));
    MessageValue value = new MessageValue();
    value.setData(new HashMap<>(
            ImmutableMap.of("created_at", "2016-04-0100:00:00", "updated_at", new Date())));
    decorator.process(value, "river1");

    final Map<String, Object> data = value.getData();
    assertNotNull(data);
    assertSame(data.get("created_at").getClass(), String.class);
    assertSame(data.get("updated_at").getClass(), Date.class);
  }

  @Test
  public void process_data_null() {
    decorator = new RiverServiceDateDecorator(delegate, Sets.newHashSet("created_at"));
    MessageValue value = new MessageValue();
    value.setData(new HashMap<>(ImmutableMap.of("created_at", "null", "updated_at", new Date())));
    decorator.process(value, "river1");

    final Map<String, Object> data = value.getData();
    assertNotNull(data);
    assertNull(data.get("created_at"));
    assertSame(data.get("updated_at").getClass(), Date.class);
  }

  @Test
  public void process_old() {
    decorator = new RiverServiceDateDecorator(delegate,
            Sets.newHashSet("created_at", "updated_at"));
    MessageValue value = new MessageValue();
    value.setData(new HashMap<>(
            ImmutableMap.of("created_at", "2016-04-01 00:00:00", "updated_at", new Date())));
    value.setOld(new HashMap<>(
            ImmutableMap.of("updated_at", "2016-04-01 00:00:00", "created_at", new Date())));
    decorator.process(value, "river1");

    final Map<String, Object> data = value.getOld();
    assertNotNull(data);
    assertSame(data.get("created_at").getClass(), Date.class);
    assertSame(data.get("updated_at")
            .getClass(), Date.class);
  }

}