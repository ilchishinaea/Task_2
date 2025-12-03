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
import utils.DataGeneratorOrders;
import static utils.DataGeneratorUsers.randomUser;

public class CreateOrderTest extends BaseTest {

    private final UserApi userApi = new UserApi();
    private final OrderApi orderApi = new OrderApi();
    private final AssertionsHelper assertionsHelper = new AssertionsHelper();
    private final DataGeneratorOrders generatorOrders = new DataGeneratorOrders();

    User user;
    String validIng = generatorOrders.getIngredientsJsonBodyValidHash();
    String invalidIng = generatorOrders.getIngredientsJsonBodyInvalidHash();
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
    @DisplayName("Создание заказа с авторизованным юзером и валидными ингредиентами")
    public void createOrderWithAuthValidReturnsSuccessResponse(){
        Response response = orderApi.createOrderStep(validIng, token);
        assertionsHelper.checkStatusCode200Step(response);
        assertionsHelper.checkSuccessTrueStep(response);
    }

    @Test
    @DisplayName("Создание заказа с неавторизованным юзером и валидными ингредиентами")
    public void createOrderWithAuthInvalidReturnsFailResponse(){
        Response response = orderApi.createOrderStep(validIng, "");
        assertionsHelper.checkStatusCode401Step(response); //тест написан согласно документации, но фактически заказ создается без авторизации
        assertionsHelper.checkMessageIncorrectAuthStep(response);
    }

    @Test
    @DisplayName("Создание заказа с авторизованным юзером и без ингредиентов")
    public void createOrderWithoutIngReturnsFailResponse(){
        Response response = orderApi.createOrderStep("", token);
        assertionsHelper.checkStatusCode400Step(response);
        assertionsHelper.checkMessageWithoutIngredientForOrderStep(response);
    }

    @Test
    @DisplayName("Создание заказа с авторизованным юзером и неврным хеш ингредиента")
    public void createOrderInvalidHashIngReturnsFailResponse(){
        Response response = orderApi.createOrderStep(invalidIng, token);
        assertionsHelper.checkStatusCode500Step(response);
    }

    @AfterEach
    public void deleteUser(){
        Allure.step("Удаление юзера", () -> {
            userApi.deleteUserStep(user);
        });
    }
}
