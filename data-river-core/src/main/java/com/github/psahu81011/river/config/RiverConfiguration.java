package com.github.psahu81011.river.config;

import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Slf4j
public abstract class RiverConfiguration {

  @Valid
  @NotNull
  private KafkaConsumerConfig kafkaConsumerConfig;

  @NotEmpty
  private Set<String> dateFields;

}