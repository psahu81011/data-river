package com.github.psahu81011.river.processor;

import com.github.psahu81011.river.bean.MessageKey;
import com.github.psahu81011.river.bean.MessageValue;
import com.github.psahu81011.river.service.IRiverService;
import com.github.psahu81011.river.service.TransformerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBStreamProcessor {

  private final IRiverService riverService;

  public DBStreamProcessor(final IRiverService riverService) {
    this.riverService = riverService;
  }

  public void process(final MessageKey key, final MessageValue message) {
    try {
      log.debug("message {}", message);
      for (final String riverType : TransformerFactory.INSTANCE.getRiverTypes()) {
        riverService.process(message, riverType);
      }
    } catch (Exception e) {
      log.error("Error in message {}", message, e);
    }
  }
}