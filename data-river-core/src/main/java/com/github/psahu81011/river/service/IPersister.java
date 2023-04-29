package com.github.psahu81011.river.service;

import com.github.psahu81011.river.bean.MessageValue;

public interface IPersister {

  void persist(final MessageValue messageValue, final String riverType);
}
