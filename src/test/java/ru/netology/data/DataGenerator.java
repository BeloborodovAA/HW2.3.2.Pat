package ru.netology.data;

import com.github.javafaker.Faker;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {}

    public static String generateLogin() {
        return new Faker(new Locale("en")).name().username();
    }

    public static String generatePassword() {
        return new Faker(new Locale("en")).internet().password();
    }

    public static RegistrationDto createUser(String status) {
        String login = generateLogin();
        String password = generatePassword();

        RegistrationDto user = new RegistrationDto(login, password, status);

        given()
                .baseUri("http://localhost")
                .port(9999)
                .contentType("application/json")
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        return user;
    }
}