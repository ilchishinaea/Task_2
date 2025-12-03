package utils;

import io.qameta.allure.Step;

import java.util.UUID;

public class DataGeneratorOrders {

    @Step("Генерируем тело с валидным хеш")
    public String getIngredientsJsonBodyValidHash() {
        return "{ \"ingredients\": [\"61c0c5a71d1f82001bdaaa70\", \"61c0c5a71d1f82001bdaaa6c\", \"61c0c5a71d1f82001bdaaa7a\"] }";
    }

    @Step("Генерируем тело с невалидным хеш")
    public String getIngredientsJsonBodyInvalidHash() {
        String hash = randomHash();
        return "{ \"ingredients\": [\"" + hash + "\"] }";
    }

    @Step("Генерируем рандомный хеш")
    public static String randomHash() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 23);
    }
}
