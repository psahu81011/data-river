package com.github.psahu81011.river.service.impl;

import com.github.psahu81011.river.bean.MessageValue;
import org.junit.Assert;
import org.junit.Test;

public class PassAllFilterTest {

  @Test
  public void shouldProcess() {
    PassAllFilter filter = new PassAllFilter();
    final boolean shouldProcess = filter.shouldProcess(new MessageValue());
    Assert.assertTrue(shouldProcess);
  }

}