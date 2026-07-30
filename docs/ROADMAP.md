# Roadmap

## 1. Delivery principles

- Build a vertical, testable slice before broadening scope.
- Learn one major infrastructure concept at a time.
- Keep application implementation owned by the repository owner.
- Do not move to Kubernetes before the local application works in Compose.
- Do not hide architectural risks; record trade-offs and revisit them.
- Prefer portfolio evidence: runnable flows, meaningful tests, diagrams, and
  documented decisions.

## 2. Milestones

### Phase 0 — Foundation and documentation

**Status:** Complete.

**Goal:** establish project boundaries, working agreements, architecture, API
conventions, backlog, and review criteria.

Deliverables:

- documentation set under `docs/`;
- GitHub labels, epics, stories, and technical tasks;
- documentation PR to `Production`;
- clear next task and manual implementation workflow.

Exit criteria:

- documentation is reviewed and merged by the owner;
- priority and dependencies are visible;
- no application code was generated as part of this phase.

### Phase 1 — Backend API foundation

**Status:** In progress. The Java 21/Spring Boot skeleton, shared API error
response model, global exception handling, and request-body Bean Validation are
complete. PostgreSQL, Flyway, and the first Testcontainers foundation are also
complete. The health endpoint remains in this phase, while the first Salon CRM
vertical slice can start independently.

**Goal:** create a minimal, maintainable Spring Boot service ready for feature
development.

Deliverables:

- Java 21/Maven `backend-api` skeleton;
- environment profiles and configuration conventions;
- consistent API error contract and global exception handling;
- Bean Validation;
- PostgreSQL and Flyway connection;
- first Testcontainers foundation;
- health endpoint.

Learning focus:

- Spring Boot structure;
- dependency selection;
- request lifecycle;
- validation and exception mapping;
- test pyramid and test slices.

Exit criteria:

- application starts locally;
- migrations run on an empty database;
- smoke/health tests pass;
- no secrets are committed.

### Phase 2 — Salon CRM vertical slice

**Goal:** deliver the first complete CRUD-oriented business slice.

Deliverables:

- create, list/filter, view, and update salon;
- salon lifecycle status;
- sales notes;
- DTO mapping, validation, constraints, pagination;
- unit, repository integration, and controller/API tests.

Learning focus:

- JPA entity lifecycle;
- managed updates and dirty checking;
- service transactions;
- database uniqueness versus application validation;
- thin controllers and API DTOs.

Exit criteria:

- salon flows meet their acceptance criteria;
- duplicate and invalid inputs have stable error responses;
- queries are checked for N+1;
- documentation reflects actual endpoints.

### Phase 3 — Product Catalog

**Goal:** create a manageable product catalogue used by inventory and orders.

Deliverables:

- create/list/filter products;
- SKU uniqueness;
- price update with an explicit money representation;
- deactivation instead of destructive deletion;
- brand/category modelling decision.

Learning focus:

- schema modelling and indexes;
- value validation;
- controlled status changes;
- filtering and pagination.

Exit criteria:

- active/inactive behaviour is tested;
- price and SKU integrity is protected in the database;
- API models remain separate from entities.

### Phase 4 — Inventory

**Goal:** represent stock safely, including concurrent changes.

Deliverables:

- current inventory view;
- stock adjustments and immutable movement history;
- order reservation and release;
- non-negative stock invariant;
- optimistic locking and concurrency tests.

Learning focus:

- transaction boundaries;
- optimistic locking;
- race conditions;
- audit/history models;
- integration tests against PostgreSQL.

Exit criteria:

- overselling is prevented in tested concurrent scenarios;
- every change creates an explainable movement;
- reservations can be traced to an order.

### Phase 5 — Orders (MVP completion)

**Goal:** implement the primary business workflow from draft to placed order.

Deliverables:

- create draft order;
- add/change items;
- place and cancel order;
- stock reservation/release;
- order status history;
- order detail view and meaningful errors.

Learning focus:

- aggregate boundaries;
- state machines and invariants;
- multi-module orchestration;
- price snapshots;
- transaction rollback.

Exit criteria:

- a salon can create and place a valid order;
- invalid transitions are rejected;
- stock remains consistent on success and rollback;
- the main flow is covered by integration tests.

**MVP boundary:** Phases 0–5 plus a minimal runnable local environment.

### Phase 6 — RabbitMQ worker and notifications

**Goal:** add an observable asynchronous flow without hiding delivery risks.

Deliverables:

- RabbitMQ in local Compose;
- versioned event envelope;
- `OrderPlaced` publisher;
- separate worker consumer;
- idempotency approach;
- retry and dead-letter plan;
- notification record or deterministic email simulation.

Learning focus:

- exchanges, queues, bindings, acknowledgements;
- at-least-once delivery;
- redelivery and poison messages;
- dual-write risk.

Exit criteria:

- duplicate delivery does not create an incorrect duplicate effect;
- failure/retry/DLQ behaviour is tested with RabbitMQ Testcontainers;
- event contract is documented.

