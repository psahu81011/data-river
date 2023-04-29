package com.github.psahu81011.river.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.psahu81011.river.bean.MessageKey;
import com.github.psahu81011.river.bean.MessageValue;
import com.github.psahu81011.river.config.KafkaConsumerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class KafkaKraftConsumer implements Runnable {

    private KafkaConsumer<String, String> consumer;
    private final DBStreamProcessor dbStreamProcessor;
    private final ObjectMapper objectMapper;
    private boolean running = true;
    private final KafkaConsumerConfig kafkaConsumerConfig;

    public KafkaKraftConsumer(DBStreamProcessor dbStreamProcessor, ObjectMapper objectMapper, KafkaConsumerConfig kafkaConsumerConfig) {
        this.kafkaConsumerConfig = kafkaConsumerConfig;
        this.dbStreamProcessor = dbStreamProcessor;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run() {
        log.info("Starting consumer");
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerConfig.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfig.getGroup());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfig.getInitialOffset());

        log.info("initialising consumer with configs");
        consumer = new KafkaConsumer<>(props);
        startConsuming(kafkaConsumerConfig.getTopic());
    }

    public void startConsuming(String topic) {
        log.info("Starting consuming");
        try {
            consumer.subscribe(Collections.singletonList(topic));
            while (running) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(this.kafkaConsumerConfig.getPollingTimeMs()));
                    records.forEach(record -> {
                        MessageKey messageKey = null;
                        MessageValue messageValue = null;
                        try {
                            messageKey = objectMapper.readValue(record.key(), MessageKey.class);
                            messageValue = objectMapper.readValue(record.value(), MessageValue.class);
                        } catch (IOException e) {
                            log.error("Error while converting to MessageKey or MessageValue");
                            throw new RuntimeException(e);
                        }
                        dbStreamProcessor.process(messageKey, messageValue);
                    });
                } catch (Exception e) {
                    log.error("Fatal error encountered: {}", e.getMessage());
                    stopConsumer(); // Shutdown on fatal error
                }
            }
        } finally {
            stopConsumer(); // Ensure consumer is closed properly
        }
    }

    public void stopConsumer() {
        if (!running) return;
        running = false;
        if (consumer != null) {
            try {
                consumer.close();
            } catch (Exception e) {
                log.error("Error while closing Kafka consumer: {}", e.getMessage());
            }
        }
        log.info("Consumer shut down gracefully.");
    }
}