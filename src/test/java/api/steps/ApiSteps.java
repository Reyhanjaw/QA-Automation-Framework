package api.steps;

import api.clients.ApiClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiSteps {

    // Store the latest API response so it can be validated in the Then steps.
    private Response response;

    // Store the user ID so subsequent update and delete requests can target
    // the same user created or selected during the scenario.
    private String userId;

    // Store the submitted user data for later comparison with the API response.
    private String firstName;
    private String lastName;
    private String email;

    // Store the expected first name after the user update request.
    private String updatedFirstName;

    @Given("the API request is configured")
    public void theApiRequestIsConfigured() {
        // Verify that the DummyAPI App ID is configured before sending requests.
        // This helps identify configuration issues before the API test proceeds.
        assertNotNull(
                api.config.ApiConfig.getAppId(),
                "DummyAPI App ID must be configured."
        );
    }

    @When("the client requests the users endpoint")
    public void theClientRequestsTheUsersEndpoint() {
        // Send a GET request to retrieve the list of users.
        response = ApiClient.getRequestSpec()
                .when()
                .get("/user");

        // Log the HTTP status code to help with test execution and debugging.
        System.out.println("[GET USERS] Status: " + response.getStatusCode());
    }

    @Then("the API response status code should be {int}")
    public void theApiResponseStatusCodeShouldBe(int expectedStatusCode) {
        // Verify that the actual HTTP status code matches the expected value
        // provided by the Cucumber scenario.
        assertEquals(
                expectedStatusCode,
                response.getStatusCode(),
                "Unexpected API response status code."
        );
    }

    @Then("the response should contain a list of users")
    public void theResponseShouldContainAListOfUsers() {
        // Extract the user list from the "data" field in the JSON response.
        List<Map<String, Object>> users =
                response.jsonPath().getList("data");

        // Confirm that the response contains the expected data field.
        assertNotNull(users, "Response must contain a 'data' field.");

        // Confirm that at least one user is available for validation.
        assertTrue(
                !users.isEmpty(),
                "Expected at least one user in the response."
        );

        // Use the first user to verify that essential user attributes exist.
        Map<String, Object> firstUser = users.get(0);

        assertNotNull(firstUser.get("id"), "User ID must not be null.");
        assertNotNull(firstUser.get("firstName"), "First name must not be null.");
        assertNotNull(firstUser.get("lastName"), "Last name must not be null.");
    }

    @When("the client requests an existing user by ID")
    public void theClientRequestsAnExistingUserById() {
        // Retrieve the user list first so the test can use an existing user ID.
        Response usersResponse = ApiClient.getRequestSpec()
                .when()
                .get("/user");

        // Ensure the user list request succeeds before extracting the user ID.
        assertEquals(
                200,
                usersResponse.getStatusCode(),
                "Unable to retrieve users."
        );

        // Extract the ID of the first user returned by the API.
        userId = usersResponse.jsonPath().getString("data[0].id");

        // Stop the test with a clear message if no user ID is available.
        assertNotNull(userId, "User ID must not be null.");

        // Request the selected user by ID.
        response = ApiClient.getRequestSpec()
                .when()
                .get("/user/" + userId);

        // Log the request details to help verify which user was retrieved.
        System.out.println("========== GET USER BY ID ==========");
        System.out.println("[GET USER BY ID] Requested ID: " + userId);
        System.out.println("[GET USER BY ID] Status: " + response.getStatusCode());
    }

    @Then("the response user ID should match the requested ID")
    public void theResponseUserIdShouldMatchTheRequestedId() {
        // Extract the user ID returned by the single-user endpoint.
        String returnedUserId = response.jsonPath().getString("id");

        // Verify that the returned user is the same user requested.
        assertEquals(
                userId,
                returnedUserId,
                "The returned user ID does not match the requested ID."
        );

        // Log the comparison result for easier debugging.
        System.out.println("[GET USER BY ID] Returned ID: " + returnedUserId);
        System.out.println("[GET USER BY ID] ID Match: " + userId.equals(returnedUserId));
    }

    @When("the client creates a new user")
    public void theClientCreatesANewUser() {
        // Prepare the user data that will be submitted to the API.
        firstName = "Reyhan";
        lastName = "QA";

        // Generate a unique email address to reduce conflicts with existing users.
        email = "reyhan." + UUID.randomUUID() + "@example.com";

        // Build the JSON request body using the prepared user data.
        String requestBody = """
            {
                "firstName": "%s",
                "lastName": "%s",
                "email": "%s"
            }
            """.formatted(firstName, lastName, email);

        // Record the start time to measure how long the request takes.
        long startTime = System.nanoTime();

        // Send a POST request to create a new user.
        response = ApiClient.getRequestSpec()
                .body(requestBody)
                .when()
                .post("/user/create");

        // IMPORTANT:
        // Save the created user's ID immediately after the create request.
        // Update and delete steps depend on this value to target the correct user.
        // Do not move this assignment into the validation Then step.
        userId = response.jsonPath().getString("id");

        // Calculate the request duration in milliseconds.
        long durationMs =
                (System.nanoTime() - startTime) / 1_000_000;

        // Log the request details, response status, duration, and response body.
        System.out.println("========== CREATE USER ==========");
        System.out.println("[CREATE USER] Request: POST /user/create");
        System.out.println("[CREATE USER] Status: " + response.getStatusCode());
        System.out.println("[CREATE USER] Response Time: " + durationMs + " ms");
        System.out.println("[CREATE USER] Response Body: " + response.asString());
        System.out.println("=================================");
    }

    @Then("the created user should contain an ID and the submitted data")
    public void theCreatedUserShouldContainAnIdAndTheSubmittedData() {
        // Extract the ID from the create response for explicit validation.
        String createdUserId = response.jsonPath().getString("id");

        // Verify that the API returned a user ID.
        assertNotNull(createdUserId, "Created user ID must not be null.");

        // Verify that the ID was also saved for subsequent update/delete steps.
        assertNotNull(userId, "Stored user ID must not be null.");

        // Ensure that the stored ID matches the ID returned by the create request.
        assertEquals(
                createdUserId,
                userId,
                "Stored user ID does not match the created user ID."
        );

        // Verify that the API returned the same first name that was submitted.
        assertEquals(
                firstName,
                response.jsonPath().getString("firstName"),
                "First name does not match."
        );

        // Verify that the API returned the same last name that was submitted.
        assertEquals(
                lastName,
                response.jsonPath().getString("lastName"),
                "Last name does not match."
        );

        // Verify that the API returned the same email that was submitted.
        assertEquals(
                email,
                response.jsonPath().getString("email"),
                "Email does not match."
        );

        // Verify that the create request returned the expected HTTP status code.
        assertEquals(
                200,
                response.getStatusCode(),
                "Unexpected status code when creating a user."
        );

        // Log the ID of the successfully validated user.
        System.out.println("[CREATE USER] Created ID: " + createdUserId);
    }

    @When("the client updates the user's first name")
    public void theClientUpdatesTheUsersFirstName() {
        // Ensure that a user ID is available before sending the update request.
        assertNotNull(userId, "User ID must not be null before updating.");

        // Define the new first name that the API should store.
        updatedFirstName = "ReyhanUpdated";

        // Build the JSON request body containing the field to update.
        String requestBody = """
            {
                "firstName": "%s"
            }
            """.formatted(updatedFirstName);

        // Record the start time to measure the update request duration.
        long startTime = System.nanoTime();

        // Send a PUT request to update the selected user's first name.
        response = ApiClient.getRequestSpec()
                .body(requestBody)
                .when()
                .put("/user/" + userId);

        // Calculate the request duration in milliseconds.
        long durationMs =
                (System.nanoTime() - startTime) / 1_000_000;

        // Log the update request details and response for debugging.
        System.out.println("========== UPDATE USER ==========");
        System.out.println("[UPDATE USER] Request: PUT /user/" + userId);
        System.out.println("[UPDATE USER] Status: " + response.getStatusCode());
        System.out.println("[UPDATE USER] Response Time: " + durationMs + " ms");
        System.out.println("[UPDATE USER] Response Body: " + response.asString());
        System.out.println("=================================");
    }

    @Then("the updated user's first name should match the request")
    public void theUpdatedUsersFirstNameShouldMatchTheRequest() {
        // Extract the first name returned by the update response.
        String actualFirstName = response.jsonPath().getString("firstName");

        // Verify that the updated value matches the submitted value.
        assertEquals(
                updatedFirstName,
                actualFirstName,
                "Updated first name does not match the request."
        );

        // Log the expected and actual values to make failures easier to diagnose.
        System.out.println("[UPDATE USER] User ID: " + userId);
        System.out.println("[UPDATE USER] Expected First Name: " + updatedFirstName);
        System.out.println("[UPDATE USER] Actual First Name: " + actualFirstName);
        System.out.println("[UPDATE USER] Name Match: " +
                updatedFirstName.equals(actualFirstName));
    }

    @When("the client deletes the created user")
    public void theClientDeletesTheCreatedUser() {
        // Ensure that a user ID is available before sending the delete request.
        assertNotNull(userId, "User ID must not be null before deleting.");

        // Record the start time to measure the delete request duration.
        long startTime = System.nanoTime();

        // Send a DELETE request for the user identified by the stored ID.
        response = ApiClient.getRequestSpec()
                .when()
                .delete("/user/" + userId);

        // Calculate the request duration in milliseconds.
        long durationMs =
                (System.nanoTime() - startTime) / 1_000_000;

        // Extract the deleted user's ID from the API response.
        String deletedUserId = response.jsonPath().getString("id");

        // Log the delete request details and response for debugging.
        System.out.println("========== DELETE USER ==========");
        System.out.println("[DELETE USER] Request: DELETE /user/" + userId);
        System.out.println("[DELETE USER] Status: " + response.getStatusCode());
        System.out.println("[DELETE USER] Response Time: " + durationMs + " ms");
        System.out.println("[DELETE USER] Response Body: " + response.asString());

        // Verify that the delete request returned the expected HTTP status code.
        assertEquals(
                200,
                response.getStatusCode(),
                "Unexpected status code when deleting user."
        );

        // Verify that the API response refers to the same user targeted for deletion.
        assertEquals(
                userId,
                deletedUserId,
                "Deleted user ID does not match the requested user ID."
        );

        // Log the ID comparison result for easier verification.
        System.out.println("[DELETE USER] Requested ID: " + userId);
        System.out.println("[DELETE USER] Returned ID: " + deletedUserId);
        System.out.println("[DELETE USER] ID Match: " + userId.equals(deletedUserId));
        System.out.println("=================================");
    }

    @When("the client requests the tags endpoint")
    public void theClientRequestsTheTagsEndpoint() {
        // Record the start time before requesting the tags endpoint.
        long startTime = System.nanoTime();

        // Send a GET request to retrieve the available tags.
        response = ApiClient.getRequestSpec()
                .when()
                .get("/tag");

        // Calculate the request duration in milliseconds.
        long durationMs =
                (System.nanoTime() - startTime) / 1_000_000;

        // Log the request details and response body for debugging.
        System.out.println("========== GET TAGS ==========");
        System.out.println("[GET TAGS] Request: GET /tag");
        System.out.println("[GET TAGS] Status: " + response.getStatusCode());
        System.out.println("[GET TAGS] Response Time: " + durationMs + " ms");
        System.out.println("[GET TAGS] Response Body: " + response.asString());
    }

    @Then("the response should contain a list of tags")
    public void theResponseShouldContainAListOfTags() {
        // Extract the tags list from the "data" field in the JSON response.
        List<?> tags = response.jsonPath().getList("data");

        // Verify that the response contains a tags list.
        assertNotNull(tags, "Tags list must not be null.");

        // Verify that the tags list contains at least one item.
        assertTrue(!tags.isEmpty(), "Tags list must not be empty.");

        // Log the number of retrieved tags and the validation result.
        System.out.println("[GET TAGS] Total Tags Retrieved: " + tags.size());
        System.out.println("[GET TAGS] Tags: " + tags);
        System.out.println("[GET TAGS] List Validation: PASSED");
        System.out.println("==============================");
    }
}