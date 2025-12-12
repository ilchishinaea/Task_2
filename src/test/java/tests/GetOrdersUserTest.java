package tests;

import api.OrderApi;
import api.UserApi;
import baseTest.BaseTest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.AssertionsHelper;
import static utils.DataGeneratorUsers.randomUser;

public class GetOrdersUserTest extends BaseTest {

    private final UserApi userApi = new UserApi();
    private final OrderApi orderApi = new OrderApi();
    private final AssertionsHelper assertionsHelper = new AssertionsHelper();

    User user;
    String token;

    @BeforeEach
    public void createAndGetTokenRandomUser(){
        Allure.step("Создание юзера и получение токена", () -> {
            user = randomUser();
            Response response = userApi.createUserStep(user);
            assertionsHelper.checkStatusCode200Step(response);
            token = userApi.getAccessTokenStep(user);
        });
    }

    @Test
    @DisplayName("Получение списка заказов авторизованного юзера")
    public void getOrdersWithAuthReturnsSuccessResponse(){
        Response response = orderApi.getOrdersUserStep(token);
        assertionsHelper.checkStatusCode200Step(response);
        assertionsHelper.checkSuccessTrueStep(response);
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованного юзера")
    public void getOrdersWithoutAuthReturnsFailResponse(){
        Response response = orderApi.getOrdersUserStep("");
        assertionsHelper.checkStatusCode401Step(response);
        assertionsHelper.checkMessageIncorrectAuthStep(response);
    }

    @AfterEach
    public void deleteUser(){
        Allure.step("Удаление юзера", () -> {
            userApi.deleteUserStep(user);
        });
    }
}
