[README.md](https://github.com/user-attachments/files/33632885/README.md)
# QA Automation Framework

A Java-based test automation framework for practicing and demonstrating
**API testing**, **Web UI testing**, and **continuous integration with
GitHub Actions**.

The project uses Cucumber feature files to describe test scenarios in
readable Gherkin, Java step definitions to execute those scenarios, and
Gradle to run the test suites.

## Table of Contents

-   [Project Overview](#project-overview)
-   [Features](#features)
-   [Technology Stack](#technology-stack)
-   [Project Structure](#project-structure)
-   [Prerequisites](#prerequisites)
-   [Getting Started](#getting-started)
-   [Configuration and Secrets](#configuration-and-secrets)
-   [Running the Tests](#running-the-tests)
-   [Test Coverage](#test-coverage)
-   [Test Reports](#test-reports)
-   [GitHub Actions CI](#github-actions-ci)
-   [How the Framework Works](#how-the-framework-works)
-   [Troubleshooting](#troubleshooting)
-   [Security Notes](#security-notes)

## Project Overview

This repository contains automated tests for two application layers:

1.  **API testing** against DummyAPI.
2.  **Web UI testing** against the DemoBlaze website.

The framework is designed to make tests easier to read, maintain, and
execute locally or in a CI environment.

### Project Goals

-   Practice writing automated API and browser-based tests.
-   Separate test scenarios, step definitions, API clients,
    configuration, and browser support code.
-   Execute API and Web UI suites independently.
-   Generate test reports for reviewing execution results.
-   Run both test suites automatically through GitHub Actions.

## Features

### API Automation

The API suite uses REST Assured to send HTTP requests and validate
responses. The current step definitions cover operations such as:

-   Retrieving a list of users.
-   Checking that user data contains expected fields.
-   Retrieving an existing user by ID.
-   Creating a user and validating the returned ID and submitted data.
-   Updating a user's first name and checking the returned value.
-   Deleting a user and checking the response.
-   Retrieving tags and verifying that the response contains a list.

The API request setup is centralized in `api.clients.ApiClient`, while
API configuration is handled by `api.config.ApiConfig`.

### Web UI Automation

The Web UI suite uses Selenium WebDriver and Cucumber. The current
scenario coverage includes logging in to DemoBlaze with valid
credentials and verifying that the login succeeds.

Browser setup and cleanup are handled by the Web support classes, while
page interactions and scenario steps are separated into page objects and
step definitions.

### Continuous Integration

GitHub Actions runs the API and Web UI tests in separate jobs. This
separation makes it easier to identify which suite failed. The workflow
also uploads test reports as build artifacts so they can be inspected
after a run.

## Technology Stack

  -----------------------------------------------------------------------
  Technology                          Purpose
  ----------------------------------- -----------------------------------
  Java 25                             Programming language and runtime
                                      used by the project

  Gradle Wrapper 9.6                  Build automation and test execution

  Cucumber                            Describes scenarios in Gherkin and
                                      connects them to Java step
                                      definitions

  JUnit Platform                      Discovers and launches the Cucumber
                                      test runners

  REST Assured                        Sends HTTP requests and validates
                                      API responses

  Selenium WebDriver                  Automates browser interactions for
                                      Web UI tests

  Git and GitHub                      Source control and repository
                                      hosting

  GitHub Actions                      Runs automated tests in CI

  DummyAPI                            API test target

  DemoBlaze                           Web UI test target
  -----------------------------------------------------------------------

Dependency versions are defined in `build.gradle`. Use the Gradle
Wrapper scripts included in the repository so the project uses its
configured Gradle version.

## Project Structure

The following is a guide to the main directories and files. Some
generated directories, such as `build/`, appear only after a build or
test run.

``` text
qa-automation-framework/
├── .github/
│   └── workflows/
│       └── qa-automation.yml       # GitHub Actions workflow
├── gradle/
│   └── wrapper/                    # Gradle Wrapper configuration
├── src/
│   └── test/
│       ├── java/
│       │   ├── api/
│       │   │   ├── clients/
│       │   │   │   └── ApiClient.java
│       │   │   ├── config/
│       │   │   │   └── ApiConfig.java
│       │   │   └── steps/
│       │   │       └── ApiSteps.java
│       │   ├── runners/
│       │   │   ├── ApiTestRunner.java
│       │   │   └── WebTestRunner.java
│       │   └── web/
│       │       ├── pages/
│       │       │   └── LoginPage.java
│       │       ├── steps/
│       │       │   └── LoginSteps.java
│       │       └── support/
│       │           ├── DriverFactory.java
│       │           └── Hooks.java
│       └── resources/
│           └── features/
│               ├── api/
│               │   └── users.feature
│               └── web/
│                   └── login.feature
├── build.gradle                    # Plugins, dependencies, and test tasks
├── gradlew                         # Gradle Wrapper for Unix-like systems
├── gradlew.bat                     # Gradle Wrapper for Windows
├── settings.gradle                 # Gradle project settings
└── README.md
```

> This tree highlights the main files discussed in the project. If your
> local repository contains additional feature files or support classes,
> keep them in their existing locations.

### Main Components

  -----------------------------------------------------------------------
  Component                           Responsibility
  ----------------------------------- -----------------------------------
  `ApiClient.java`                    Provides the shared REST Assured
                                      request specification

  `ApiConfig.java`                    Reads API configuration, including
                                      the DummyAPI App ID

  `ApiSteps.java`                     Implements API Cucumber steps and
                                      response assertions

  `ApiTestRunner.java`                Launches the API Cucumber suite

  `LoginPage.java`                    Encapsulates page elements and
                                      login interactions

  `LoginSteps.java`                   Connects Web UI feature steps to
                                      browser actions

  `DriverFactory.java`                Creates and configures the
                                      WebDriver

  `Hooks.java`                        Handles browser setup and cleanup
                                      around scenarios

  `WebTestRunner.java`                Launches the Web UI Cucumber suite

  `qa-automation.yml`                 Defines API and Web UI jobs in
                                      GitHub Actions
  -----------------------------------------------------------------------

## Prerequisites

Install or prepare the following before running the project:

1.  **JDK 25** --- ensure Java is available in your terminal.
2.  **Git** --- required to clone the repository and manage changes.
3.  **IntelliJ IDEA** or another Java IDE --- recommended for
    development.
4.  **Internet access** --- required to reach DummyAPI, DemoBlaze, and
    download Gradle dependencies.
5.  **DummyAPI App ID** --- required for authenticated API requests.
6.  **DemoBlaze test credentials** --- required for the valid-login UI
    scenario.

Verify the Java installation:

``` bash
java -version
```

The output should identify Java 25. You can also check that the project
wrapper is available:

**Windows PowerShell:**

``` powershell
.\gradlew.bat --version
```

**macOS/Linux:**

``` bash
./gradlew --version
```

You do not need to install Gradle globally when using the included
Wrapper.

## Getting Started

### 1. Clone the repository

``` bash
git clone https://github.com/Reyhanjaw/QA-Automation-Framework.git
cd QA-Automation-Framework
```

### 2. Open the project

Open the cloned folder in IntelliJ IDEA and allow Gradle to import the
project and resolve its dependencies.

### 3. Configure the required values

Set up the DummyAPI App ID and DemoBlaze credentials as described in
[Configuration and Secrets](#configuration-and-secrets). Do not commit
real credentials to Git.

### 4. Run the relevant test suite

On Windows, use the Gradle Wrapper commands in [Running the
Tests](#running-the-tests). On macOS/Linux, replace `gradlew.bat` with
`./gradlew`.

## Configuration and Secrets

The framework uses configuration values for API access and UI login. The
GitHub Actions workflow expects these repository secrets:

  Secret name         Purpose
  ------------------- -----------------------------------------------
  `DUMMYAPI_APP_ID`   App ID used when sending requests to DummyAPI
  `DEMO_USERNAME`     Username for the DemoBlaze login scenario
  `DEMO_PASSWORD`     Password for the DemoBlaze login scenario

### Configure GitHub Actions Secrets

1.  Open the GitHub repository.
2.  Go to **Settings**.
3.  Open **Secrets and variables** → **Actions**.
4.  Select **New repository secret**.
5.  Add each secret using the exact name shown above and its correct
    value.

GitHub Actions makes repository secrets available to workflows that
reference them. Confirm that the variable names used in the workflow and
Java configuration match exactly.

### Local Configuration

For local execution, provide the values through the configuration
mechanism implemented in `ApiConfig.java`, the Web UI
support/configuration code, and the Gradle setup. Check those files
before running the tests to confirm how each value is read.

Do not paste actual credentials into this README, feature files, Java
source, or committed configuration files. Do not assume that GitHub
repository secrets automatically become available in a local IntelliJ
run; local execution needs its own configuration.

## Running the Tests

Run commands from the repository root directory.

### Run API tests

**Windows PowerShell:**

``` powershell
.\gradlew.bat apiTest
```

**macOS/Linux:**

``` bash
./gradlew apiTest
```

This runs the API suite through `ApiTestRunner`.

### Run Web UI tests

**Windows PowerShell:**

``` powershell
.\gradlew.bat webTest
```

**macOS/Linux:**

``` bash
./gradlew webTest
```

This runs the Web UI suite through `WebTestRunner`. The browser
configuration used by the project determines whether the browser runs
visibly or in headless mode. The GitHub Actions workflow is configured
for headless Web UI execution.

### Run both test suites

``` powershell
.\gradlew.bat apiTest webTest
```

On macOS/Linux:

``` bash
./gradlew apiTest webTest
```

### Clean and rerun

If you need a fresh build output, run:

``` powershell
.\gradlew.bat clean apiTest webTest
```

`clean` removes generated build output before the test tasks run. It
does not delete your source code.

### View available Gradle tasks

``` powershell
.\gradlew.bat tasks
```

If a command fails, read the first meaningful error in the terminal
output before changing configuration. A failure can come from missing
secrets, an unavailable external service, an assertion mismatch, or a
browser setup issue.

## Test Coverage

The scenarios are written in `.feature` files using Gherkin. Each
scenario step is linked to a Java method through Cucumber annotations.

### API scenario coverage

  -----------------------------------------------------------------------
  Area                                What is checked
  ----------------------------------- -----------------------------------
  User collection                     HTTP status and presence of a
                                      non-empty user list

  User lookup                         A selected user's returned ID
                                      matches the requested ID

  Create user                         Response includes an ID and matches
                                      the submitted first name, last
                                      name, and email

  Update user                         Returned first name matches the
                                      requested updated value

  Delete user                         Expected status code and matching
                                      user ID in the response

  Tags                                Response contains a non-empty tags
                                      list
  -----------------------------------------------------------------------

### Web UI scenario coverage

  -----------------------------------------------------------------------
  Area                                What is checked
  ----------------------------------- -----------------------------------
  Valid login                         The user can log in to DemoBlaze
                                      with the configured valid
                                      credentials

  -----------------------------------------------------------------------

The table describes the coverage currently represented by the project
files. It does not imply that every possible edge case, negative
scenario, or browser has been tested.

## Test Reports

The project generates test output under the Gradle `build/` directory.
The current workflow and runner configuration use the following report
locations:

``` text
build/
├── reports/
│   ├── api-tests/
│   ├── web-tests/
│   └── cucumber-reports/
│       ├── api/
│       │   ├── report.html
│       │   └── report.json
│       └── web/
│           ├── report.html
│           └── report.json
```

The exact files present depend on which tasks have run and how the
report plugins are configured.

-   **HTML reports** are intended for convenient visual review.
-   **JSON reports** contain structured results that can be consumed by
    other tools.
-   **Gradle test output** helps identify assertion failures,
    exceptions, and setup problems.

After running a test task, check the relevant report path in the local
`build/` directory. Build output is generated and should generally not
be treated as source code.

### Download reports from GitHub Actions

1.  Open the repository on GitHub.
2.  Select the **Actions** tab.
3.  Open the workflow run you want to inspect.
4.  Select the relevant API or Web UI job.
5.  Find the uploaded artifact section and download the report artifact.
6.  Extract the downloaded archive and open the HTML report in a browser
    if one is included.

Artifact names and available report files depend on the workflow
configuration and the results of the run.

## GitHub Actions CI

The workflow is defined in:

``` text
.github/workflows/qa-automation.yml
```

The workflow separates the test suites into two jobs:

-   **API tests** --- executes the `apiTest` Gradle task.
-   **Web UI tests** --- executes the `webTest` Gradle task with the
    browser configured for headless CI execution.

The workflow uses Java 25, reads required credentials from GitHub
Actions secrets, and uploads report artifacts so results can be reviewed
after the workflow finishes.

### Typical CI workflow

1.  A workflow run is triggered according to the events configured in
    `qa-automation.yml`.
2.  GitHub Actions prepares the runner and Java environment.
3.  Gradle executes the API test job.
4.  Gradle executes the Web UI test job.
5.  Test reports are uploaded as artifacts when the workflow's upload
    steps run.
6.  The workflow run shows whether each job succeeded or failed.

For the exact trigger events, job dependencies, runner image, artifact
names, and upload paths, refer to `.github/workflows/qa-automation.yml`.
Those details should be kept synchronized with this README whenever the
workflow changes.

### Troubleshooting CI failures

-   **Missing secret:** Check the repository's Actions secrets and
    verify the names used by the workflow.
-   **API request fails:** Check the DummyAPI App ID, connectivity, and
    the API response body.
-   **Web UI login fails:** Verify the configured DemoBlaze credentials
    and inspect the browser/test output.
-   **Browser startup fails:** Check the WebDriver configuration and
    headless options.
-   **Report artifact is missing:** Check whether the test created the
    expected files and whether the workflow's artifact path matches
    their actual location.
-   **A test fails only in CI:** Compare the local and CI configuration,
    environment variables, browser mode, and external-service
    availability.

## How the Framework Works

The framework follows a simple flow:

``` text
Cucumber Feature File (.feature)
             |
             v
      Cucumber Test Runner
             |
             v
      Java Step Definitions
        /             \
       v               v
   API Client       Page Object
       |               |
       v               v
   DummyAPI         Selenium WebDriver
                         |
                         v
                     DemoBlaze
             |
             v
      Assertions and Reports
```

### API test flow

1.  A feature file describes an API scenario in readable Gherkin.
2.  `ApiTestRunner` launches the API suite.
3.  Cucumber matches each step to a method in `ApiSteps.java`.
4.  `ApiClient` provides the shared request configuration.
5.  REST Assured sends the request to DummyAPI.
6.  JUnit assertions verify the status code and response data.
7.  Logs and configured reports help the tester inspect the result.

### Web UI test flow

1.  A feature file describes the user interaction.
2.  `WebTestRunner` launches the Web UI suite.
3.  `LoginSteps` maps Gherkin steps to Java actions.
4.  `LoginPage` encapsulates login-page interactions.
5.  `DriverFactory` configures the WebDriver, while `Hooks` manages
    browser lifecycle tasks.
6.  Selenium interacts with DemoBlaze.
7.  Assertions verify the expected UI outcome and the run produces test
    output.

## Troubleshooting

### Gradle command is not recognized

Run the Wrapper from the repository root:

``` powershell
.\gradlew.bat apiTest
```

Do not use `gradle apiTest` unless Gradle is installed globally and
configured in your environment.

### Java version mismatch

Check:

``` powershell
java -version
.\gradlew.bat --version
```

Make sure the Java version used by the terminal and Gradle matches the
project's Java 25 configuration.

### API returns an unexpected status code

1.  Inspect the response status and response body in the test output.
2.  Confirm the App ID is configured correctly.
3.  Confirm the endpoint and request payload.
4.  Check whether DummyAPI is reachable and whether its current behavior
    matches the test expectation.

### User ID is null in an update or delete step

The update/delete steps need a valid `userId` before making their
requests. Ensure that the scenario creates or selects the target user
before attempting the operation. Cucumber scenarios normally have
separate scenario state; one scenario should not rely on a field
populated by a different scenario.

### Web UI login fails

1.  Confirm the local or CI credentials are configured.
2.  Check whether DemoBlaze is reachable.
3.  Inspect the browser and Selenium logs.
4.  Verify that the page elements and login behavior have not changed.

### Tests pass locally but fail in GitHub Actions

Check secrets, Java versions, headless browser settings, external
service availability, and the paths used to upload reports. Avoid
changing assertions until the failure output shows why the current
expectation is incorrect.

## Security Notes

-   Never commit passwords, access tokens, or private API credentials.
-   Keep real credentials in local environment/configuration mechanisms
    or GitHub Actions secrets.
-   Avoid printing secret values in test logs.
-   Review `git status` before committing changes.
-   If a secret is accidentally committed, remove it from the repository
    and rotate the credential; deleting it in a later commit alone does
    not make the exposed secret safe.

## Future Improvements

Potential extensions for this learning project include:

-   Adding negative API scenarios and more response-field validations.
-   Adding additional Web UI scenarios such as invalid login and other
    user flows.
-   Improving report presentation and retaining artifacts consistently.
-   Adding reusable test data setup and cleanup where appropriate.
-   Expanding browser and environment configuration.
-   Documenting additional scenarios as they are implemented.

These are suggestions for future development, not claims that the
features are already implemented.

------------------------------------------------------------------------

## Maintainer

**Repository:**
[QA-Automation-Framework](https://github.com/Reyhanjaw/QA-Automation-Framework)

This project is intended for QA automation learning and demonstration.
Update this README whenever source structure, required configuration,
test coverage, or CI behavior changes.
