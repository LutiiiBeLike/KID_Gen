# CID Generator

A beginner-friendly Spring Boot application that creates unique Contract IDs
(CIDs) and records each generated CID with its contract information in
PostgreSQL.

## CID format

A CID is always six characters: `C` + range letter + four base-34 characters.
The configured initial range letter is `D`, so the first CIDs are `CD0000`,
`CD0001`, `CD0009`, `CD000A`, and `CD000B`.

The first `C` is fixed. The second character is the current CID range. The
last four characters are a counter written with this base-34 alphabet:

```text
0123456789ABCDEFGHJKLMNPQRSTUVWXYZ
```

`I` and `O` are intentionally excluded so that an ID is less likely to be
misread as a number. Decimal `0` becomes `0000`, `10` becomes `000A`, `33`
becomes `000Z`, and `34` becomes `0010`. Four base-34 characters allow values
from `0` through `1,336,335` (`34^4 - 1`). The next request after that limit
returns `409 Conflict` with `CID range D is exhausted`.

The database counter stores the ordinary decimal value, not the base-34 text.
It starts at `0`, therefore the explicitly chosen first CID is `CD0000`.

## Configure the range letter

`src/main/resources/application.properties` contains:

```properties
cid.range-letter=D
```

Spring reads this property and passes it into `CidService` using `@Value` in
its constructor. To begin another range, add a corresponding `cid_counter`
row in PostgreSQL and change this property to that one uppercase letter.

## PostgreSQL setup

Prerequisites: Java 21, Maven 3.9+, and Docker Desktop.

Create a local `.env` from the ignored template, choose a local password, and
start PostgreSQL:

```bash
cp .env.example .env
docker compose up -d
```

Start the API with the environment values loaded:

```bash
set -a
source .env
set +a
mvn spring-boot:run
```

Flyway applies the SQL files in `src/main/resources/db/migration`. V3 removes
the previous KID-only tables in this development project and creates:

- `cid_counter(range_letter, counter)`, initialized with `D, 0`
- `generated_cid`, the CID audit history, with a unique `cid` column

The counter and audit insert are one transaction. The repository uses a
`PESSIMISTIC_WRITE` lock on the current range’s row: when two requests arrive
together, the second waits for the first transaction to finish before it reads
the counter. This prevents duplicate counter values; the database unique
constraint is an additional safeguard. Because the counter is in PostgreSQL,
restarting Spring Boot does not reset it.

## Generate a CID

Send a POST request to `/api/cids`:

```bash
curl -i -X POST http://localhost:8080/api/cids \
  -H "Content-Type: application/json" \
  -d '{
    "hrSystem": "SAP",
    "eonAccountingAreaID": "DE01",
    "employeeNumber": "12345678"
  }'
```

The response is `201 Created`:

```json
{
  "cid": "CD0000"
}
```

All three request fields must contain non-whitespace text. Invalid requests
return `400 Bad Request` with a small JSON error. Unique database conflicts and
CID range exhaustion return `409 Conflict`. Unexpected failures return `500`
without exposing a Java stack trace.

## Key Spring annotations

- `@RestController` handles HTTP requests and returns JSON; `@PostMapping`
  maps the generation method to POST, and `@RequestBody` reads JSON.
- `@Service` marks the class containing CID generation rules.
- `@Entity` maps a Java class to a table, `@Id` is its primary key, and
  `@Column` maps fields to columns.
- `JpaRepository` supplies basic database operations. `@Lock` requests the
  database lock used when reading the counter.
- `@Transactional` makes saving the audit row and advancing the counter
  all-or-nothing.

## Tests and build

Run the automated tests and package the application:

```bash
mvn test
mvn package
```

The tests cover base-34 boundary values, four-character formatting, the lack
of `I`/`O`, sequence progression, stored audit data, exhaustion, request
validation, and `POST /api/cids`. The old `/api/kids` endpoint and KID
generation code no longer exist.
