
package runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("features.web")
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "web"
)
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty, html:build/reports/web-tests/cucumber/report.html, json:build/reports/web-tests/cucumber/report.json"
)
@ConfigurationParameter(key = "cucumber.execution.parallel.enabled", value = "false")
public class WebTestRunner {
}
