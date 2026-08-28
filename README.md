# KID Generator

This is a beginner-friendly Java learning project for generating **KIDs**
(person identifiers). The current version implements only the basic KID
generation logic. It is not a Spring Boot application yet.

## Current status

Implemented:

- KID generation in the format `XYYYY`, for example `M0001`
- One in-memory counter for each letter from `A` to `Z`
- Unicode accent conversion, for example `Ä -> A` and `Ö -> O`
- Fallback from `givenName` to `sn`, then to `X`
- Simple application logging when a KID is generated

Not implemented yet:

- JUnit tests
- CID generation
- Spring Boot and a REST API
- Database persistence
- Concurrency protection
- OAuth 2.0 security

## Prerequisites

- Java 21
- Maven 3.9 or newer

On the current macOS development environment, Java 21 installed with Homebrew
can be selected for the current terminal session with:

```bash
export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"
java -version
```

The last command should show Java 21.

## Build and test

From the repository root, run:

```bash
mvn clean package
```

This compiles the source code and creates a JAR file in `target/`.

You can also run the test phase directly:

```bash
mvn test
```

There are no JUnit tests in Step 1 yet, so Maven completes successfully with
zero tests. Step 2 will add the KID generator tests.

## Try the generator

This project is currently a Java library, not a command-line or web
application. A simple way to try it is Java's `jshell` after building it:

```bash
jshell --class-path target/classes
```

Then enter the following lines:

```java
import de.eon.kidgen.model.KidRequest;
import de.eon.kidgen.service.KidGenerator;

KidGenerator generator = new KidGenerator();

KidRequest request = new KidRequest(
        "Mustermann",
        "Max",
        "EON",
        "Employee",
        "Standard",
        "Test user"
);

String kid = generator.generateKid(request);
System.out.println(kid);
```

The output is:

```text
M0001
```

Type `/exit` to leave `jshell`.

## How KID generation works

`KidGenerator` uses the first character of `givenName` as the KID letter. If
that character cannot become an ASCII letter from `A` to `Z`, it tries the
first character of `sn`. If neither works, it uses `X`.

Before checking a character, Java's `Normalizer` separates accents from a
letter. For example, `Ö` becomes `O` plus an accent mark. The generator removes
the accent mark and keeps `O`. This allows names such as `Änne`, `Ömer`, and
`Émile` to produce `A`, `O`, and `E`.

The generator keeps a `Map<Character, Integer>` with a separate counter for
each letter. Therefore, two names beginning with `M` produce `M0001` and
`M0002`, while the first name beginning with `A` produces `A0001`.

The number is formatted with at least four digits:

```text
1     -> 0001
42    -> 0042
1000  -> 1000
10000 -> 10000
```

When a KID is generated, Java's normal application logger writes the KID,
request values, and generation time.

## Production readiness

**Do not deploy the current version to production.** Its counters exist only
in memory. Restarting the application resets every counter to zero, and two
requests at the same time could receive the same KID.

Before production use, the project needs all of the following:

1. JUnit tests for the KID rules and edge cases.
2. A relational database that stores counters and generated identifiers.
3. Safe concurrent database updates and unique database constraints, so IDs
   cannot be duplicated.
4. A Spring Boot REST API for controlled access to the generator.
5. Persistent audit logging for generation requests and results.
6. OAuth 2.0 protection for the REST API.
7. Deployment-specific configuration for the database and OAuth provider.

Never commit database passwords, OAuth client secrets, access tokens, or other
credentials to this repository. Production secrets must be supplied through a
secure deployment environment or secret manager.
