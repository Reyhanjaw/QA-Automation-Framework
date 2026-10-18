package runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;

// Marks this class as a JUnit Platform Suite.
// This allows the API Cucumber scenarios to run through the JUnit Platform.
@Suite

// Specifies Cucumber as the test engine used by this runner.
@IncludeEngines("cucumber")

// Selects the package containing the API Cucumber feature files.
@SelectPackages("features.api")

// Specifies the package containing the Java step definitions.
// Cucumber uses these step definitions to execute the steps in feature files.
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "api")

// Filters the scenarios so that only those tagged with @api are executed.
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@api")

// Configures the Cucumber output plugins and report locations.
// "pretty" displays readable execution details in the console.
// "html" generates an HTML report.
// "json" generates a JSON report for structured test results.
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty, html:build/reports/cucumber-reports/api/report.html, json:build/reports/cucumber-reports/api/report.json"
)

// Disables parallel scenario execution so tests run sequentially.
@ConfigurationParameter(
        key = "cucumber.execution.parallel.enabled",
        value = "false"
)
public class ApiTestRunner {
}