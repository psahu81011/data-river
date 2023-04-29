package com.github.psahu81011.river.service.impl;

import com.github.psahu81011.river.bean.MessageValue;
import com.github.psahu81011.river.service.IEnricher;
import com.github.psahu81011.river.service.IFilter;
import com.github.psahu81011.river.service.IPersister;
import com.github.psahu81011.river.service.IRiverService;
import com.github.psahu81011.river.service.TransformerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RiverServiceImpl
        implements IRiverService {

  private final IPersister persister;

  public RiverServiceImpl(final IPersister persister) {
    this.persister = persister;
  }

  @Override
  public void process(final MessageValue messageValue, final String riverType) {
    final TransformerFactory transformerFactory = TransformerFactory.INSTANCE;
    final IFilter filter = transformerFactory.filter(riverType);
    if (filter.shouldProcess(messageValue)) {
      log.info("processing message {} for riverType {}", messageValue, riverType);
      final IEnricher enricher = transformerFactory.enricher(riverType);
      final MessageValue enrich = enricher.enrich(messageValue);
      persister.persist(enrich, riverType);
    }
  }
}