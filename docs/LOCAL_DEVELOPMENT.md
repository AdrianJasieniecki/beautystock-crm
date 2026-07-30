# Local Development

## 1. Status

The minimal `backend-api` Spring Boot skeleton exists and its build and startup
commands are verified. Its shared error handling and request-body Bean Validation
mapping are implemented, while no business endpoint exists yet. PostgreSQL,
Spring Data JPA, Flyway, and the first Testcontainers integration test are
implemented. RabbitMQ, `backend-worker`, `frontend`, Dockerfiles, and
`compose.yml` are still planned. This document distinguishes commands that work
now from the future full-system workflow.

## 2. Prerequisites

Planned developer tools:

| Tool | Requirement |
| --- | --- |
| Git | Current supported version |
| JDK | Java 21 |
| Maven | Use Maven 3.9.16 through the wrapper in `backend-api` |
| Node.js | Active LTS selected in frontend foundation Issue |
| npm/pnpm | One package manager selected and locked |
| Docker | Running Docker Engine/Desktop; required by the PostgreSQL integration test |
| API client | Optional: curl, HTTP client, or Postman |

Verify the current backend toolchain:

```bash
java -version
cd backend-api
./mvnw -version
```

Both commands must report Java 21. The complete backend verification also
requires Docker:

```bash
docker version
```

Verify the remaining tools when their project foundations are introduced:

```bash
node --version
docker compose version
```

## 3. Local and planned services

| Service | Purpose | Current state |
| --- | --- | --- |
| PostgreSQL | Application database | Local installation for manual startup; PostgreSQL 17 Testcontainer in tests; Compose exposure planned |
| RabbitMQ | Event broker | Planned: AMQP plus local management UI |
| backend-api | REST API | Implemented: `8080` by default; override with `SERVER_PORT` |
| backend-worker | Queue consumer | Planned; no public application port required except management |
| frontend | Admin UI | Planned development or container port |

The manual datasource URL selects the local PostgreSQL port. Compose ports remain
intentionally deferred until Issue #53.

## 4. Environment configuration

Use environment variables for environment-specific values. The ignored
`backend-api/.env.local` file may hold local-only values; never commit it or any
real/shared credentials.

Implemented backend variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
SERVER_PORT
SPRING_PROFILES_ACTIVE
```

`DB_URL` is a complete JDBC URL such as
`jdbc:postgresql://localhost:<local-port>/<local-database>`. `SERVER_PORT` is
optional and defaults to `8080`.

Future components are expected to add variables such as:

```text
RABBITMQ_HOST
RABBITMQ_PORT
RABBITMQ_USERNAME
RABBITMQ_PASSWORD
API_BASE_URL
```

Future names must be reconciled with their actual configuration during
implementation. Examples must use clearly synthetic local-only credentials.

## 5. Spring profiles

### `local`

- activates the environment-backed PostgreSQL datasource using `DB_URL`,
  `DB_USERNAME`, and `DB_PASSWORD`;
- reads the HTTP port from `SERVER_PORT`, defaulting to `8080`;
- runs Flyway before JPA validates the schema;
- may later expose selected local management endpoints;
- does not alter the schema with Hibernate.

### `test`

- the current integration test does not activate a separate profile;
- `@ServiceConnection` supplies datasource connection details from a fresh
  PostgreSQL 17 Testcontainer;
- `.env.local` and a developer's local PostgreSQL are not used by tests;
- deterministic time/external effects remain required where relevant.

### Production-like profile

- receives secrets and URLs from the environment/platform;
- uses safe logging and management exposure;
- validates schema after Flyway;
- fails fast for missing required configuration.

Do not create many profiles that differ only by small values. Prefer common
configuration plus environment variables.

## 6. Current backend workflow

From the repository root:

```bash
cd backend-api
./mvnw clean verify
```

Docker must be running because the full build starts a PostgreSQL Testcontainer.
It applies V1 from an empty database, starts JPA with schema validation, and runs
the remaining unit, JSON, and MVC tests.

To start the application manually, first provide a running PostgreSQL database
and create the ignored `backend-api/.env.local` file with the three required
datasource variables. Then, from `backend-api`:

```bash
set -a
source .env.local
set +a
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

Expected results:

- the complete build passes 23 tests;
- the application reports the `local` profile;
- Flyway applies or validates V1 and Hibernate validates the resulting schema;
- embedded Tomcat listens on port `8080`, unless `SERVER_PORT` overrides it;
- `GET /` returns `404` because the foundation task deliberately adds no sample
  or business endpoint.

Stop the application with `Ctrl+C`.

## 7. Planned full-system startup workflow

After relevant Issues are implemented:

1. clone the repository and checkout `Production`;
2. copy safe environment template to a local ignored file;
3. start PostgreSQL and RabbitMQ;
4. verify dependency health;
5. start `backend-api`; Flyway migrates the database;
6. verify API readiness and migration status;
7. start `backend-worker`;
8. start `frontend`;
9. run a documented smoke flow.

The final Compose phase should reduce this to a small number of verified commands.

## 8. Backend test workflow

Run Maven commands from `backend-api`:

```bash
./mvnw test
./mvnw verify
./mvnw clean verify
```

`backend-api` is an independent Maven build. There is no root parent or
aggregator build at this stage; see ADR-015 in [Architecture decisions](DECISIONS.md).
Docker is required whenever the PostgreSQL integration test runs. Do not source
`.env.local` for tests; the container service connection supplies test database
properties.

Test selection guidance:

- run pure unit tests frequently;
- run slice tests for the changed adapter;
- run PostgreSQL/RabbitMQ Testcontainers integration tests before PR;
- run the full relevant build before requesting review.

## 9. Planned frontend workflow

Expected commands after the frontend toolchain is selected:

```bash
npm install
npm run dev
npm run test
npm run build
```

The committed lockfile is authoritative. Do not mix npm, pnpm, and yarn lockfiles.

## 10. Database and Flyway

- Flyway owns schema history.
- `V1__baseline.sql` is an intentionally comment-only empty-domain migration;
  the next business migration starts at V2.
- Hibernate uses `ddl-auto=validate`; no profile may use `update`, `create`, or
  `create-drop` as an alternative migration mechanism.
- Open EntityManager in View is disabled so database access does not escape
  deliberate application-service transaction boundaries.
- Local reset instructions must name and limit the exact development volume or
  database; avoid broad destructive commands.
- Do not edit the merged V1. Correct later schema changes with a new forward-only
  migration.
- Test migrations from an empty database and from the previous schema.

## 11. RabbitMQ

Local documentation will eventually include:

- exchange, queue, routing-key, retry, and DLQ names;
- management UI access with local-only credentials;
- how to inspect ready/unacknowledged/dead-letter messages;
- how to replay a failed message safely;
- how consumers stop gracefully.

## 12. Troubleshooting checklist

When startup fails:

1. from `backend-api`, confirm `java -version` and `./mvnw -version` both report
   Java 21;
2. for tests, confirm Docker is running and can start containers;
3. for manual startup, confirm local PostgreSQL is running;
4. check the active Spring profile;
5. check that `.env.local` was sourced and required variables exist without
   printing their values;
6. inspect Flyway migration status;
7. check the JDBC host, database, and port without exposing credentials;
8. when RabbitMQ exists, check its vhost/user permissions;
9. run the smallest failing test;
10. document a recurring fix in this file.

## 13. Safe local data handling

- Use synthetic salon/customer data.
- Do not use real customer contact details.
- Treat database dumps, heap dumps, and log archives as potentially sensitive.
- Keep diagnostic artifacts outside Git and delete them using a deliberate,
  narrow operation when no longer needed.
