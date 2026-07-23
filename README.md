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
| Backend | Java 21, Spring Boot, Maven |
| API | Spring MVC, Bean Validation, DTO mapping |
| Persistence | PostgreSQL, Spring Data JPA, Hibernate, Flyway |
| Messaging | RabbitMQ, Spring AMQP |
| Testing | JUnit 5, AssertJ, Mockito, Spring test slices, Testcontainers |
| Frontend | React, TypeScript |
| Local platform | Docker, Docker Compose |
| Deployment | Kubernetes, Azure Container Registry, AKS, Azure Database for PostgreSQL |
| Operations | Spring Boot Actuator, structured logs, metrics |

Exact dependency and platform versions will be selected in the implementation
issues and recorded in architecture decisions. No application skeleton has been
generated yet.

## Proposed repository structure

```text
beautystock-crm/
├── backend-api/
│   ├── pom.xml
│   └── src/
├── backend-worker/
│   ├── pom.xml
│   └── src/
├── frontend/
│   └── src/
├── docker/
├── k8s/
├── docs/
├── compose.yml
└── README.md
```

The proposed backend package layout is package-by-business-module rather than a
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

The application is not runnable yet. The planned local workflow is:

1. install Java 21, Maven, Node.js, and Docker;
2. start PostgreSQL and RabbitMQ with Docker Compose;
3. run Flyway migrations through the backend;
4. start `backend-api` using the `local` Spring profile;
5. start `backend-worker`;
6. start the React development server.

Commands, ports, environment variables, and verification steps will be added when
the corresponding skeleton and Compose issues are implemented. See
[Local development](docs/LOCAL_DEVELOPMENT.md).

## Current status

**Phase 0 — project foundation and documentation.**

- Architecture, roadmap, backlog, API contract, review rules, and deployment plan
  are being established.
- Application code and infrastructure configuration have not been generated.
- The next implementation task is the Spring Boot `backend-api` skeleton, performed
  manually by the repository owner after the documentation PR is reviewed.

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
