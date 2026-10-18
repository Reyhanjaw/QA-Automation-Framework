
package api.config;

public final class ApiConfig {

    private ApiConfig() {
        // Prevent creating an instance of this utility class.
    }

    public static final String BASE_URL =
            "https://dummyapi.io/data/v1";

    public static String getAppId() {
        String appId = System.getenv("DUMMYAPI_APP_ID");

        if (appId == null || appId.isBlank()) {
            throw new IllegalStateException(
                    "Environment variable DUMMYAPI_APP_ID is not set."
            );
        }

        return appId;
    }
}