### Phase 7 — Demand forecasting

**Goal:** add simple, explainable recommendations based on real project data.

Deliverables:

- average product consumption per salon or time window;
- expected reorder date;
- low-stock recommendations;
- documented calculation assumptions.

Learning focus:

- analytical SQL or deliberate application aggregation;
- date/time modelling;
- deterministic algorithm tests;
- communicating uncertainty.

Exit criteria:

- recommendations are reproducible from sample history;
- edge cases with insufficient data are explicit;
- this module does not mutate orders automatically.

### Phase 8 — Frontend admin panel

**Goal:** provide a simple but usable interface for the main workflows.

Deliverables:

- routing and application shell;
- dashboard;
- salon list/details/form;
- product list;
- order creation/details;
- notifications list;
- loading, empty, success, validation, and server-error states.

Learning focus:

- React components, props, state, routing, and forms;
- TypeScript API models;
- REST integration;
- usable operational feedback.

Exit criteria:

- the MVP flow can be demonstrated without an API client;
- validation and error states are visible;
- frontend does not duplicate backend business rules as source of truth.

### Phase 9 — Docker Compose environment

**Goal:** run the complete local system predictably.

Deliverables:

- multi-stage images for API, worker, and frontend;
- Compose services for PostgreSQL and RabbitMQ;
- health checks and dependency readiness;
- example environment configuration;
- persistent local volumes and reset instructions.

Exit criteria:

- a documented command starts the system from a clean checkout;
- no real secrets are in images or repository files;
- service health is observable.

### Phase 10 — Observability and JVM diagnostics

**Goal:** make runtime behaviour explainable.

Deliverables:

- Actuator health and metrics;
- structured logging and correlation identifiers;
- selected Micrometer metrics;
- JVM notes for GC logs, heap dump, thread dump, and basic analysis;
- one documented diagnostic exercise.

Exit criteria:

- a failing dependency is visible in readiness;
- an order/event can be traced through logs;
- diagnostic commands and safe dump handling are documented.

### Phase 11 — Kubernetes

**Goal:** translate a stable containerized system to Kubernetes primitives.

Deliverables:

- Deployments and Services;
- ConfigMaps and Secret templates;
- Ingress;
- liveness/readiness probes;
- resource requests/limits;
- migration strategy;
- local cluster validation.

Exit criteria:

- manifests deploy to a clean local cluster or documented test environment;
- secrets are external to committed values;
- rollout and rollback steps are known.

### Phase 12 — Azure deployment

**Goal:** deploy a portfolio demonstration using managed cloud services.

Deliverables:

- Azure Container Registry;
- AKS or a consciously reconsidered simpler compute option;
- Azure Database for PostgreSQL;
- secret and configuration integration;
- logging/metrics path;
- cost and teardown plan.

Exit criteria:

- deployment is reproducible from documentation;
- health and logs are accessible;
- public exposure is secured appropriately;
- resources can be stopped or deleted to control cost.

### Phase 13 — Portfolio polish

**Goal:** present engineering decisions and working evidence clearly.

Deliverables:

- architecture diagram;
- screenshots or short demo;
- representative API examples;
- test strategy summary;
- trade-offs and lessons learned;
- final README navigation;
- issue/PR history with clean, reviewable increments.

Exit criteria:

- a reviewer can understand and run the project;
- the main business flow is demonstrable;
- known limitations are honest;
- repository history shows incremental ownership.

**Portfolio-ready boundary:** Phases 0–10 and a credible deployment demonstration
from Phase 11 or 12.

## 3. Dependency chain

```text
Documentation
  → Backend foundation
    → Salon CRM
    → Product Catalog
      → Inventory
        → Orders
          → RabbitMQ notifications
          → Demand forecasting
          → Frontend main flow
            → Complete Compose environment
              → Observability
                → Kubernetes
                  → Azure
                    → Portfolio polish
```

Some frontend foundation work may start after stable Salon and Product API
contracts exist. Kubernetes and Azure remain intentionally late.

## 4. Stretch goals

- transactional Outbox Pattern with a relay;
- inbox/idempotency table for consumers;
- Spring Modulith boundary verification;
- CSV product import with validation report;
- authentication and role-based authorization;
- OpenAPI generation and contract testing;
- inventory reservation expiration;
- audit log beyond domain status/movement history;
- distributed tracing with OpenTelemetry;
- performance test and query-plan exercise;
- CI/CD pipeline with image scanning and deployment promotion;
- Kafka comparison documented after RabbitMQ is understood.

Stretch goals must not block completion of the core portfolio path.

## 5. Roadmap governance

- `docs/PROGRESS.md` identifies the current phase and next action.
- `docs/BACKLOG.md` is the human-readable scope map.
- GitHub Issues hold executable acceptance criteria and discussion.
- Architecture changes are captured in `docs/DECISIONS.md`.
- The owner approves merges, Issue closure, and roadmap scope changes.
