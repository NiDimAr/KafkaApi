package config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.apache.kafka.clients.admin.NewTopic;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;


@TestConfiguration
public class KafkaTestContainerConfig {


    public static final KafkaContainer kafkaContainer =
            new KafkaContainer(
                    DockerImageName.parse("apache/kafka:3.8.0")
            );


    static {
        kafkaContainer.start();
    }



    @Bean
    public KafkaAdmin kafkaAdmin(){

        Map<String,Object> configs = new HashMap<>();

        configs.put(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaContainer.getBootstrapServers()
        );

        return new KafkaAdmin(configs);
    }



    @Bean
    public NewTopic ordersCreatedTopic(){

        return TopicBuilder
                .name("orders.created")
                .partitions(1)
                .replicas(1)
                .build();
    }



    @Bean
    public NewTopic ordersProcessedTopic(){

        return TopicBuilder
                .name("orders.processed")
                .partitions(1)
                .replicas(1)
                .build();
    }

}