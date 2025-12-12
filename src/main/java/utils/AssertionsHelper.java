package utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class AssertionsHelper {

    @Step("Статус-код в ответе 200")
    public void checkStatusCode200Step(Response response){
        response.then()
                .assertThat().statusCode(SC_OK);
    }

    @Step("Статус-код в ответе 202")
    public void checkStatusCode202Step(Response response){
        response.then()
                .assertThat().statusCode(SC_ACCEPTED);
    }

    @Step("Статус-код в ответе 400")
    public void checkStatusCode400Step(Response response){
        response.then()
                .assertThat().statusCode(SC_BAD_REQUEST);
    }

    @Step("Статус-код в ответе 401")
    public void checkStatusCode401Step(Response response){
        response.then()
                .assertThat().statusCode(SC_UNAUTHORIZED);
    }

    @Step("Статус-код в ответе 403")
    public void checkStatusCode403Step(Response response){
        response.then()
                .assertThat().statusCode(SC_FORBIDDEN);
    }

    @Step("Статус-код в ответе 500")
    public void checkStatusCode500Step(Response response){
        response.then()
                .assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Поле success в ответе = true")
    public void checkSuccessTrueStep(Response response){
        response.then()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Step("Поле success в ответе = false")
    public void checkSuccessFalseStep(Response response){
        response.then()
                .assertThat()
                .body("success", equalTo(false));
    }

    @Step("Ошибка, если не указаны ингредиенты при создании заказа")
    public void checkMessageWithoutIngredientForOrderStep(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Ошибка регистрации, если юзер существует")
    public void checkMessageExistsUserForRegisterStep(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("User already exists"));
    }

    @Step("Ошибка регистрации, если обязательное поле не заполнено")
    public void checkMessageEmptyFieldForRegisterStep(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Ошибка авторизации, если обязательное поле не заполнено")
    public void checkMessageIncorrectFieldForLoginStep(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Ошибка, если пользователь не авторизован")
    public void checkMessageIncorrectAuthStep(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Ошибка изменения данных, если почта уже используется")
    public void checkMessageExistsMailForPatchAuthStep(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("User with such email already exists"));
    }
}
