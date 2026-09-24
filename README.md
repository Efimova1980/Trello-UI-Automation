# QA_50_32_Trello_Project


https://github.com/user-attachments/assets/d25fea71-5fee-4b9c-a397-0787e1be90f4



A demo UI test automation project for [Trello](https://trello.com), built with Java and Selenium WebDriver using the Page Object pattern.

## Stack

- **Java 21**
- **Selenium WebDriver 4.47** — browser automation
- **TestNG 7.11** — test runner, groups, assertions
- **Gradle** — build and test execution
- **DataFaker** — test data generation
- **com.atlassian:onetime** — TOTP code generation for two-factor authentication (2FA)
- **Logback** — logging
- **Lombok**

## Project structure

```
src/main/java/
  dto/       — data models (User, Board)
  manager/   — AppManager: WebDriver setup/teardown, shared login logic
  pages/     — Page Object classes (HomePage, LoginPage, BoardsPage, MyBoardPage, AtlassianProfilePage, BasePage)
  utils/     — helper classes (screenshot capture on test failure, event listeners)

src/test/java/tests/
  LoginTest.java              — positive and negative login scenarios (wrong password/email/TOTP secret)
  BoardTests.java             — board creation and deletion
  ChangeProfilePhotoTests.java — profile photo change (positive and negative scenario)

src/test/resources/
  smoketests.xml   — TestNG suite: tests tagged with the "smoke" group (one scenario per class)
  logintests.xml   — TestNG suite: login tests only (the whole LoginTest class)
```

## Authentication and environment variables

The test account is protected by two-factor authentication (2FA). Logging in requires three environment variables:

| Variable | Purpose |
|---|---|
| `TRELLO_EMAIL` | Test account email |
| `TRELLO_PASSWORD` | Test account password |
| `TRELLO_TOTP_SECRET` | Base32 secret used to generate the TOTP code |

Locally these are set as environment/IDE run-configuration variables. In CI they're stored as GitHub Secrets (`Settings → Secrets and variables → Actions`) and passed into the workflow via the `env` block.

⚠️ **Note:** every test that logs in performs a full sign-in, including entering a TOTP code. Trello/Atlassian rate-limits repeated login attempts from the same source — running many logins in a short time (e.g. a full test suite, or frequent repeated workflow runs) can cause the TOTP code to be rejected regardless of whether it's correct. Because of this, only one test is launched for demonstration purposes—a positive login test.

## Running tests

### Locally

```
./gradlew clean test          # full test suite
./gradlew clean smoketests    # smoke tests only (one scenario per class)
./gradlew clean logintests    # login tests only (one scenario)
```

On Windows, use `.\gradlew` instead of `./gradlew`.

### CI (GitHub Actions)

The repository runs a single workflow, `.github/workflows/login-tests.yml`, which executes the `logintests` Gradle task (the `LoginTest` class only) on a `windows-latest` runner. It triggers on push to `master` or manually via `workflow_dispatch`.

Before the build, the workflow forces a system clock sync (`Force Sync Clock`) — TOTP codes are sensitive to clock drift on the runner relative to real time.

The `smoketests` Gradle task still exists in the project (`./gradlew clean smoketests`) and can be run locally, but it is not currently wired into a CI workflow.

## Reports and artifacts

Each CI run uploads:
- **test-report** — HTML test report (`build/reports/tests/...`)
- **screenshots** — screenshots automatically captured on test failure (`build/screenshots/`)

Both are available on the run's page under **Actions → [run] → Artifacts**.
