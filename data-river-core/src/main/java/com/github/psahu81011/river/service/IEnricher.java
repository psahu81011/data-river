package com.github.psahu81011.river.service;

import com.github.psahu81011.river.bean.MessageValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.SneakyThrows;

public interface IEnricher {

  ThreadLocal<DateFormat> dtf = ThreadLocal
      .withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
  ThreadLocal<DateFormat> df = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

  /**
   * Enriches the incoming {@link MessageValue} and returns the enriched {@link MessageValue}.
   *
   * @param messageValue incoming messageValue
   * @return
   */
  MessageValue enrich(final MessageValue messageValue);

  /**
   * Returns the {@link Date} on which the record was first created.
   *
   * @param messageValue incoming messageValue
   * @return by default returns the "created_at" field in the incoming message.
   */
  @SneakyThrows
  default Date getRecordCreationDate(final MessageValue messageValue) {
    return (Date) messageValue.getData()
        .get("created_at");
  }

  /**
   * Returns the {@code id} using which is the primary/unique key of the record.
   *
   * @param messageValue incoming messageValue
   * @return by default returns the "id" field in the incoming message.
   */
  default <T> T recordId(final MessageValue messageValue) {
    return (T) String.valueOf(messageValue.getData()
        .get("id"));
  }

}
