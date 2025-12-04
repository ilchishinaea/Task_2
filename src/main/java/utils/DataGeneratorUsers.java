package utils;

import io.qameta.allure.Step;
import models.User;
import org.junit.jupiter.params.provider.Arguments;
import java.util.UUID;
import java.util.stream.Stream;

public class DataGeneratorUsers {

    @Step("Генерируем рандомного юзера c почтой, паролем и именем")
    public static User randomUser(){
        return new User(
                randomEmail(),
                randomPassword(),
                randomName()
        );
    }

    @Step("Генерация негативных тестовых юзеров для регистрации c почтой, паролем и именем")
    public static Stream<Arguments> userCreateData(){
        return Stream.of(
                Arguments.of(null, randomPassword(), randomName()),
                Arguments.of(randomEmail(), null, randomName()),
                Arguments.of(randomEmail(), randomPassword(), null)
        );
    }

    @Step("Генерируем рандомную почту")
    public static String randomEmail() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8) + "@test.ru";
    }

    @Step("Генерируем рандомный пароль")
    public static String randomPassword() {
        return "pass_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Step("Генерируем рандомное имя")
    public static String randomName() {
        return "name_" + UUID.randomUUID().toString().substring(0, 6);
    }

}
