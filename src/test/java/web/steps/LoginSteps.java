
package web.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import web.pages.LoginPage;
import web.support.Hooks;
import org.junit.jupiter.api.Assertions;

public class LoginSteps {

    private LoginPage loginPage;

    @Given("the user opens the Demoblaze website")
    public void theUserOpensTheDemoblazeWebsite() {
        Hooks.getDriver().get("https://www.demoblaze.com/");

        loginPage = new LoginPage(Hooks.getDriver());
    }

    @When("the user logs in with valid credentials")
    public void theUserLogsInWithValidCredentials() {
        String username = System.getenv("DEMO_USERNAME");
        String password = System.getenv("DEMO_PASSWORD");

        if (username == null || password == null) {
            throw new IllegalStateException(
                    "Set DEMO_USERNAME and DEMO_PASSWORD environment variables."
            );
        }

        loginPage.openLoginModal();
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
    }

    @Then("the user should be logged in successfully")
    public void theUserShouldBeLoggedInSuccessfully() {
        String username = System.getenv("DEMO_USERNAME");

        if (!loginPage.isLoggedInAs(username)) {
            throw new AssertionError("Login was not successful.");
        }
    }

    @When("the user logs in with invalid credentials")
    public void theUserLogsInWithInvalidCredentials() {
        loginPage.openLoginModal();
        loginPage.enterUsername("invalid_user_qa");
        loginPage.enterPassword("wrong_password_qa");
        loginPage.clickLoginButton();
    }

    @Then("the login error message should be displayed")
    public void theLoginErrorMessageShouldBeDisplayed() {
        String errorMessage = loginPage.getLoginErrorMessage();

        Assertions.assertNotNull(
                errorMessage,
                "Expected a login error alert, but no alert was displayed."
        );
    }
}
