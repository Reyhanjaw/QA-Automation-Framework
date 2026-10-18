package web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import org.openqa.selenium.Alert;

public class LoginPage {

    // Stores the WebDriver instance used to interact with the browser.
    private final WebDriver driver;

    // Provides explicit waits to synchronize browser interactions with page elements.
    private final WebDriverWait wait;

    // Locators: identify the web elements used on the login page.
    // Uses the element ID to locate the login menu in the navigation bar.
    private final By loginMenu = By.id("login2");

    // Identifies the username input field in the login modal.
    private final By usernameField = By.id("loginusername");

    // Identifies the password input field in the login modal.
    private final By passwordField = By.id("loginpassword");

    // Identifies the element displaying the welcome message after a successful login.
    private final By welcomeMessage = By.id("nameofuser");

    // Identifies the Log in button inside the login modal using XPath.
    private final By loginButton =
            By.xpath("//div[@id='logInModal']//button[text()='Log in']");

    // Initializes the page object with the active WebDriver instance.
    public LoginPage(WebDriver driver) {
        this.driver = driver;

        // Sets the default explicit wait timeout to 10 seconds.
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Opens the login modal and waits until the username field becomes visible.
    public void openLoginModal() {
        // Waits until the login menu is clickable before opening the modal.
        wait.until(ExpectedConditions.elementToBeClickable(loginMenu)).click();

        // Confirms that the username field is visible before continuing.
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
    }

    // Enters the provided username into the username field.
    public void enterUsername(String username) {
        // Waits for the field to become visible before typing.
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField))
                .sendKeys(username);
    }

    // Enters the provided password into the password field.
    public void enterPassword(String password) {
        // Waits for the field to become visible before typing.
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField))
                .sendKeys(password);
    }

    // Clicks the login button after it becomes clickable.
    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    // Checks whether the page displays the expected welcome message for the username.
    // Returns true when the message matches; otherwise, returns false if it times out.
    public boolean isLoggedInAs(String username) {
        try {
            // Waits until the welcome message is visible and retrieves its text.
            String actualText = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(welcomeMessage)
            ).getText();

            // Compares the actual message with the expected message.
            return actualText.equals("Welcome " + username);

        } catch (org.openqa.selenium.TimeoutException e) {
            // Returns false if the welcome message does not appear within the timeout.
            return false;
        }
    }

    // Checks whether a browser alert appears after a login attempt.
    // Returns the alert text if present, or null if no alert appears within 2 seconds.
    public String acceptLoginAlertIfPresent() {
        try {
            // Waits up to 2 seconds for a browser alert to appear.
            Alert alert = new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.alertIsPresent());

            // Retrieves the alert message before dismissing the alert.
            String alertText = alert.getText();

            // Accepts the alert to close it and allow the test to continue.
            alert.accept();

            // Returns the captured alert message for validation.
            return alertText;

        } catch (org.openqa.selenium.TimeoutException e) {
            // Returns null when no alert appears within the specified timeout.
            return null;
        }
    }

    // Provides the login error message by checking for and accepting a browser alert.
    public String getLoginErrorMessage() {
        return acceptLoginAlertIfPresent();
    }
}