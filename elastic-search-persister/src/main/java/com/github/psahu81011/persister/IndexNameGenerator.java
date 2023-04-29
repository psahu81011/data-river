package com.github.psahu81011.persister;

import com.github.psahu81011.river.bean.MessageValue;
import java.util.Date;

public interface IndexNameGenerator {

  String indexName(final MessageValue messageValue, final Date date);

}
