package org.example.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.kafka.dto.OrderEvent;
import org.example.kafka.dto.OrderResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaConfig {


    @Bean
    public ProducerFactory<String, OrderEvent> orderEventProducerFactory(
            KafkaProperties properties) {


        Map<String, Object> config = new HashMap<>();

        config.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                properties.getBootstrapServers()
        );

        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );

        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );


        return new DefaultKafkaProducerFactory<>(config);
    }



    @Bean(name = "orderKafkaTemplate")
    public KafkaTemplate<String, OrderEvent> orderKafkaTemplate(
            @Qualifier("orderEventProducerFactory")
            ProducerFactory<String, OrderEvent> factory) {

        return new KafkaTemplate<>(factory);
    }



    @Bean
    public ProducerFactory<String, OrderResult> orderResultProducerFactory(
            KafkaProperties properties) {


        Map<String, Object> config = new HashMap<>();

        config.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                properties.getBootstrapServers()
        );

        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );

        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );


        return new DefaultKafkaProducerFactory<>(config);
    }



    @Bean(name = "orderResultKafkaTemplate")
    public KafkaTemplate<String, OrderResult> orderResultKafkaTemplate(
            @Qualifier("orderResultProducerFactory")
            ProducerFactory<String, OrderResult> factory) {

        return new KafkaTemplate<>(factory);
    }



    @Bean
    public ConsumerFactory<String, OrderEvent> consumerFactory(
            KafkaProperties properties) {


        Map<String, Object> config = new HashMap<>();


        config.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                properties.getBootstrapServers()
        );

        config.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "orders-group"
        );

        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        config.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class
        );


        JsonDeserializer<OrderEvent> deserializer =
                new JsonDeserializer<>(OrderEvent.class);


        deserializer.addTrustedPackages(
                "org.example.kafka.dto"
        );


        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }



    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderEvent>
    kafkaListenerContainerFactory(
            ConsumerFactory<String, OrderEvent> consumerFactory) {


        ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();


        factory.setConsumerFactory(consumerFactory);


        return factory;
    }

}