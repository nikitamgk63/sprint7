package tests;

import api.CourierApiClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.LoginCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import net.datafaker.Faker;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.apache.http.HttpStatus.*;

public class LoginCourierTest {

    private Faker faker;
    private String login;
    private String password;
    private Integer courierId;

    @Before
    public void setUp() {
        faker = new Faker();
        login = faker.name().username() + "_" + System.currentTimeMillis();
        password = faker.internet().password();

        // Создаем тестового курьера
        courierId = createCourier(login, password)
                .then()
                .statusCode(SC_CREATED) // Проверка, что курьер создан успешно
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
        loginCourier(login, password)
                .then()
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("POST /api/v1/courier/login - Неверный логин")
    @Description("Проверка ошибки при неверном логине")
    public void loginWithInvalidLoginTest() {
        // Генерируем неверный логин (добавляем префикс)
        String invalidLogin = "invalid_" + login;

        // Создаем credentials с неверным логином, но верным паролем
        LoginCredentials credentials = new LoginCredentials(invalidLogin, password);

        CourierApiClient.loginCourier(credentials)
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("POST /api/v1/courier/login - Неверный пароль")
    @Description("Проверка ошибки при неверном пароле")
    public void loginWithInvalidPasswordTest() {
        // Генерируем неверный пароль (добавляем префикс)
        String invalidPassword = "wrong_" + password;

        // Создаем credentials с верным логином, но неверным паролем
        LoginCredentials credentials = new LoginCredentials(login, invalidPassword);

        CourierApiClient.loginCourier(credentials)
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("POST /api/v1/courier/login - Отсутствие логина")
    @Description("Проверка ошибки при отсутствии логина")
    public void loginWithoutLoginTest() {
        loginCourier(null, password)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("POST /api/v1/courier/login - Отсутствие пароля")
    @Description("Проверка ошибки при отсутствии пароля")
    public void loginWithoutPasswordTest() {
        loginCourier(login, null)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("POST /api/v1/courier/login - Пустые данные")
    @Description("Проверка ошибки при пустых логине и пароле")
    public void loginWithEmptyCredentialsTest() {
        loginCourier("", "")
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    // Вспомогательные методы

    @Step("Создание курьера")
    private Response createCourier(String login, String password) {
        Map<String, String> body = Map.of(
                "login", login,
                "password", password
        );
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");
    }

    @Step("Авторизация курьера")
    private Response loginCourier(String login, String password) {
        Map<String, String> body = new HashMap<>();
        if (login != null) body.put("login", login);
        if (password != null) body.put("password", password);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/login");
    }

    @Step("Удаление курьера")
    private void deleteCourier(int courierId) {
        given()
                .delete("https://qa-scooter.praktikum-services.ru/api/v1/courier/" + courierId)
                .then()
                .statusCode(SC_OK);
    }
}
