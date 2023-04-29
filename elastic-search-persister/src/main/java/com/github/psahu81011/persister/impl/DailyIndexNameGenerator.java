package com.github.psahu81011.persister.impl;

import static com.github.psahu81011.river.service.IEnricher.df;

import com.github.psahu81011.persister.IndexNameGenerator;
import com.github.psahu81011.river.bean.MessageValue;
import java.util.Date;

public class DailyIndexNameGenerator implements IndexNameGenerator {

  @Override
  public String indexName(final MessageValue messageValue, final Date date) {
    return String.join("-", messageValue.getDatabase(), messageValue.getTable(), df.get()
        .format(date))
        .toLowerCase();
  }

}
