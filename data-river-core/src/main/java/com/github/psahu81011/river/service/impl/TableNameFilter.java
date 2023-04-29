package com.github.psahu81011.river.service.impl;

import com.github.psahu81011.river.bean.MessageValue;
import com.github.psahu81011.river.service.IFilter;
import java.util.Objects;

public class TableNameFilter
    implements IFilter {

  private final String tableName;

  public TableNameFilter(final String tableName) {
    this.tableName = Objects.requireNonNull(tableName, "'tableName' cannot be null");
  }

  @Override
  public boolean shouldProcess(final MessageValue messageValue) {
    return tableName.equalsIgnoreCase(messageValue.getTable());
  }
}
