package tests;

import api.UserApi;
import baseTest.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.AssertionsHelper;
import java.util.stream.Stream;
import static utils.DataGeneratorUsers.*;

public class ChangeUserTest extends BaseTest {

    private final UserApi userApi = new UserApi();
    private final AssertionsHelper assertionsHelper = new AssertionsHelper();

    static User user;
    User userChange;
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
    @DisplayName("Изменение почты юзера на существующую с авторизацией неуспешно")
    public void changeUserExistsEmailWithAuthReturnsCode403(){
        userChange = randomUser();
        Response response = userApi.createUserStep(userChange);
        assertionsHelper.checkStatusCode200Step(response);

        String token = userApi.getAccessTokenStep(user);

        userInvalid = new User(userChange.getEmail(), user.getName());

        Response response1 = userApi.changeUserStep(userInvalid, token);
        assertionsHelper.checkStatusCode403Step(response1);
        assertionsHelper.checkMessageExistsMailForPatchAuthStep(response1);
    }

    @Test
    @DisplayName("Изменение почты юзера на существующую без авторизации неуспешно")
    public void changeUserExistsEmailWithoutAuthReturnsCode401(){
        userChange = randomUser();
        Response response = userApi.createUserStep(userChange);
        assertionsHelper.checkStatusCode200Step(response);

        userInvalid = new User(userChange.getEmail(), user.getName());

        Response response1 = userApi.changeUserStep(userInvalid, "");
        assertionsHelper.checkStatusCode401Step(response1);
        assertionsHelper.checkMessageIncorrectAuthStep(response1);
    }

    @ParameterizedTest
    @DisplayName("Изменение почты/почты и имени юзера с авторизацией успешно")
    @MethodSource("userChangeData")
    public void changeUserWithAuthReturnsCode200(String email, String name){
        String token = userApi.getAccessTokenStep(user);
        userChange = new User(email, name);
        Response response = userApi.changeUserStep(userChange, token);
        assertionsHelper.checkStatusCode200Step(response);
        assertionsHelper.checkSuccessTrueStep(response);
    }

    @ParameterizedTest
    @DisplayName("Изменение почты/почты и имени юзера без авторизации неуспешно")
    @MethodSource("userChangeData")
    public void changeUserWithoutAuthReturnsCode401(String email, String name){
        userChange = new User(email, name);
        Response response = userApi.changeUserStep(userChange, "");
        assertionsHelper.checkStatusCode401Step(response);
        assertionsHelper.checkMessageIncorrectAuthStep(response);
    }

    @AfterEach
    public void deleteUser(){
        Allure.step("Удаление юзеров", () -> {
            userApi.deleteUserStep(userInvalid);
            userApi.deleteUserStep(userChange);
            userApi.deleteUserStep(user);
        });
    }

    @Step("Генерация данных для изменения юзера")
    public static Stream<Arguments> userChangeData(){
        return Stream.of(
                Arguments.of(randomEmail(), user.getName()),
                Arguments.of(randomEmail(), randomName())
        );
    }
}
