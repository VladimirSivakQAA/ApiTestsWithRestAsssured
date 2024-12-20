package org.assured.project.api.assertions;

import io.restassured.response.Response;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.SoftAssertions;

public class AssertResponse extends AbstractAssert<AssertResponse, Response> {
    protected AssertResponse(Response response) {
        super(response, AssertResponse.class);
    }
    private static final SoftAssertions softly = new SoftAssertions();

    public AssertResponse checkStatusCode(int expectedCode) {
        softly.assertThat(actual.statusCode())
                .as("Check status code")
                .isEqualTo(expectedCode);
        return this;
    }

    public AssertResponse checkStringByPath(String path, String value) {
        softly.assertThat(actual.jsonPath().getString(path))
                .as("Проверить наличие строки %s по пути %s".formatted(value, path))
                .isEqualTo(value);
        return this;
    }

    public void assertAll(){
        softly.assertAll();
    }

    public static AssertResponse assertThat(Response actual){
        return new AssertResponse(actual);
    }
}
