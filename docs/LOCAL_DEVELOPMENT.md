# Local Development

## 1. Status

This document describes the planned local environment. Application skeletons,
Dockerfiles, `compose.yml`, ports, and final commands do not exist yet. Replace
placeholders with verified commands as their Issues are implemented.

## 2. Prerequisites

Planned developer tools:

| Tool | Requirement |
| --- | --- |
| Git | Current supported version |
| JDK | Java 21 |
| Maven | Wrapper preferred once backend exists |
| Node.js | Active LTS selected in frontend foundation Issue |
| npm/pnpm | One package manager selected and locked |
| Docker | Docker Engine/Desktop with Compose v2 |
| API client | Optional: curl, HTTP client, or Postman |

Verify versions after project skeletons exist:

```text
java -version
./mvnw -version
node --version
docker version
docker compose version
```

## 3. Planned local services

| Service | Purpose | Planned exposure |
| --- | --- | --- |
| PostgreSQL | Application database | Host port chosen in Compose |
| RabbitMQ | Event broker | AMQP plus local management UI |
| backend-api | REST API | Host HTTP port |
| backend-worker | Queue consumer | No public application port required except management |
| frontend | Admin UI | Development or container port |

Exact ports are intentionally deferred to avoid documenting values before
configuration exists.

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

- connects to local/Compose PostgreSQL and RabbitMQ;
- enables developer-friendly logs without exposing secrets;
- may expose selected local management endpoints;
- does not alter schema with Hibernate.

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

## 6. Planned startup workflow

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

## 7. Planned backend workflow

Expected Maven wrapper commands:

```text
./mvnw test
./mvnw verify
./mvnw spring-boot:run
```

The exact multi-module command depends on whether the repository uses a root
parent build. This remains a decision for the backend foundation Issue.

Test selection guidance:

- run pure unit tests frequently;
- run slice tests for the changed adapter;
- run PostgreSQL/RabbitMQ Testcontainers integration tests before PR;
- run the full relevant build before requesting review.

## 8. Planned frontend workflow

Expected commands after the frontend toolchain is selected:

```text
npm install
npm run dev
npm run test
npm run build
```

The committed lockfile is authoritative. Do not mix npm, pnpm, and yarn lockfiles.

## 9. Database and Flyway

- Flyway owns schema history.
- Hibernate must not use `ddl-auto=update` in production-like environments.
- Local reset instructions must name and limit the exact development volume or
  database; avoid broad destructive commands.
- Never edit an already shared migration casually.
- Test migrations from an empty database and from the previous schema.

## 10. RabbitMQ

Local documentation will eventually include:

- exchange, queue, routing-key, retry, and DLQ names;
- management UI access with local-only credentials;
- how to inspect ready/unacknowledged/dead-letter messages;
- how to replay a failed message safely;
- how consumers stop gracefully.

## 11. Troubleshooting checklist

When startup fails:

1. confirm selected Java/Node/Docker versions;
2. confirm dependency containers are healthy, not merely started;
3. check active Spring profile;
4. check required environment variables without printing secrets;
5. inspect Flyway migration status;
6. check host/container port mapping;
7. check RabbitMQ vhost/user permissions;
8. use correlation/event IDs to follow logs;
9. run the smallest failing test;
10. document a recurring fix in this file.

## 12. Safe local data handling

- Use synthetic salon/customer data.
- Do not use real customer contact details.
- Treat database dumps, heap dumps, and log archives as potentially sensitive.
- Keep diagnostic artifacts outside Git and delete them using a deliberate,
  narrow operation when no longer needed.

