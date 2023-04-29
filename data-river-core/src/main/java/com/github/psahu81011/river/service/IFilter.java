package com.github.psahu81011.river.service;

import com.github.psahu81011.river.bean.MessageValue;

@FunctionalInterface
public interface IFilter {

  /**
   * @param messageValue incoming messageValue
   * @return {@code true} if the messages needs to be processed, {@code false} if this message needs
   * to be filtered.
   */
  boolean shouldProcess(final MessageValue messageValue);

}
