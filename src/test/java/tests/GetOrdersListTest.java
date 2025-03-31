package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import api.OrderApiClient;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersListTest {
    @Test
    @DisplayName("GET /api/v1/orders - Получение списка заказов")
    public void getOrdersListTest() {
        OrderApiClient.getOrdersList()
                .then()
                .statusCode(SC_OK)
                .body("orders", notNullValue());
    }
}