package org.assured.project.api.controllers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assured.project.api.controllers.urls.URLS;
import org.assured.project.api.payloads.post_create_users.request.PostCreateUserReq;

public class UserControllers extends BaseApiController {
    @Step("Получить сущность по номеру страницы")
    public Response getUsersByPageNumber(int pageNumber) {
        return getBaseRequestSpec()
                .basePath(URLS.USERS)
                .queryParam("page", pageNumber)
                .when()
                .get();

    }

    @Step("Создать сущность {0}")
    public Response postCreateEntity(String body) { // класс контроллер
        return getBaseRequestSpec() // что мы возвращаем
                .basePath(URLS.USERS) // путь откуда берем данные
                .body(body) // что мы передаем
                .when() // собрали аннотацию
                .post(); // метод пост, запостили данные
    }

    @Step("Создать сущность {0}")
    public Response postCreateEntity(PostCreateUserReq body) {
        return getBaseRequestSpec()
                .basePath(URLS.USERS)
                .body(body)
                .when()
                .post();
    }

    @Step("Обновить данные сущности")
    public Response updateUserEntity(String body, int path) {
        return getBaseRequestSpec()
                .basePath(URLS.PATH_USERS)
                .pathParam("path", path )
                .body(body)
                .when()
                .put();
    }

    @Step("Пропатчить сущность")
    public Response patchEntity(String body, int id){
        return getBaseRequestSpec()
                .basePath(URLS.PATH_USERS)
                .pathParam("path", id)
                .when()
                .patch();
    }

}

