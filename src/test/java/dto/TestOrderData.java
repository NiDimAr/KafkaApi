package dto;

import lombok.Data;

@Data
public class TestOrderData {

    private String orderId;
    private String customer;
    private double amount;
    private String expectedStatus;


    public TestOrderData(
            String orderId,
            String customer,
            double amount,
            String expectedStatus
    ) {
        this.orderId = orderId;
        this.customer = customer;
        this.amount = amount;
        this.expectedStatus = expectedStatus;
    }


    @Override
    public String toString() {
        return "OrderTestData{" +
                "orderId='" + orderId + '\'' +
                ", customer='" + customer + '\'' +
                ", amount=" + amount +
                ", expectedStatus='" + expectedStatus + '\'' +
                '}';
    }
}
