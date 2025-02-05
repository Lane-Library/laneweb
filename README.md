# Laneweb

## Prerequisites

1. **[Required tools and software](https://gitlab.med.stanford.edu/irt-lane/som-laneweb/-/blob/main/tools.md)**

## Build Laneweb image

### Clone the laneweb repo and build app jar
    
```
$ cd $HOME/projects/lane
$ git clone git@gitlab.med.stanford.edu:lane/laneweb.git
$ cd laneweb

$ make docker
```

## Push to repo

```
$ make push
```

## Pull the latest image from repo

```
$ make pull
```
## CI/CD Support

### Get and setup personal gitlab access token
Gitlab API requires access token to talk to gitlab server.

Please get your personal token from https://gitlab.med.stanford.edu/-/profile/personal_access_tokens
and save the the token to ${HOME}/.gitlab-token file.

_NOTE_: Do not add newline at the end of the token.

### Setup gitlab pipeline and slack notification

```
$ make gl-setup
```

## Running Cypress Tests

LaneWeb uses [Cypress](https://www.cypress.io/) for end-to-end integration testing. `mvn clean verify` will launch Cypress in a test container and run it against a working instance of LaneWeb. To run Cypress tests manually, follow these steps:

1. **Prepare the environment**
    - Ensure you have all necessary dependencies installed.
    - Note, dependent services like `eresource-service` will need to be accessible to LaneWeb for tests to pass.
    - Run `mvn clean package` to copy the JavaScript files to `/target/test-classes/e2e`.
    - Start LaneWeb (Cypress `baseUrl` is http://localhost:8080/)

1. **Run Cypress from the UI**
    - Navigate to the directory containing the Cypress tests:
      ```
      $ cd target/test-classes/e2e
      ```
    - Instrument the code:
      ```
      $ npm run instrument
      ```
    - Open the Cypress UI:
      ```
      $ npx cypress open
      ```

1. **Run all Cypress tests via the command line**
    - Navigate to the directory containing the Cypress tests:
      ```
      $ cd target/test-classes/e2e
      ```
    - Execute the tests:
      ```
      $ npm test
      ```
### Disabling Cypress Tests

Use the property `cypress.tests.disable=true` to disable Cypress testing. This is useful, for example, to skip Cypress at Maven `release` time.