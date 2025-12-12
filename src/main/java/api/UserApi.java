package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;
import models.UserResponse;
import utils.AssertionsHelper;
import static config.RequestSpec.requestSpec;
import static io.restassured.RestAssured.given;

public class UserApi {

    private final AssertionsHelper assertionsHelper = new AssertionsHelper();

    //эндпоинты
    private static final String REGISTER_USER = "/api/auth/register";
    private static final String LOGIN_USER = "/api/auth/login";
    private static final String DATA_USER = "/api/auth/user"; //получение, изменение или удаление пользователя

    //шаги
    @Step("Создание пользователя: {user}")
    public Response createUserStep(User user){
        return given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post(REGISTER_USER);
    }

    @Step("Авторизация пользователя: {user}")
    public Response loginUserStep(User user){
        return given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post(LOGIN_USER);
    }

    @Step("Получить accessToken пользователя: {user}")
    public String getAccessTokenStep(User user){
        Response response = loginUserStep(user);
        UserResponse userResponse = response.as(UserResponse.class);

        return userResponse.getAccessToken();
    }

    @Step("Изменение данных пользователя: {user}")
    public Response changeUserStep(User user, String token){
        return given()
                .spec(requestSpec)
                .header("Authorization", token)
                .body(user)
                .when()
                .patch(DATA_USER);
    }

    @Step("Удаление пользователя, если существует: {user}")
    public String deleteUserStep(User user) {
        if (user == null) {
            return "Объект не создавался";
        }

        String token = getAccessTokenStep(user);
        if (token != null) {
            Response response =
                    given()
                            .spec(requestSpec)
                            .header("Authorization", token)
                            .when()
                            .delete(DATA_USER);
            assertionsHelper.checkStatusCode202Step(response);
            return "Юзер удален";
        } else {
            return "Юзер не был создан";
        }
    }
}
