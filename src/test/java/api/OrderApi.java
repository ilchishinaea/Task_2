package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static baseTest.BaseTest.requestSpec;
import static io.restassured.RestAssured.given;

public class OrderApi {

    //эндпоинты
    private static final String ORDERS = "/api/orders"; // создание и получение заказов

    //шаги
    @Step("Создание заказа: {jsonBody}")
    public Response createOrderStep(String jsonBody, String token){
        return given()
                .spec(requestSpec)
                .header("Authorization", token)
                .body(jsonBody)
                .when()
                .post(ORDERS);
    }

    @Step("Получение списка заказов юзера")
    public Response getOrdersUserStep(String token){
        return given()
                .spec(requestSpec)
                .header("Authorization", token)
                .when()
                .get(ORDERS);
    }
}
