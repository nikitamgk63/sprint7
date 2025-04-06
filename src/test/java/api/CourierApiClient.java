package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Courier;
import models.LoginCredentials;
import static io.restassured.RestAssured.given;
import static utils.Endpoints.*;

public class CourierApiClient {
    public static Response createCourier(Courier courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .post(CREATE_COURIER);
    }

    public static Response loginCourier(LoginCredentials credentials) {
        return given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .post(LOGIN_COURIER);
    }

    public static Response deleteCourier(int courierId) {
        return given()
                .pathParam("id", courierId)
                .delete(DELETE_COURIER);
    }
}