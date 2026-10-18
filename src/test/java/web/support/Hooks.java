
package web.support;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class Hooks {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @Before
    public void setUp() {
        driver.set(DriverFactory.createDriver());
    }

    @After
    public void tearDown() {
        WebDriver currentDriver = driver.get();

        if (currentDriver != null) {
            try {
                currentDriver.quit();
            } finally {
                driver.remove();
            }
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }
}
