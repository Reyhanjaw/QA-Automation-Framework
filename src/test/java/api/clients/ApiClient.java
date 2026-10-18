package api.clients;

import api.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public final class ApiClient {

    // Prevents object creation because this utility class only provides static methods.
    private ApiClient() {
        // Prevent creating an instance of this utility class.
    }

    // Creates and returns a reusable request specification for API requests.
    public static RequestSpecification getRequestSpec() {
        return RestAssured.given()

                // Sets the base URL used for API requests.
                .baseUri(ApiConfig.BASE_URL)

                // Adds the application ID required by the API as a request header.
                .header("app-id", ApiConfig.getAppId())

                // Specifies that the request body is formatted as JSON.
                .contentType("application/json")

                // Indicates that the client expects JSON responses from the API.
                .accept("application/json");
    }
}