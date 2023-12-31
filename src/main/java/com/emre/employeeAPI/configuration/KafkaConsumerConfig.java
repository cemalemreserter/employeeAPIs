package com.emre.employeeAPI.configuration;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.retry.policy.SimpleRetryPolicy;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.emre.employeeAPI.constants.AppConstants.*;


@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Autowired
    private ConsumerFactory<Integer, String> consumerFactory;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        factory.setConcurrency(FACTORY_CONCURRENCY_COUNT);
        factory.getContainerProperties().setIdleBetweenPolls(IDLE_BETWEEN_POLLS);
        return factory;
    }

    private Map<String, Object> consumerConfig() {
        Map<String, Object> consumerConfig = new HashMap<>(consumerFactory.getConfigurationProperties());
        consumerConfig.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, MAX_POLL_INTERVAL_MS_CONFIG);
        consumerConfig.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, SESSION_TIMEOUT_MS_CONFIG );
        consumerConfig.put(ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, DEFAULT_API_TIMEOUT_MS_CONFIG);
        consumerConfig.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS_CONFIG);
        consumerConfig.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, GROUP_INSTANCE_ID_CONFIG);
        consumerConfig.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, MAX_PARTITION_FETCH_BYTES_CONFIG);
        consumerConfig.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, FETCH_MAX_BYTES_CONFIG);

        return consumerConfig;
    }


    private SimpleRetryPolicy getSimpleRetryPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();
        exceptionMap.put(IllegalArgumentException.class, false);
        exceptionMap.put(TimeoutException.class, true);
        return new SimpleRetryPolicy(MAX_RETRY_ATTEMPT,exceptionMap,true);
    }

    public ConsumerFactory<String, String> employeeEventConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_ADDRESS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new StringDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    employeeEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String,String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(employeeEventConsumerFactory());
        return factory;
    }
}