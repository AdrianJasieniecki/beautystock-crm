# BeautyStock CRM

BeautyStock CRM is an educational portfolio project that models a CRM and mini-ERP
for a small beauty-products wholesaler. Beauty salons are managed as customers,
products are tracked in a catalogue and warehouse, orders reserve stock, simple
demand recommendations help with replenishment, and asynchronous notifications
are processed by a RabbitMQ worker.

The project is intentionally developed in small, reviewable steps. The repository
owner implements application tasks manually to build practical Mid Java Backend
Developer skills. Codex supports architecture, task preparation, documentation,
and code review unless implementation is explicitly requested.

## Educational goals

The project is designed to demonstrate practical knowledge of:

- Java 21 and Spring Boot;
- resource-oriented REST APIs, DTOs, mapping, and Bean Validation;
- JPA/Hibernate, persistence context, dirty checking, flush, commit, and
  transaction boundaries;
- PostgreSQL, SQL constraints, indexes, and Flyway migrations;
- RabbitMQ messaging, retries, dead-letter queues, idempotency, and the future
  Outbox Pattern;
- unit, slice, integration, and Testcontainers-based tests;
- Docker, Docker Compose, Kubernetes, and an Azure deployment path;
- environment configuration, profiles, secrets, health checks, metrics, and
  structured logs;
- React and TypeScript fundamentals;
- clean code, modularity, SRP, naming, and disciplined code review;
- JVM diagnostics: heap, stack, GC logs, heap dumps, and thread dumps.

## Planned architecture

BeautyStock CRM starts as a modular monolith with an optional separate worker:

- `backend-api` — Spring Boot REST API and transactional business use cases;
- `backend-worker` — RabbitMQ consumers and notification processing;
- `frontend` — React + TypeScript administration panel;
- PostgreSQL — transactional source of truth;
- RabbitMQ — asynchronous event transport.

This approach keeps deployment and local development understandable while making
module boundaries explicit enough to support a later extraction of selected
capabilities.

## Business modules

| Module | Responsibility |
| --- | --- |
| Salon CRM | Salon profiles, contact data, lifecycle status, notes, order history |
| Product Catalog | Products, brands, categories, prices, SKU, active status |
| Inventory | Stock level, adjustments, movements, reservations, concurrency |
| Orders | Drafting, placing, reserving, confirming, cancelling, fulfilling |
| Demand Forecasting | Consumption averages, reorder estimates, low-stock recommendations |
| Notifications | Order and low-stock events processed asynchronously |
| Frontend Admin Panel | Operational views and forms for the above modules |

## Technology stack

| Area | Planned technology |
| --- | --- |
| Backend | Java 21, Spring Boot 4.1.0, Maven 3.9.16 via project wrapper |
| API | Spring MVC, Bean Validation, DTO mapping |
| Persistence | PostgreSQL, Spring Data JPA, Hibernate, Flyway |
| Messaging | RabbitMQ, Spring AMQP |
| Testing | JUnit 5, AssertJ, Mockito, Spring test slices, Testcontainers |
| Frontend | React, TypeScript |
| Local platform | Docker, Docker Compose |
| Deployment | Kubernetes, Azure Container Registry, AKS, Azure Database for PostgreSQL |
| Operations | Spring Boot Actuator, structured logs, metrics |

The initial `backend-api` versions are recorded in its `pom.xml` and Maven
Wrapper configuration. Versions for dependencies and platforms that have not yet
been introduced will be selected in their implementation Issues and recorded in
architecture decisions when the choice affects the project.

## Repository structure

```text
beautystock-crm/
├── backend-api/
│   ├── mvnw
│   ├── pom.xml
│   └── src/
├── backend-worker/          # planned
│   ├── pom.xml
│   └── src/
├── frontend/                # planned
│   └── src/
├── docker/                  # planned
├── k8s/                     # planned
├── docs/
├── compose.yml              # planned
└── README.md
```

`backend-api` is currently an independent Maven build with its own wrapper. A
root aggregator is intentionally deferred until another component creates a
concrete need for one. See ADR-015 in
[Architecture decisions](docs/DECISIONS.md).

The backend package layout will be package-by-business-module rather than a
single repository-wide `controller/service/repository` split:

```text
com.beautystock.crm
├── salon
├── product
├── inventory
├── order
├── forecasting
├── notification
└── shared
```

Each module will own its API, application, domain, and persistence concerns.
Cross-module access should use explicit application interfaces or events, not
another module's repositories.

## Local development

The minimal `backend-api` skeleton can already be built, tested, and started.
It includes the shared API error response DTOs, but still has no business
endpoints, global exception handler, database connection, or messaging
integration.

Prerequisite: JDK 21.

```bash
cd backend-api
./mvnw clean verify
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

The API starts on port `8080` by default. Set `SERVER_PORT` to override it.
PostgreSQL, RabbitMQ, the worker, and the frontend will be added in their
respective Issues. See [Local development](docs/LOCAL_DEVELOPMENT.md).

## Current status

**Phase 1 — Backend API foundation.**

- The project and GitHub delivery foundation is published.
- The repository owner created the Spring Boot `backend-api` skeleton, merged it
  through [PR #72](https://github.com/AdrianJasieniecki/beautystock-crm/pull/72),
  and completed Issue #7.
- The Java 21 build, context test, local-profile startup, and HTTP response from
  the running application have been verified.
- The shared immutable API error response model was implemented and verified in
  [PR #75](https://github.com/AdrianJasieniecki/beautystock-crm/pull/75),
  completing
  [Issue #9](https://github.com/AdrianJasieniecki/beautystock-crm/issues/9).
- Business endpoints, persistence, messaging, containers, and the frontend have
  not been implemented yet.
- The next selected task is
  [Issue #8 — global exception handling](https://github.com/AdrianJasieniecki/beautystock-crm/issues/8).
  It is `Ready`; its dependency on the API error model is resolved, and
  completing it will unblock validation mapping in Issue #10.

See [Progress](docs/PROGRESS.md) for the current source of truth.

## Documentation

- [Architecture](docs/ARCHITECTURE.md)
- [Roadmap](docs/ROADMAP.md)
- [Backlog](docs/BACKLOG.md)
- [GitHub Issue import package](docs/GITHUB_ISSUES.md)
- [Architecture decisions](docs/DECISIONS.md)
- [REST API plan](docs/API.md)
- [Progress](docs/PROGRESS.md)
- [Review checklist](docs/REVIEW_CHECKLIST.md)
- [GitHub workflow](docs/GITHUB_WORKFLOW.md)
- [Local development](docs/LOCAL_DEVELOPMENT.md)
- [Deployment plan](docs/DEPLOYMENT_PLAN.md)

## Contribution model

1. Start from a GitHub Issue with acceptance criteria.
2. The repository owner creates and implements the task branch.
3. Keep changes small and update relevant documentation.
4. Open a PR to `Production` with tests and self-review.
5. Codex reviews the implementation but does not silently rewrite it.
6. The owner decides when to merge and when to close the Issue.

Detailed rules are in [GitHub workflow](docs/GITHUB_WORKFLOW.md).
