package com.github.psahu81011.persister.impl;

import java.util.Date;

import com.github.psahu81011.river.bean.MessageValue;
import org.junit.Assert;
import org.junit.Test;


public class TableNameIndexNameGeneratorTest {

  @Test
  public void indexName() throws Exception {
    final MessageValue value = new MessageValue();
    value.setDatabase("db");
    value.setTable("Table");
    TableNameIndexNameGenerator indexNameGenerator = new TableNameIndexNameGenerator();
    final String indexName = indexNameGenerator.indexName(value, new Date());
    Assert.assertEquals("table", indexName);
  }

}