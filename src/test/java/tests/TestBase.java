package tests;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import utils.Endpoints;

public class TestBase {
    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = Endpoints.BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}