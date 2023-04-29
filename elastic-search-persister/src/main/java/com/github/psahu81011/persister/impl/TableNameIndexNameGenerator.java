package com.github.psahu81011.persister.impl;

import com.github.psahu81011.persister.IndexNameGenerator;
import com.github.psahu81011.river.bean.MessageValue;
import java.util.Date;

public class TableNameIndexNameGenerator implements IndexNameGenerator {

  @Override
  public String indexName(final MessageValue messageValue, final Date date) {
    return messageValue.getTable().toLowerCase();
  }
}
