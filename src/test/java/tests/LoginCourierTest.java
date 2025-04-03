package tests;

import api.CourierApiClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import models.Courier;
import models.LoginCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import net.datafaker.Faker;

import static api.CourierApiClient.deleteCourier;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.apache.http.HttpStatus.*;
import static utils.Endpoints.BASE_URL;

public class LoginCourierTest extends TestBase {
    private Faker faker;
    private String login;
    private String password;
    private String firstName;
    private Integer courierId;

    @Before
    public void setUp() {
        faker = new Faker();
        login = faker.name().username() + "_" + System.currentTimeMillis();
        password = faker.internet().password();
        firstName = faker.name().firstName();
        RestAssured.baseURI = BASE_URL;

        Courier courier = new Courier(login, password, firstName);
        courierId = CourierApiClient.createCourier(courier)
                .then()
                .statusCode(SC_CREATED)
                .extract()
                .path("id");
    }

    @After
    public void tearDown() {
        // Удаляем курьера после тестов, если он был создан
        if (courierId != null) {
            deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("POST /api/v1/courier/login - Успешная авторизация")
    @Description("Проверка успешной авторизации с валидными логином и паролем")
    public void successfulLoginTest() {
        CourierApiClient.loginCourier(new LoginCredentials(login, password))
                .then()
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("POST /api/v1/courier/login - Неверный логин")
    @Description("Проверка ошибки при неверном логине")
    public void loginWithInvalidLoginTest() {
        String invalidLogin = "invalid_" + login;
        CourierApiClient.loginCourier(new LoginCredentials(invalidLogin, password))
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("POST /api/v1/courier/login - Неверный пароль")
    @Description("Проверка ошибки при неверном пароле")
    public void loginWithInvalidPasswordTest() {
        String invalidPassword = "wrong_" + password;
        CourierApiClient.loginCourier(new LoginCredentials(login, invalidPassword))
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("POST /api/v1/courier/login - Отсутствие логина")
    @Description("Проверка ошибки при отсутствии логина")
    public void loginWithoutLoginTest() {
        CourierApiClient.loginCourier(new LoginCredentials(null, password))
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("POST /api/v1/courier/login - Отсутствие пароля")
    @Description("Проверка ошибки при отсутствии пароля")
    public void loginWithoutPasswordTest() {
        CourierApiClient.loginCourier(new LoginCredentials(login, null))
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("POST /api/v1/courier/login - Пустые данные")
    @Description("Проверка ошибки при пустых логине и пароле")
    public void loginWithEmptyCredentialsTest() {
        CourierApiClient.loginCourier(new LoginCredentials("", ""))
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}