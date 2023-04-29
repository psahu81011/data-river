package com.github.psahu81011.river.service;

import com.github.psahu81011.river.bean.MessageValue;

@FunctionalInterface
public interface IRiverService {

  void process(final MessageValue messageValue, final String riverType);

}
