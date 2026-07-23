# Architecture Decision Records

This file contains concise project-level Architecture Decision Records (ADRs).
Each record is immutable in intent: if a decision changes, add a superseding
record rather than rewriting history. Minor clarifications may be edited.

## ADR-001 — Start with a modular monolith

- **Status:** Accepted
- **Date:** 2026-07-23

### Context

BeautyStock CRM includes several business capabilities, but the domain, workload,
and operational boundaries are not yet proven. Starting with independently
deployed microservices would introduce distributed transactions, network failure,
service discovery, separate pipelines, and observability overhead before the
owner has implemented the core workflows.

### Decision

Build `backend-api` as a modular monolith organized by business capability. Keep
module ownership explicit and prevent cross-module repository access. Introduce
`backend-worker` as a separate process when RabbitMQ consumption starts.

### Consequences

- local development and transactional consistency are simpler;
- refactoring remains inexpensive while requirements evolve;
- module discipline must be enforced through review and possibly future automated
  architecture tests;
- a shared database can tempt accidental coupling;
- services may be extracted later only with evidence such as independent scaling,
  release cadence, ownership, or fault-isolation needs.

## ADR-002 — Use RabbitMQ before Kafka

- **Status:** Accepted
- **Date:** 2026-07-23

### Context

The first messaging needs are work queues, notification processing, retries, and
dead-letter handling. The project needs to learn delivery acknowledgements,
redelivery, idempotency, and failure topology before adding a distributed event
stream platform.

### Decision

Use RabbitMQ and Spring AMQP for the first asynchronous flow. Kafka may be studied
later as a comparison, not as a replacement without a concrete requirement.

### Consequences

- queue semantics match the notification-worker use case;
- local setup is relatively approachable;
- event envelope versioning and consumer idempotency are still required;
- RabbitMQ-specific topology must remain infrastructure, not domain logic;
- Kafka concepts such as partitions and retained logs are intentionally deferred.

## ADR-003 — Use PostgreSQL as the primary database

- **Status:** Accepted
- **Date:** 2026-07-23

### Context

The project requires transactions, constraints, indexes, relational history, and
concurrency behaviour representative of production systems.

### Decision

Use PostgreSQL for local, integration-test, and deployed persistence. Avoid using
H2 as a behavioural substitute for PostgreSQL.

### Consequences

- SQL and constraint behaviour remain consistent across environments;
- Testcontainers adds startup cost but improves fidelity;
- PostgreSQL-specific features may be used only with an explicit rationale;
- local development requires Docker or a compatible PostgreSQL installation.

## ADR-004 — Use Flyway for schema migrations

- **Status:** Accepted
- **Date:** 2026-07-23

### Context

Database schema changes must be reviewable, repeatable, and independent of
Hibernate's automatic schema mutation.

### Decision

Use versioned Flyway SQL migrations. Production-like profiles must not rely on
`ddl-auto=create` or `update`. Hibernate schema validation may be enabled after
migrations.

### Consequences

- schema history is explicit in Git;
- constraint and index changes receive normal review;
- merged migrations are normally forward-only and should not be casually edited;
- migration ordering and multi-replica execution require a deployment strategy.

## ADR-005 — Use React and TypeScript for the admin frontend

- **Status:** Accepted
- **Date:** 2026-07-23

### Context

The project needs a simple operational interface and an opportunity to learn
frontend fundamentals without making design the primary goal.

### Decision

Use React with TypeScript for the admin panel. Prioritize typed API integration,
routing, forms, and complete UI states over advanced visual design.

### Consequences

- backend flows can be demonstrated without a separate API client;
- TypeScript makes API assumptions visible;
- frontend scope must be controlled so it does not delay backend learning;
- backend remains the source of truth for business validation.

## ADR-006 — Use Docker Compose before Kubernetes

- **Status:** Accepted
- **Date:** 2026-07-23

### Context

Kubernetes is not useful learning evidence if the application cannot first run
reliably as local processes and containers.

### Decision

Establish a working local environment with Docker Compose before creating
Kubernetes manifests. Compose will include PostgreSQL, RabbitMQ, API, worker, and
optionally frontend when those components exist.

### Consequences

- runtime configuration and health requirements are discovered early;
- container images can be tested before orchestration;
- Kubernetes remains a later, focused learning phase;
- Compose startup ordering must not be mistaken for dependency readiness.

## ADR-007 — The repository owner implements learning tasks

- **Status:** Accepted
- **Date:** 2026-07-23

### Context

The project's purpose is practical learning and portfolio evidence. Automatically
generating complete features would reduce the owner's opportunity to reason about
design, persistence, tests, and debugging.

### Decision

The repository owner manually implements business logic, endpoints, entities,
services, repositories, tests, and infrastructure configuration. Codex acts as
architect, mentor, backlog maintainer, and reviewer. Codex provides full
implementation only after an explicit request such as “implement it” or “write
the code.”

### Consequences

