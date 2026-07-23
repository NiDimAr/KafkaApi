package tests;

import config.KafkaTestContainerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.kafka.KafkaApplication;
import org.example.kafka.dto.OrderResult;
import org.example.kafka.producer.OrderProducer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = KafkaApplication.class)
@Import(KafkaTestContainerConfig.class)
public abstract class BaseKafkaTest {


    @Autowired
    protected OrderProducer producer;


    protected org.apache.kafka.clients.consumer.Consumer<String, OrderResult> consumer;



    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry){

        registry.add(
                "spring.kafka.bootstrap-servers",
                KafkaTestContainerConfig.kafkaContainer::getBootstrapServers
        );

    }



    @BeforeEach
    void setUpConsumer(){


        Map<String,Object> props = new HashMap<>();


        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                KafkaTestContainerConfig.kafkaContainer.getBootstrapServers()
        );


        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "test-group-" + UUID.randomUUID()
        );


        props.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );


        JsonDeserializer<OrderResult> deserializer =
                new JsonDeserializer<>(OrderResult.class);


        deserializer.addTrustedPackages(
                "org.example.kafka.dto"
        );


        DefaultKafkaConsumerFactory<String, OrderResult> factory =
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        deserializer
                );


        consumer = factory.createConsumer();


        consumer.subscribe(
                Collections.singleton("orders.processed")
        );


        consumer.poll(Duration.ofSeconds(2));

    }



    @AfterEach
    void tearDown(){

        if(consumer != null){
            consumer.close();
        }

    }

}