package com.github.psahu81011.river.service.impl;

import com.github.psahu81011.river.bean.MessageValue;
import org.junit.Assert;
import org.junit.Test;

public class NoopEnricherTest {

  @Test
  public void enrich() {
    NoopEnricher enricher = new NoopEnricher();
    MessageValue value = new MessageValue();
    final MessageValue enrich = enricher.enrich(value);
    Assert.assertEquals(value, enrich);
  }

}