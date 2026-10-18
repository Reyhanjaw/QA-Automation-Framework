package api.config;

public final class ApiConfig {

    // Prevents object creation because this utility class only provides static members.
    private ApiConfig() {
        // Prevent creating an instance of this utility class.
    }

    // Defines the base URL used for requests to the DummyAPI service.
    // Keeping the URL in one place makes it easier to maintain the API configuration.
    public static final String BASE_URL =
            "https://dummyapi.io/data/v1";

    // Retrieves the API application ID from an environment variable.
    public static String getAppId() {

        // Reads the application ID from the environment variable.
        // This allows credentials to be configured without hardcoding them in the source code.
        String appId = System.getenv("DUMMYAPI_APP_ID");

        // Checks whether the variable is missing or contains only whitespace.
        if (appId == null || appId.isBlank()) {

            // Stops execution with a clear error message if the application ID is unavailable.
            throw new IllegalStateException(
                    "Environment variable DUMMYAPI_APP_ID is not set."
            );
        }

        // Returns the validated application ID for use in API requests.
        return appId;
    }
}