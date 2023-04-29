package com.github.psahu81011.river.service.impl;

import com.github.psahu81011.river.bean.MessageValue;
import org.junit.Assert;
import org.junit.Test;

public class TableNameFilterTest {

  @Test(expected = NullPointerException.class)
  public void shouldProcess_error() {
    TableNameFilter filter = new TableNameFilter(null);
  }

  @Test
  public void shouldProcess() {
    TableNameFilter filter = new TableNameFilter("Table");
    MessageValue messageValue = new MessageValue();
    messageValue.setTable("Table");
    Assert.assertTrue(filter.shouldProcess(messageValue));

    messageValue.setTable("InvalidTable");
    Assert.assertFalse(filter.shouldProcess(messageValue));
  }

}