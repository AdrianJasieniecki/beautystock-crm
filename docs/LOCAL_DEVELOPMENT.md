# Local Development

## 1. Status

The minimal `backend-api` Spring Boot skeleton exists and its build and startup
commands are verified. PostgreSQL, RabbitMQ, `backend-worker`, `frontend`,
Dockerfiles, and `compose.yml` are still planned. This document distinguishes
commands that work now from the future full-system workflow.

## 2. Prerequisites

Planned developer tools:

| Tool | Requirement |
| --- | --- |
| Git | Current supported version |
| JDK | Java 21 |
| Maven | Use Maven 3.9.16 through the wrapper in `backend-api` |
| Node.js | Active LTS selected in frontend foundation Issue |
| npm/pnpm | One package manager selected and locked |
| Docker | Docker Engine/Desktop with Compose v2 |
| API client | Optional: curl, HTTP client, or Postman |

Verify the current backend toolchain:

```bash
java -version
cd backend-api
./mvnw -version
```

Both commands must report Java 21. Verify the remaining tools when their project
foundations are introduced:

```bash
node --version
docker version
docker compose version
```

## 3. Planned local services

| Service | Purpose | Planned exposure |
| --- | --- | --- |
| PostgreSQL | Application database | Host port chosen in Compose |
| RabbitMQ | Event broker | AMQP plus local management UI |
| backend-api | REST API | `8080` by default; override with `SERVER_PORT` |
| backend-worker | Queue consumer | No public application port required except management |
| frontend | Admin UI | Development or container port |

Ports for services other than `backend-api` are intentionally deferred to avoid
documenting values before their configuration exists.

## 4. Environment configuration

Use environment variables for environment-specific values. Commit only safe
templates such as `.env.example`; never commit a real `.env`.

Proposed variable categories:

```text
SPRING_PROFILES_ACTIVE
DB_HOST
DB_PORT
DB_NAME
DB_USERNAME
DB_PASSWORD
RABBITMQ_HOST
RABBITMQ_PORT
RABBITMQ_USERNAME
RABBITMQ_PASSWORD
API_BASE_URL
```

Names must be reconciled with actual Spring configuration during implementation.
Examples should use clearly local-only credentials.

## 5. Spring profiles

### `local`

- currently activates local configuration and reads the HTTP port from
  `SERVER_PORT`, defaulting to `8080`;
- will connect to local/Compose PostgreSQL and RabbitMQ when those integrations
  are implemented;
- may later expose selected local management endpoints;
- must not alter schema with Hibernate after persistence is introduced.

### `test`

- configured by tests, preferably with Testcontainers service connection or
  dynamic properties;
- isolated data per test context;
- deterministic time/external effects where required.

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
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

Expected results:

- the build completes with the Spring context test passing;
- the application reports the `local` profile;
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
./mvnw spring-boot:run
```

`backend-api` is an independent Maven build. There is no root parent or
aggregator build at this stage; see ADR-015 in [Architecture decisions](DECISIONS.md).

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
- Hibernate must not use `ddl-auto=update` in production-like environments.
- Local reset instructions must name and limit the exact development volume or
  database; avoid broad destructive commands.
- Never edit an already shared migration casually.
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
2. confirm dependency containers are healthy, not merely started;
3. check active Spring profile;
4. check required environment variables without printing secrets;
5. inspect Flyway migration status;
6. check host/container port mapping;
7. check RabbitMQ vhost/user permissions;
8. use correlation/event IDs to follow logs;
9. run the smallest failing test;
10. document a recurring fix in this file.

## 13. Safe local data handling

- Use synthetic salon/customer data.
- Do not use real customer contact details.
- Treat database dumps, heap dumps, and log archives as potentially sensitive.
- Keep diagnostic artifacts outside Git and delete them using a deliberate,
  narrow operation when no longer needed.
