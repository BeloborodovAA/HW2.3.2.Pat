package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.RegistrationDto;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class LoginTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    // Тест 1: Успешный вход активного пользователя
    @Test
    void shouldLoginSuccessfully() {
        RegistrationDto user = DataGenerator.createUser("active");

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

         $("h2").shouldHave(Condition.text("Личный кабинет"), Duration.ofSeconds(5));
    }

    // Тест 2: Попытка входа заблокированного пользователя
    @Test
    void shouldNotLoginBlockedUser() {
        RegistrationDto user = DataGenerator.createUser("blocked");

        // 2. Вводим данные в форму
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Пользователь заблокирован"));
    }

    // Тест 3: Неверный логин или пароль
    @Test
    void shouldNotLoginWithWrongCredentials() {
        RegistrationDto user = DataGenerator.createUser("active");

        $("[data-test-id='login'] input").setValue(user.getLogin());
        String wrongPassword = DataGenerator.generatePassword();
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}
