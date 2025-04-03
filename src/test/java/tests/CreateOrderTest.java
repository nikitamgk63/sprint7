package tests;

import io.qameta.allure.junit4.DisplayName;
import models.OrderCancelRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import api.OrderApiClient;
import models.Order;

import java.util.Arrays;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class CreateOrderTest extends TestBase {
    private final Order order;
    private String trackNumber;

    public CreateOrderTest(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}")
    public static Object[][] getOrderData() {
        return new Object[][]{
                {new Order(
                        "Naruto",
                        "Uzumaki",
                        "Konoha, 142 apt.",
                        4,
                        "+7 800 355 35 35",
                        5,
                        "2023-12-31",
                        "Sasuke, come back to Konoha",
                        Arrays.asList("BLACK")
                )},
                {new Order(
                        "Sasuke",
                        "Uchiha",
                        "Orochimaru's Hideout",
                        3,
                        "+7 800 355 35 36",
                        3,
                        "2023-12-30",
                        "I'll come back when I'm ready",
                        Arrays.asList("GREY")
                )},
                {new Order(
                        "Sakura",
                        "Haruno",
                        "Konoha Medical Center",
                        5,
                        "+7 800 355 35 37",
                        7,
                        "2024-01-01",
                        "Waiting for both of you",
                        Arrays.asList("BLACK", "GREY")
                )},
                {new Order(
                        "Kakashi",
                        "Hatake",
                        "Hokage Office",
                        1,
                        "+7 800 355 35 38",
                        1,
                        "2023-12-25",
                        "Mission complete",
                        null
                )}
        };
    }

    @Test
    @DisplayName("POST /api/v1/orders - Создание заказа с различными параметрами цвета")
    public void createOrderWithDifferentColorsTest() {
        trackNumber = OrderApiClient.createOrder(order)
                .then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue())
                .extract()
                .path("track")
                .toString();
        assertThat("Номер трека должен быть положительным", trackNumber, not(emptyString()));
    }

    @After
    public void tearDown() {
        if (trackNumber != null) {
            OrderApiClient.cancelOrder(new OrderCancelRequest(trackNumber))
                    .then()
                    .statusCode(anyOf(is(SC_OK), is(SC_NOT_FOUND), is(SC_CONFLICT)));
        }
    }
}