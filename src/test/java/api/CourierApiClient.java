package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Courier;
import models.LoginCredentials;
import utils.Endpoints;
import static io.restassured.RestAssured.given;

public class CourierApiClient {
    public static Response createCourier(Courier courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .post(Endpoints.CREATE_COURIER);
    }

    public static Response loginCourier(LoginCredentials credentials) {
        return given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .post(Endpoints.LOGIN_COURIER);
    }

    public static Response deleteCourier(int courierId) {
        return given()
                .pathParam("id", courierId)
                .delete(Endpoints.DELETE_COURIER);
    }
}