- progress may be slower but produces stronger learning evidence;
- Issues must contain actionable guidance without becoming copy-paste solutions;
- review feedback explains causes and trade-offs before any automated fix;
- the owner controls merging and Issue closure.

## ADR-008 — Keep API DTOs separate from JPA entities

- **Status:** Accepted
- **Date:** 2026-07-23

### Context

JPA entities reflect persistence lifecycle and relationships. REST contracts have
different validation, compatibility, security, and presentation concerns.

### Decision

Use explicit request and response DTOs at API boundaries. Never return JPA
entities directly from controllers and never use request DTOs as detached entity
replacement objects.

### Consequences

- mapping code is required;
- API evolution is decoupled from schema refactoring;
- lazy-loading and recursion are less likely to leak into serialization;
- update use cases can deliberately load and modify managed entities.

## ADR-009 — Put transaction boundaries at application use cases

- **Status:** Accepted
- **Date:** 2026-07-23

### Context

Order and inventory operations require multiple checks and persistence changes to
succeed or fail as one unit.

### Decision

Place transactions on application-service/use-case methods. For updates, load the
aggregate within the transaction, modify the managed entity, and rely on dirty
checking at flush/commit. Use repository `save()` mainly to persist new entities
or when merge semantics are consciously required.

### Consequences

- persistence-context behaviour is intentional and testable;
- controllers do not own transactions;
- self-invocation and transaction propagation need review;
- remote calls should not be casually performed inside long database
  transactions.

## ADR-010 — Use optimistic locking for the first inventory concurrency model

- **Status:** Proposed
- **Date:** 2026-07-23

### Context

Concurrent reservations can cause lost updates or overselling. Expected contention
for a small educational wholesaler is not yet known.

### Decision

Start by evaluating an optimistic-locking version column on the inventory
aggregate and implement realistic concurrency integration tests. Confirm the
decision only after defining reservation semantics and failure handling.

### Consequences

- uncontended updates remain simple;
- callers must receive or safely retry an explicit concurrency conflict;
- blind retries may be incorrect if availability changed;
- pessimistic locking or atomic SQL updates remain alternatives if tests or
  measured contention justify them.

## ADR-011 — Treat message delivery as at-least-once

- **Status:** Accepted
- **Date:** 2026-07-23

### Context

RabbitMQ consumers may receive the same logical event more than once because of
redelivery, acknowledgement loss, retry, or operator action.

### Decision

Consumers must be idempotent for side effects that cannot safely repeat. Use
`eventId` as a stable processing key, define retryable versus terminal failures,
and route exhausted/invalid messages to a dead-letter queue.

### Consequences

- consumer storage or a naturally idempotent operation is required;
- exactly-once delivery is not claimed;
- failed-message operations need monitoring and replay procedures;
- idempotency retention and cleanup need a later decision.

## ADR-012 — Accept a documented initial dual-write risk, then add Outbox

- **Status:** Proposed
- **Date:** 2026-07-23

### Context

Saving an order and publishing `OrderPlaced` to RabbitMQ are two separate systems.
Publishing before commit may notify about rolled-back data; publishing after
commit may lose an event if the process fails between the two operations.

### Decision

For the first messaging learning increment, a simple after-commit publication may
be used only if its loss window is documented, tested as far as practical, and
not represented as reliable atomic delivery. Add a transactional Outbox Pattern
as an advanced follow-up.

### Consequences

- the first RabbitMQ flow is easier to understand;
- a known event-loss risk remains temporarily;
- the portfolio demonstrates awareness rather than false guarantees;
- Outbox requires schema, relay, retries, cleanup, and observability work.

## ADR-013 — Use one consistent API error contract

- **Status:** Proposed
- **Date:** 2026-07-23

### Context

Validation, malformed input, missing resources, conflicts, and unexpected
failures should be predictable for frontend and API clients.

### Decision

Define one JSON error contract with a stable machine-readable code, human-readable
message, HTTP status, timestamp, request path, correlation identifier, and
optional field violations. Map errors through `@RestControllerAdvice`.

### Consequences

- controllers remain focused on HTTP success paths;
- exception-to-status mapping must be maintained deliberately;
- internal stack traces and sensitive data must not appear in responses;
- the exact field names are finalized in the Backend API foundation Issue.

## ADR-014 — Store time in UTC and expose ISO 8601

- **Status:** Proposed
- **Date:** 2026-07-23

### Context

Orders, events, histories, and forecasts depend on consistent timestamps. The
project owner works in Europe/Warsaw, which includes daylight-saving changes.

### Decision

Use UTC instants for technical occurrence timestamps and ISO 8601 in APIs. Model
business dates or salon-local schedules separately when the requirement needs a
calendar date rather than an instant.

### Consequences

- event ordering and log correlation are clearer;
- frontend performs display-zone conversion;
- forecasts must distinguish durations, instants, and local dates;
- tests should use an injectable `Clock` for time-dependent logic.

