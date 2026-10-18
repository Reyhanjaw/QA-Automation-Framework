package runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

// Marks this class as a JUnit Platform Suite.
// It allows Cucumber scenarios to run through the JUnit Platform.
@Suite

// Specifies Cucumber as the test engine used by this runner.
@IncludeEngines("cucumber")

// Selects the package containing the Web UI Cucumber feature files.
@SelectPackages("features.web")

// Specifies the package containing the Java step definitions.
// Cucumber uses these step definitions to execute Gherkin steps.
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "web"
)

// Configures the output format and locations of Cucumber test reports.
// "pretty" displays readable test execution details in the console.
// "html" generates an HTML report for viewing in a browser.
// "json" generates a JSON report for structured test results.
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty, html:build/reports/cucumber-reports/web/report.html, json:build/reports/cucumber-reports/web/report.json"
)

// Disables parallel execution so scenarios run sequentially.
// This can help avoid conflicts when scenarios share browser state or resources.
@ConfigurationParameter(
        key = "cucumber.execution.parallel.enabled",
        value = "false"
)
public class WebTestRunner {
}