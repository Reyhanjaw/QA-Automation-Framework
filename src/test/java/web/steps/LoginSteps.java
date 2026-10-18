package web.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import web.pages.LoginPage;
import web.support.Hooks;
import org.junit.jupiter.api.Assertions;

public class LoginSteps {

    // Stores the LoginPage object used to interact with the login page.
    private LoginPage loginPage;

    // Runs before the login scenario steps that use this Given annotation.
    // Opens the Demoblaze website and initializes the LoginPage object.
    @Given("the user opens the Demoblaze website")
    public void theUserOpensTheDemoblazeWebsite() {

        // Retrieves the shared WebDriver instance and navigates to Demoblaze.
        Hooks.getDriver().get("https://www.demoblaze.com/");

        // Creates the page object responsible for login page interactions.
        loginPage = new LoginPage(Hooks.getDriver());
    }

    // Executes the login process using valid credentials from environment variables.
    @When("the user logs in with valid credentials")
    public void theUserLogsInWithValidCredentials() {

        // Reads the demo username and password from environment variables.
        // This avoids hardcoding credentials directly in the source code.
        String username = System.getenv("DEMO_USERNAME");
        String password = System.getenv("DEMO_PASSWORD");

        // Stops test execution if either required credential is missing.
        if (username == null || password == null) {
            throw new IllegalStateException(
                    "Set DEMO_USERNAME and DEMO_PASSWORD environment variables."
            );
        }

        // Performs the login actions through the LoginPage methods.
        loginPage.openLoginModal();
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
    }

    // Verifies that the user has successfully logged in.
    @Then("the user should be logged in successfully")
    public void theUserShouldBeLoggedInSuccessfully() {

        // Retrieves the username used to identify the expected welcome message.
        String username = System.getenv("DEMO_USERNAME");

        // Fails the scenario if the expected welcome message is not displayed.
        if (!loginPage.isLoggedInAs(username)) {
            throw new AssertionError("Login was not successful.");
        }
    }

    // Attempts to log in using intentionally invalid credentials.
    @When("the user logs in with invalid credentials")
    public void theUserLogsInWithInvalidCredentials() {

        // Opens the login modal before entering the test credentials.
        loginPage.openLoginModal();

        // Uses invalid test data to verify the application's login validation.
        loginPage.enterUsername("invalid_user_qa");
        loginPage.enterPassword("wrong_password_qa");

        // Submits the login form to trigger the application's response.
        loginPage.clickLoginButton();
    }

    // Verifies that an error alert appears after an unsuccessful login attempt.
    @Then("the login error message should be displayed")
    public void theLoginErrorMessageShouldBeDisplayed() {

        // Retrieves the alert message and accepts the alert if it is present.
        String errorMessage = loginPage.getLoginErrorMessage();

        // Fails the scenario if no login error alert was detected.
        Assertions.assertNotNull(
                errorMessage,
                "Expected a login error alert, but no alert was displayed."
        );
    }
}