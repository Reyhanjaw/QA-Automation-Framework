
package web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Alert;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By loginMenu = By.id("login2");
    private final By usernameField = By.id("loginusername");
    private final By passwordField = By.id("loginpassword");
    private final By welcomeMessage = By.id("nameofuser");
    private final By loginButton =
            By.xpath("//div[@id='logInModal']//button[text()='Log in']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openLoginModal() {
        wait.until(ExpectedConditions.elementToBeClickable(loginMenu)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
    }

    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField))
                .sendKeys(username);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField))
                .sendKeys(password);
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public boolean isLoggedInAs(String username) {
        try {
            String actualText = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(welcomeMessage)
            ).getText();

            return actualText.equals("Welcome " + username);
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public String acceptLoginAlertIfPresent() {
        try {
            Alert alert = new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.alertIsPresent());

            String alertText = alert.getText();
            alert.accept();
            return alertText;

        } catch (org.openqa.selenium.TimeoutException e) {
            return null;
        }
    }

    public String getLoginErrorMessage() {
        return acceptLoginAlertIfPresent();
    }
}
