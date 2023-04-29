package com.github.psahu81011.river.service.impl;

import com.github.psahu81011.river.bean.MessageValue;
import com.github.psahu81011.river.service.IEnricher;

public class NoopEnricher
    implements IEnricher {

  @Override
  public MessageValue enrich(final MessageValue messageValue) {
    return messageValue;
  }

}
