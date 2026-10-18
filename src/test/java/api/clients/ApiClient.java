
package api.clients;

import api.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public final class ApiClient {

    private ApiClient() {
        // Prevent creating an instance of this utility class.
    }

    public static RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .header("app-id", ApiConfig.getAppId())
                .contentType("application/json")
                .accept("application/json");
    }
}
