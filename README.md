# Trello-UI-Automation

[![Smoke tests](https://github.com/Efimova1980/Trello-UI-Automation/actions/workflows/smoke-tests.yml/badge.svg)](https://github.com/Efimova1980/Trello-UI-Automation/actions/workflows/smoke-tests.yml)

https://github.com/user-attachments/assets/d25fea71-5fee-4b9c-a397-0787e1be90f4

UI test automation project for [Trello](https://trello.com), built with Java, Selenium WebDriver and TestNG using the Page Object pattern. The test account is protected by two-factor authentication (2FA), and the tests log in with a generated TOTP code.

## Stack

- **Java 21**
- **Selenium WebDriver 4.47** — browser automation
- **TestNG 7.11** — test runner, groups, dependencies, assertions
- **Gradle** — build and test execution
- **DataFaker** — test data generation
- **com.atlassian:onetime** — TOTP code generation for 2FA
- **Logback** — logging
- **Lombok**
- **GitHub Actions** — CI

## What is tested

| Test class | Scenarios |
|---|---|
| `LoginTest` | Positive login with email, password and TOTP code. Negative scenarios (wrong email / password / TOTP) are written but disabled because of login rate limits. |
| `BoardTests` | Create a board → delete the same board. Delete depends on create and is skipped if creation fails. |
| `ChangeProfilePhotoTests` | Change the profile photo (positive); upload a file with a wrong format (negative). |

## Project structure

```
src/main/java/
  dto/       — data models (User, Board)
  manager/   — AppManager: browser lifecycle, login, return to the start page
  pages/     — Page Objects (BasePage, HomePage, LoginPage, BoardsPage, MyBoardPage, AtlassianProfilePage)
  utils/     — TestNG listener (test logs + screenshot on failure), WebDriver listener (action logs)

src/test/java/tests/
  LoginTest, BoardTests, ChangeProfilePhotoTests

src/test/resources/
  smoketests.xml   — Smoke suite: BoardTests → ChangeProfilePhotoTests ("smoke" group)
  logintests.xml   — Login suite: LoginTest
  logback.xml      — logging to console and to src/test/test_logs/
```

## Design decisions

- **One login per test class.** Atlassian rate-limits repeated logins: when there are too many, TOTP codes get rejected even if they are correct. So the browser is opened and the user logs in once per class (`@BeforeClass`), not before every test.
- **Independent tests in one session.** Before each test, `openTrello()` closes extra tabs (for example, the Atlassian account tab) and opens the boards page, so every test starts from the same point.
- **Stable locators.** Locators use `data-testid`, `name` or visible text instead of auto-generated IDs and CSS classes, which change between releases.
- **Explicit waits instead of fixed pauses.** Tests wait for a condition (element present, clickable, URL, number of tabs) instead of `Thread.sleep`.
- **Fresh TOTP window.** If less than 5 seconds are left in the current 30-second TOTP window, the test waits for the next one, so the code does not expire on the way to the server.

## Authentication and environment variables

Logging in requires three environment variables:

| Variable | Purpose |
|---|---|
| `TRELLO_EMAIL` | Test account email |
| `TRELLO_PASSWORD` | Test account password |
| `TRELLO_TOTP_SECRET` | Base32 secret used to generate the TOTP code |

Locally they are set as environment / IDE run-configuration variables. In CI they are stored as GitHub Secrets (`Settings → Secrets and variables → Actions`).

## Running tests

```
./gradlew clean smoketests    # Smoke suite: boards, then profile photo (2 logins)
./gradlew clean logintests    # Login suite
```

On Windows, use `.\gradlew` instead of `./gradlew`.

⚠️ Running many suites in a row may trigger the login rate limit. Leave a pause between runs.

## CI (GitHub Actions)

| Workflow | Runs | Trigger |
|---|---|---|
| `smoke-tests.yml` | `smoketests` | on push to `master`, or manually |
| `login-tests.yml` | `logintests` | manually only (`workflow_dispatch`) |

Both run on `windows-latest`. Before the tests, the system clock is synced (`Force Sync Clock`), because TOTP codes depend on accurate time.

## Reports and artifacts

Each CI run uploads:
- **test-report** — HTML test report (`build/reports/tests/...`)
- **screenshots** — screenshots taken automatically on test failure (`build/screenshots/`)

Both are available on the run page under **Actions → [run] → Artifacts**.
