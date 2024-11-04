# U-Fund: Pandas International - A nonprofit Giant Panda preservation organization

An online U-Fund system built in Java 17=> with Angular Framework; manages HTTP requests using Spring  
  
## Team

- Zoe Rizzo
- Ryan Garvin
- Oscar Li
- Jack Sutton
- Veronika Kirievskaya


## Prerequisites

- Java 11=>17 (Make sure to have correct JAVA_HOME setup in your environment)
- Maven
- Java Spring
- Angular
- TypeScript
- HTML/CSS

## How to run it

1. Clone the repository and go to the ufund-api directory.
2. Execute `mvn compile exec:java`
3. Go to the ufund-ui directory.
4. Execute `ng serve --open`
5. Open in your browser: `http://localhost:8080/`   <-- URI 

Default logins:

Helper--
username=helper password=helper

Admin--
username=admin password=admin

## Known bugs and disclaimers 
None at present

## Assumptions

The quantity of a need is the amount that a user will be purchasing, not the total amount of a need that is available in the needs cupboard.

The price of a need is the price of the entire quantity, not individual price.

## How to test it

The Maven build script provides hooks for run unit tests and generate code coverage
reports in HTML.

To run tests on all tiers together do this:

1. Execute `mvn clean test jacoco:report`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/index.html`

To run tests on a single tier do this:

1. Execute `mvn clean test-compile surefire:test@tier jacoco:report@tier` where `tier` is one of `controller`, `model`, `persistence`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/{controller, model, persistence}/index.html`

To run tests on all the tiers in isolation do this:

1. Execute `mvn exec:exec@tests-and-coverage`
2. To view the Controller tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
3. To view the Model tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
4. To view the Persistence tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`

*(Consider using `mvn clean verify` to attest you have reached the target threshold for coverage)
  
  
## How to generate the Design documentation PDF

1. Access the `PROJECT_DOCS_HOME/` directory
2. Execute `mvn exec:exec@docs`
3. The generated PDF will be in `PROJECT_DOCS_HOME/` directory


## How to setup/run/test program 
1. Tester, first obtain the Acceptance Test plan
2. IP address of target machine running the app
3. Execute ________
4. ...
5. ...

## License

MIT License

See LICENSE for details.
