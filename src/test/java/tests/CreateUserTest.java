package tests;

import api.UserApi;
import baseTest.BaseTest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.AssertionsHelper;
import static utils.DataGeneratorUsers.*;

public class CreateUserTest extends BaseTest {

    private final UserApi userApi = new UserApi();
    private final AssertionsHelper assertionsHelper = new AssertionsHelper();

    User user;

    @BeforeEach
    public void generateUser(){
        Allure.step("Генерация юзера", () -> {
            user = randomUser();
        });
    }

    @Test
    @DisplayName("Создание уникального юзера")
    public void createUniqueUserReturnsSuccessResponse(){
        user = randomUser();
        Response response = userApi.createUserStep(user);
        assertionsHelper.checkStatusCode200Step(response);
        assertionsHelper.checkSuccessTrueStep(response);
    }

    @Test
    @DisplayName("Создание юзера, который уже зарегистрирован")
    public void createExistsUserReturnsFailResponse(){
        user = randomUser();
        Response response = userApi.createUserStep(user);
        assertionsHelper.checkStatusCode200Step(response);
        assertionsHelper.checkSuccessTrueStep(response);
        Response response1 = userApi.createUserStep(user);
        assertionsHelper.checkStatusCode403Step(response1);
        assertionsHelper.checkMessageExistsUserForRegisterStep(response1);
    }

    @ParameterizedTest
    @DisplayName("Создание юзера с незаполненными обязательными полями")
    @MethodSource("utils.DataGeneratorUsers#userCreateData")
    public void createUserWithEmptyFieldReturnsFailResponse(String email, String password, String name){
        user = new User(email, password, name);
        Response response = userApi.createUserStep(user);
        assertionsHelper.checkStatusCode403Step(response);
        assertionsHelper.checkMessageEmptyFieldForRegisterStep(response);
    }

    @AfterEach
    public void deleteUser(){
        Allure.step("Удаление юзера", () -> {
            userApi.deleteUserStep(user);
        });
    }
}
