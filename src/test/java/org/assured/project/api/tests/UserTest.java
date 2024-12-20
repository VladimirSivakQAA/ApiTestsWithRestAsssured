package org.assured.project.api.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.assertj.core.api.SoftAssertions;
import org.assured.project.api.assertions.AssertResponse;
import org.assured.project.api.controllers.UserControllers;
import org.assured.project.api.payloads.get_users.GetUsers;
import org.assured.project.api.payloads.post_create_users.request.PostCreateUserReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API")
@Feature("User")
public class UserTest {
    private static final UserControllers USER_CONTROLLERS = new UserControllers();
    private static final SoftAssertions softly = new SoftAssertions();

    @Test
    @Tag("smoke")
    @Story("GetUserByPage")
    @Description("Тестирование с использованием AssertJ и Pojo")
    @DisplayName("Standart Test")
    public void getUsersPageTest() {
        int page = 2;

        Response response = USER_CONTROLLERS.getUsersByPageNumber(page);
        assertThat(response.statusCode()).isEqualTo(200); // проверка какой статус код ожидаем
        softly.assertThat(response.jsonPath().getInt("page"))
                .as("First Check")
                .isEqualTo(page);
        GetUsers getUsers = response.as(GetUsers.class);
        softly.assertThat(getUsers.page()).isEqualTo(page);
        softly.assertAll();
    }

    @Test
    @Story("GetUserByPage")
    @DisplayName("Email testing with Pojo class")
    public void emailFromPOJOTest() {
        int page = 2;
        String expectedEmail = "tobias.funke@reqres.in";

        Response usersByPageNumber = USER_CONTROLLERS.getUsersByPageNumber(page);
        assertThat(usersByPageNumber.statusCode()).isEqualTo(200); //
        GetUsers getUsers = usersByPageNumber.as(GetUsers.class);

        String email = getUsers.data().stream()
                .filter(dataItem -> dataItem.id().equals(9))
                .findFirst()
                .orElseThrow(() -> new AssertionError("email with id %s not found".formatted(9)))
                .email();

        softly.assertThat(email).isEqualTo(expectedEmail); //1 вариант проверки (не обязательно, можно стримом проверить)

        boolean isEmailPresent = getUsers.data().stream()
                .anyMatch(e -> e.email().equals(expectedEmail));

        softly.assertThat(isEmailPresent).isTrue(); // 2 вариант проверки

        softly.assertThat(getUsers)
                .hasFieldOrPropertyWithValue("page", 2)
                .hasFieldOrPropertyWithValue("total", 12);
        softly.assertAll();
    }

    @Test
    @Tag("smoke")
    @Story("Create User")
    @DisplayName("Create user from String")
    public void createUserFromString() {
        String email = "someNewEmail@reqres.in";
        String json = "{\n" + "    \"email\": \"%s\",\n".formatted(email) + "    \"password\": \"somePassword\"\n" + "}";
        Response response = USER_CONTROLLERS.postCreateEntity(json);
        response.then().statusCode(201); // использование ValidatableResponse
        String emailFromResponse = response.jsonPath().getString("email");
        assertThat(email).isEqualTo(emailFromResponse);

    }

    @Test
    @Tag("smoke")
    @Story("Update User")
    @DisplayName("Update User Entity")
    public void updateUser() {
        String json = "{\"first_name\": \"morpheus\", \"last_name\": \"zion\"}";
        Response response = USER_CONTROLLERS.updateUserEntity(json, 2);
        response.then().statusCode(200);
        assertThat(response.getBody().as(Object.class)).hasFieldOrProperty("updatedAt");
    }


    //создание юзера по модельке
    @Test
    @Tag("smoke")
    @Story("Create User")
    @DisplayName("Create User from Pojo")
    public void createUserByModel() {
        PostCreateUserReq req = new PostCreateUserReq()
                .name("Alex")
                .job("QA");
        Response response = USER_CONTROLLERS.postCreateEntity(req); //альт+энтер создаем объект респонс для проверки
        assertThat(response.statusCode()).isEqualTo(201); //проверка на статус код 201
    }

    // создание юзера из файла
    @SneakyThrows
    @Test
    @Tag("smoke")
    @Story("Create User")
    @DisplayName("Create User from File")
    public void createUserFromFile() { // создаем юзера через джейсон файл
        File file = new File(Resources.getResource("test_data/user.json").getFile()); // создаем объект класса файл и собираем его из ресурсес
        String json = Files.readString(file.toPath());
        Response response = USER_CONTROLLERS.postCreateEntity(json);
        assertThat(response.statusCode()).isEqualTo(201);
    }

    @SneakyThrows
    @Test
    @Tag("smoke")
    @Story("Get User")
    @DisplayName("Get User as Map")
    public void getUsersMapPage() {
        int id = 10;
        Response response = USER_CONTROLLERS.getUsersByPageNumber(2);
        assertThat(response.statusCode()).isEqualTo(200);
        // используется для серииализации и десериализации
        ObjectMapper objectMapper = new ObjectMapper();
        // получить тело ответа как строку
        String responseBody = objectMapper.writeValueAsString(response.getBody().jsonPath().get());
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<>() {
        });
        // представить ответ как мар
        softly.assertThat(responseMap.get("page")).isEqualTo(2);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>>
                data = (List<Map<String, Object>>) responseMap.get("data");
        boolean isHasId = data.stream()
                .anyMatch(e -> e.get("id").equals(id));
        softly.assertThat(isHasId)
                .withFailMessage(" Id %s отсутсвует в массиве data".formatted(id))
                .isTrue();
        softly.assertAll();

    }

    @Test
    @Tag("smoke")
    @Story("Get User")
    @DisplayName("Get User with own assertions")
    public void checkUserWithOwnAssertions() {
        Response response = USER_CONTROLLERS.getUsersByPageNumber(2);
        AssertResponse.assertThat(response)
                .checkStatusCode(200)
                .checkStringByPath("total_pages", "2")
                .assertAll();
    }
}

