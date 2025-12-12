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
import utils.AssertionsHelper;
import static utils.DataGeneratorUsers.*;

public class LoginUserTest extends BaseTest {

    private final UserApi userApi = new UserApi();
    private final AssertionsHelper assertionsHelper = new AssertionsHelper();

    static User user;
    User userInvalid;

    @BeforeEach
    public void createRandomUser(){
        Allure.step("Создание юзера", () -> {
            user = randomUser();
            Response response = userApi.createUserStep(user);
            assertionsHelper.checkStatusCode200Step(response);
        });
    }

    @Test
    @DisplayName("Авторизация под существующим юзером")
    public void authExistsUserReturnsSuccessResponse(){
        Response response = userApi.loginUserStep(user);
        assertionsHelper.checkStatusCode200Step(response);
        assertionsHelper.checkSuccessTrueStep(response);
    }

    @Test
    @DisplayName("Авторизация с ошибкой в почте")
    public void authIncorrectEmailReturnsFailResponse(){
        userInvalid = new User(randomEmail(), user.getPassword(), user.getName());
        Response response1 = userApi.loginUserStep(userInvalid);
        assertionsHelper.checkStatusCode401Step(response1);
        assertionsHelper.checkSuccessFalseStep(response1);
        assertionsHelper.checkMessageIncorrectFieldForLoginStep(response1);
    }

    @Test
    @DisplayName("Авторизация с ошибкой в пароле")
    public void authIncorrectPassReturnsFailResponse(){
        userInvalid = new User(user.getEmail(), randomPassword(), user.getName());
        Response response1 = userApi.loginUserStep(userInvalid);
        assertionsHelper.checkStatusCode401Step(response1);
        assertionsHelper.checkSuccessFalseStep(response1);
        assertionsHelper.checkMessageIncorrectFieldForLoginStep(response1);
    }

    @Test
    @DisplayName("Авторизация с ошибкой в почте и пароле")
    public void authIncorrectEmailAndPassReturnsFailResponse(){
        userInvalid = new User(randomEmail(), randomPassword(), user.getName());
        Response response1 = userApi.loginUserStep(userInvalid);
        assertionsHelper.checkStatusCode401Step(response1);
        assertionsHelper.checkSuccessFalseStep(response1);
        assertionsHelper.checkMessageIncorrectFieldForLoginStep(response1);
    }

    @AfterEach
    public void deleteUser(){
        Allure.step("Удаление юзеров", () -> {
            userApi.deleteUserStep(userInvalid);
            userApi.deleteUserStep(user);
        });
    }
}
