package com.github.psahu81011.river.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@Data
public class KafkaConsumerConfig {

    @Pattern(regexp = "latest|earliest", message = "must of values [latest, earliest]")
    private String initialOffset;

    @NotEmpty
    private String group;

    @NotEmpty
    private String topic;

    @NotEmpty
    private String bootstrapServers;

    @Min(1)
    private int threadCount;

    @Min(10)
    private int pollingTimeMs;
}