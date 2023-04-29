package com.github.psahu81011.river.service.impl;

import com.github.psahu81011.river.bean.MessageValue;
import com.github.psahu81011.river.service.IFilter;

public class PassAllFilter
    implements IFilter {

  @Override
  public boolean shouldProcess(final MessageValue messageValue) {
    return true;
  }
}
