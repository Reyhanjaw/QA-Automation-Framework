
package api.steps;

import api.clients.ApiClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiSteps {

    private Response response;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String updatedFirstName;

    @Given("the API request is configured")
    public void theApiRequestIsConfigured() {
        // Verify that the App ID is available.
        assertNotNull(
                api.config.ApiConfig.getAppId(),
                "DummyAPI App ID must be configured."
        );
    }

    @When("the client requests the users endpoint")
    public void theClientRequestsTheUsersEndpoint() {
        response = ApiClient.getRequestSpec()
                .when()
                .get("/user");

        System.out.println("[GET USERS] Status: " + response.getStatusCode());
    }

    @Then("the API response status code should be {int}")
    public void theApiResponseStatusCodeShouldBe(int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                response.getStatusCode(),
                "Unexpected API response status code."
        );
    }

    @Then("the response should contain a list of users")
    public void theResponseShouldContainAListOfUsers() {
        java.util.List<java.util.Map<String, Object>> users =
                response.jsonPath().getList("data");

        assertNotNull(users, "Response must contain a 'data' field.");

        assertTrue(
                !users.isEmpty(),
                "Expected at least one user in the response."
        );

        java.util.Map<String, Object> firstUser = users.get(0);

        assertNotNull(firstUser.get("id"), "User ID must not be null.");
        assertNotNull(firstUser.get("firstName"), "First name must not be null.");
        assertNotNull(firstUser.get("lastName"), "Last name must not be null.");
    }


    @When("the client requests an existing user by ID")
    public void theClientRequestsAnExistingUserById() {
        // Get the list of users first.
        Response usersResponse = ApiClient.getRequestSpec()
                .when()
                .get("/user");

        assertEquals(
                200,
                usersResponse.getStatusCode(),
                "Unable to retrieve users."
        );

        // Get an existing user ID from the response.
        userId = usersResponse.jsonPath().getString("data[0].id");

        assertNotNull(userId, "User ID must not be null.");

        // Request the selected user by ID.
        response = ApiClient.getRequestSpec()
                .when()
                .get("/user/" + userId);

        System.out.println("========== GET USER BY ID ==========");
        System.out.println("[GET USER BY ID] Requested ID: " + userId);
        System.out.println("[GET USER BY ID] Status: " + response.getStatusCode());
    }

    @Then("the response user ID should match the requested ID")
    public void theResponseUserIdShouldMatchTheRequestedId() {
        String returnedUserId = response.jsonPath().getString("id");

        assertEquals(
                userId,
                returnedUserId,
                "The returned user ID does not match the requested ID."
        );
        System.out.println("[GET USER BY ID] Returned ID: " + returnedUserId);
        System.out.println("[GET USER BY ID] ID Match: " + userId.equals(returnedUserId));
    }

    @When("the client creates a new user")
    public void theClientCreatesANewUser() {
        firstName = "Reyhan";
        lastName = "QA";
        email = "reyhan." + UUID.randomUUID() + "@example.com";

        String requestBody = """
            {
                "firstName": "%s",
                "lastName": "%s",
                "email": "%s"
            }
            """.formatted(firstName, lastName, email);

        long startTime = System.nanoTime();

        response = ApiClient.getRequestSpec()
                .body(requestBody)
                .when()
                .post("/user/create");

        userId = response.jsonPath().getString("id");

        long durationMs =
                (System.nanoTime() - startTime) / 1_000_000;

        System.out.println("========== CREATE USER ==========");
        System.out.println("[CREATE USER] Request: POST /user/create");
        System.out.println("[CREATE USER] Status: " + response.getStatusCode());
        System.out.println("[CREATE USER] Response Time: " + durationMs + " ms");
        System.out.println("[CREATE USER] Response Body: " + response.asString());
        System.out.println("=================================");
    }

    @Then("the created user should contain an ID and the submitted data")
    public void theCreatedUserShouldContainAnIdAndTheSubmittedData() {
        String createdUserId = response.jsonPath().getString("id");

        assertNotNull(createdUserId, "Created user ID must not be null.");

        assertEquals(
                firstName,
                response.jsonPath().getString("firstName"),
                "First name does not match."
        );

        assertEquals(
                lastName,
                response.jsonPath().getString("lastName"),
                "Last name does not match."
        );

        assertEquals(
                email,
                response.jsonPath().getString("email"),
                "Email does not match."
        );

        assertEquals(
                200,
                response.getStatusCode(),
                "Unexpected status code when creating a user."
        );

        System.out.println("[CREATE USER] Created ID: " + createdUserId);;
    }

    @When("the client updates the user's first name")
    public void theClientUpdatesTheUsersFirstName() {
        assertNotNull(userId, "User ID must not be null before updating.");

        updatedFirstName = "ReyhanUpdated";

        String requestBody = """
            {
                "firstName": "%s"
            }
            """.formatted(updatedFirstName);

        long startTime = System.nanoTime();

        response = ApiClient.getRequestSpec()
                .body(requestBody)
                .when()
                .put("/user/" + userId);

        long durationMs =
                (System.nanoTime() - startTime) / 1_000_000;

        System.out.println("========== UPDATE USER ==========");
        System.out.println("[UPDATE USER] Request: PUT /user/" + userId);
        System.out.println("[UPDATE USER] Status: " + response.getStatusCode());
        System.out.println("[UPDATE USER] Response Time: " + durationMs + " ms");
        System.out.println("[UPDATE USER] Response Body: " + response.asString());
        System.out.println("=================================");
    }

    @Then("the updated user's first name should match the request")
    public void theUpdatedUsersFirstNameShouldMatchTheRequest() {
        String actualFirstName = response.jsonPath().getString("firstName");

        assertEquals(
                updatedFirstName,
                actualFirstName,
                "Updated first name does not match the request."
        );

        System.out.println("[UPDATE USER] User ID: " + userId);
        System.out.println("[UPDATE USER] Expected First Name: " + updatedFirstName);
        System.out.println("[UPDATE USER] Actual First Name: " + actualFirstName);
        System.out.println("[UPDATE USER] Name Match: " +
                updatedFirstName.equals(actualFirstName));
    }

    @When("the client deletes the created user")
    public void theClientDeletesTheCreatedUser() {
        assertNotNull(userId, "User ID must not be null before deleting.");

        long startTime = System.nanoTime();

        response = ApiClient.getRequestSpec()
                .when()
                .delete("/user/" + userId);

        long durationMs =
                (System.nanoTime() - startTime) / 1_000_000;

        String deletedUserId = response.jsonPath().getString("id");

        System.out.println("========== DELETE USER ==========");
        System.out.println("[DELETE USER] Request: DELETE /user/" + userId);
        System.out.println("[DELETE USER] Status: " + response.getStatusCode());
        System.out.println("[DELETE USER] Response Time: " + durationMs + " ms");
        System.out.println("[DELETE USER] Response Body: " + response.asString());

        assertEquals(
                200,
                response.getStatusCode(),
                "Unexpected status code when deleting user."
        );

        assertEquals(
                userId,
                deletedUserId,
                "Deleted user ID does not match the requested user ID."
        );

        System.out.println("[DELETE USER] Requested ID: " + userId);
        System.out.println("[DELETE USER] Returned ID: " + deletedUserId);
        System.out.println("[DELETE USER] ID Match: " + userId.equals(deletedUserId));
        System.out.println("=================================");
    }

    @When("the client requests the tags endpoint")
    public void theClientRequestsTheTagsEndpoint() {
        long startTime = System.nanoTime();

        response = ApiClient.getRequestSpec()
                .when()
                .get("/tag");

        long durationMs =
                (System.nanoTime() - startTime) / 1_000_000;

        System.out.println("========== GET TAGS ==========");
        System.out.println("[GET TAGS] Request: GET /tag");
        System.out.println("[GET TAGS] Status: " + response.getStatusCode());
        System.out.println("[GET TAGS] Response Time: " + durationMs + " ms");
        System.out.println("[GET TAGS] Response Body: " + response.asString());
    }

    @Then("the response should contain a list of tags")
    public void theResponseShouldContainAListOfTags() {
        java.util.List<?> tags = response.jsonPath().getList("data");

        assertNotNull(tags, "Tags list must not be null.");
        assertTrue(!tags.isEmpty(), "Tags list must not be empty.");

        System.out.println("[GET TAGS] Total Tags Retrieved: " + tags.size());
        System.out.println("[GET TAGS] Tags: " + tags);
        System.out.println("[GET TAGS] List Validation: PASSED");
        System.out.println("==============================");
    }

}
