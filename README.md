# KID Generator

A beginner-friendly Spring Boot project that generates person identifiers (KIDs)
and stores their counters and audit history in PostgreSQL. CID generation and
OAuth are not implemented yet.

## KID rules

A KID has the format `XYYYY`, such as `M0001`.

- `X` is an uppercase ASCII letter from `A` to `Z`.
- `YYYY` is the decimal counter for that letter, with at least four digits.
- Each prefix has its own PostgreSQL counter: `M0001`, `M0002`, then `A0001`.

The prefix comes from the first character of `givenName`; if that cannot become
an ASCII letter, `sn` is tried; otherwise `X` is used. Java `Normalizer`
separates an accented letter from its accent mark, allowing `Ä`, `Ö`, `Ü`, `É`,
`Ç`, and `Å` to become `A`, `O`, `U`, `E`, `C`, and `A`.

## Prerequisites

- Java 21
- Maven 3.9 or newer
- Docker Desktop running locally

On the current macOS development environment:

```bash
export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"
java -version
```

## Start PostgreSQL

`.env` is ignored by Git, so a password is never committed. Create it from the
example, then choose a local development password in `DATABASE_PASSWORD`:

```bash
cp .env.example .env
docker compose up -d
```

Docker Compose starts PostgreSQL 17 on port `5432`. Its named volume keeps data
when the container stops.

## Start the API

Load the local database settings, then start Spring Boot:

```bash
set -a
source .env
set +a
mvn spring-boot:run
```

Flyway runs the SQL migrations from `src/main/resources/db/migration` during
startup. It creates the counter and audit tables. Hibernate validates that the
Java entities match that schema but does not create tables itself.

## Generate a KID

With the application running, use a second terminal:

```bash
curl -i -X POST http://localhost:8080/api/kids \
  -H "Content-Type: application/json" \
  -d '{
    "sn": "Müller",
    "givenName": "Max",
    "eonBUshort": "EON",
    "eonUserType": "Employee",
    "eonUserPurpose": "Standard",
    "description": "Test user"
  }'
```

The first request returns `201 Created`:

```json
{
  "kid": "M0001"
}
```

Missing, empty, or whitespace-only required fields return `400 Bad Request`:

```json
{
  "error": "givenName must not be empty"
}
```

Inspect the stored counter and audit rows after loading `.env`:

```bash
docker compose exec db psql -U "$DATABASE_USERNAME" -d "$DATABASE_NAME" \
  -c "SELECT letter, counter FROM kid_counter WHERE letter = 'M';"

docker compose exec db psql -U "$DATABASE_USERNAME" -d "$DATABASE_NAME" \
  -c "SELECT kid, given_name, sn, created_at FROM generated_kid;"
```

## Important Spring concepts

- `@RestController` handles HTTP requests and returns JSON.
- `@PostMapping` maps a Java method to an HTTP POST request.
- `@RequestBody` reads request JSON into a Java object.
- `@Service` marks the KID business logic class.
- `@Entity` maps a Java class to a database table; `@Id` is its primary key and
  `@Column` maps a field to a database column.
- `JpaRepository` provides simple database operations such as save and find.
- `@Transactional` makes the counter update and audit insert all-or-nothing.
- Constructor injection makes a class's required dependencies explicit.

PostgreSQL stores the data. JPA maps Java entities to PostgreSQL rows, and
Spring Data creates repository implementations from the repository interfaces.

`PESSIMISTIC_WRITE` locks the selected counter row during generation. A second
request for the same letter waits for the first request to commit, preventing
both requests from creating the same KID. The unique `generated_kid.kid`
constraint is an additional database safety check.

## Build and production status

Run these before committing:

```bash
mvn test
mvn package
```

JUnit is configured but KID test classes are the next planned step, so the
current test phase completes with zero tests. Before production use, add those
tests, CID support, operational monitoring, and OAuth 2.0. Supply production
database and OAuth secrets through a deployment environment or secret manager;
never commit them to Git.
