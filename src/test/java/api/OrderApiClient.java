package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Order;
import utils.Endpoints;

import static io.restassured.RestAssured.given;

public class OrderApiClient {
    public static Response createOrder(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .post(Endpoints.CREATE_ORDER);
    }

    public static Response getOrdersList() {
        return given()
                .get(Endpoints.GET_ORDERS);
    }
}
