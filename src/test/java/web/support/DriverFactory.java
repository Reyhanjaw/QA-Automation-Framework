package web.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    // Creates and configures a Chrome WebDriver instance.
    // This method is called by Hooks before each Cucumber scenario.
    public static WebDriver createDriver() {

        // Defines the configuration options for the Chrome browser.
        ChromeOptions options = new ChromeOptions();

        // Sets the browser window size to 1920x1080.
        // A consistent viewport helps make UI test results more predictable.
        options.addArguments("--window-size=1920,1080");

        // Checks whether the "headless" Java system property is set to true.
        // Headless mode allows browser tests to run without displaying a browser window.
        if (Boolean.getBoolean("headless")) {

            // Runs Chrome in headless mode using the newer headless implementation.
            options.addArguments("--headless=new");

            // Disables Chrome's sandbox, which may be required in some CI environments.
            options.addArguments("--no-sandbox");

            // Reduces shared-memory usage to help prevent browser crashes in containers.
            options.addArguments("--disable-dev-shm-usage");
        }

        // Starts Chrome with the configured options and returns the WebDriver instance.
        return new ChromeDriver(options);
    }
}