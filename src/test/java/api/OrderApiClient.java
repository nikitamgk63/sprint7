package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Order;
import models.OrderCancelRequest;
import static io.restassured.RestAssured.given;
import static utils.Endpoints.*;

public class OrderApiClient {
    public static Response createOrder(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .post(CREATE_ORDER);
    }

    public static Response cancelOrder(OrderCancelRequest cancelRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(cancelRequest)
                .put(CANCEL_ORDER);
    }

    public static Response getOrdersList() {
        return given()
                .get(CREATE_ORDER);
    }
}