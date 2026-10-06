# Setup

You need a working Java toolchain and the course agent tool.

## 1. Java and Maven

- Install a JDK, version 21 or newer. Check with `java --version`.
- Install Maven 3.8 or newer. Check with `mvn --version`.

## 2. Get the repo

Clone your fork and work inside it. Commit as you go. Milestone 1 asks you to
write a characterization test before you direct any refactor, and a commit is
a cheap way to show a TA the order you worked in.

## 3. Build and run the tests

From this directory:

```
mvn -B test
```

## 4. What green looks like

Surefire prints one line per test class:

- `BookingWorkflowTest`: 18 tests, 0 failures, 0 errors, 0 skipped
- `ReportServiceTest`: 7 tests, 0 failures, 0 errors, 0 skipped
- `PriceCalculatorTest`: 6 tests, 0 failures, 0 errors, 0 skipped
- `NotificationHubTest`: 4 tests, 0 failures, 0 errors, 0 skipped

then `Tests run: 35, Failures: 0, Errors: 0, Skipped: 0` and `BUILD SUCCESS`.

That is the starting state. If it is red before you have changed anything, you
have an environment problem to sort out first.

Your own characterization test raises the total by one (name the class
`...Test`, or Maven skips it). The shipped 35 stay green through every
milestone. If one of them goes red after the refactor, the refactor changed
behavior and you have something to fix.

## 5. Editor

Use any editor or IDE you like. VS Code, IntelliJ IDEA, and Eclipse all import
a Maven project directly. Open this folder, the one with `pom.xml`.

## 6. Course agent tool

Configure the course's coding agent as described on the course page, and point
it at this folder. It performs the refactor in milestone 1. That is expected.
What you write in `REFACTOR.md`, and the test you
write before the agent touches the workflow, are what you answer for at
recitation.
