package web.support;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class Hooks {

    // Stores the WebDriver instance separately for each thread.
    // ThreadLocal helps prevent different threads from sharing the same driver.
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Runs before each Cucumber scenario.
    // Initializes the browser and stores its WebDriver instance.
    @Before
    public void setUp() {
        driver.set(DriverFactory.createDriver());
    }

    // Runs after each Cucumber scenario, including scenarios that fail.
    // Closes the browser and releases the stored WebDriver reference.
    @After
    public void tearDown() {

        // Retrieves the WebDriver instance associated with the current thread.
        WebDriver currentDriver = driver.get();

        // Checks whether a WebDriver instance was successfully created.
        if (currentDriver != null) {
            try {
                // Closes the browser and ends the WebDriver session.
                currentDriver.quit();
            } finally {
                // Removes the WebDriver reference from the current thread.
                // This helps prevent stale references and memory leaks.
                driver.remove();
            }
        }
    }

    // Provides access to the WebDriver instance for the current thread.
    // Other classes can use this method to interact with the browser.
    public static WebDriver getDriver() {
        return driver.get();
    }
}