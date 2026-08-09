package tests;

import dto.TestOrderData;
import org.example.kafka.dto.OrderEvent;
import org.example.kafka.dto.OrderResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.CsvReader;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NegativeOrderKafkaTest extends BaseKafkaTest {
    static java.util.stream.Stream<TestOrderData> negativeOrders() {

        return CsvReader.readOrders("test-data/orders.csv")
                .stream()
                .filter(order ->
                        order.getAmount() <= 0
                );
    }



    @ParameterizedTest
    @MethodSource("negativeOrders")
    void shouldRejectInvalidOrder(TestOrderData data) {


        producer.sendOrder(
                new OrderEvent(
                        data.getOrderId(),
                        data.getCustomer(),
                        data.getAmount()
                )
        );


        await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {


                    var records =
                            consumer.poll(Duration.ofSeconds(1));


                    if(records.isEmpty()){
                        throw new AssertionError(
                                "No message received"
                        );
                    }


                    OrderResult result =
                            records.iterator()
                                    .next()
                                    .value();



                    assertEquals(
                            data.getOrderId(),
                            result.getOrderId()
                    );


                    assertEquals(
                            data.getExpectedStatus(),
                            result.getStatus().name()
                    );

                });

    }
}
