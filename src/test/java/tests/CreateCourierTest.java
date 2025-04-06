package tests;

import api.CourierApiClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import models.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import net.datafaker.Faker;

import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.*;
import static utils.Endpoints.BASE_URL;

public class CreateCourierTest extends TestBase {
    private Faker faker = new Faker();
    private String login = faker.name().username() + "_" + System.currentTimeMillis();
    private String password = faker.internet().password();
    private String firstName = faker.name().firstName();
    private Integer courierId;

    @Before
    public void setUp() {
        faker = new Faker();
        login = faker.name().username() + "_" + System.currentTimeMillis();
        password = faker.internet().password();
        firstName = faker.name().firstName();
        RestAssured.baseURI = BASE_URL;
    }


    @Test
    @DisplayName("POST /api/v1/courier - Создание курьера с firstName")
    @Description("Проверка успешного создания курьера со всеми полями")
    public void createCourierWithFirstNameTest() {
        Courier courier = new Courier(login, password, firstName);
        CourierApiClient.createCourier(courier)
                .then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("POST /api/v1/courier - Успешное создание курьера")
    @Description("Проверка успешного создания курьера с обязательными полями")
    public void successfulCourierCreationTest() {
        Courier courier = new Courier(login, password);
        courierId = CourierApiClient.createCourier(courier)
                .then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true))
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("POST /api/v1/courier - Отсутствие логина")
    @Description("Проверка ошибки при отсутствии логина")
    public void courierCreationWithoutLoginTest() {
        Courier courier = new Courier(null, password, firstName);
        CourierApiClient.createCourier(courier)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("POST /api/v1/courier - Отсутствие пароля")
    @Description("Проверка ошибки при отсутствии пароля")
    public void courierCreationWithoutPasswordTest() {
        Courier courier = new Courier(login, null, firstName);
        CourierApiClient.createCourier(courier)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
        @After
        public void tearDown() {
            if (courierId != null) {
                CourierApiClient.deleteCourier(courierId)
                        .then()
                        .statusCode(SC_OK);
            }
        }
}