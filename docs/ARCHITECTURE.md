# Architecture

## 1. Purpose

BeautyStock CRM is a CRM and mini-ERP for a small beauty-products wholesaler. It
supports salon relationships, a product catalogue, warehouse stock, orders,
simple demand recommendations, and asynchronous notifications.

The architecture optimizes for:

- learning core backend concepts before introducing distributed complexity;
- explicit business-module boundaries;
- transactional consistency for ordering and inventory;
- incremental delivery with small, reviewable pull requests;
- a credible path from local development to a portfolio-ready cloud deployment.

## 2. Architecture style

The initial solution is a **modular monolith** in `backend-api`, supported by an
optional separately deployable `backend-worker`.

- The API is one Spring Boot application and one primary PostgreSQL database.
- Business capabilities are separated into packages/modules with explicit
  ownership.
- Synchronous use cases execute inside the API process.
- RabbitMQ carries asynchronous integration events.
- The worker consumes events and performs notification-related work.

This is not a distributed microservice architecture. A module boundary is a code
and ownership boundary first; extraction into a service is a later decision based
on evidence.

## 3. Component diagram

```text
┌──────────────────────┐
│ React Admin Panel    │
│ frontend             │
└──────────┬───────────┘
           │ HTTPS / JSON REST
           ▼
┌───────────────────────────────────────────────────────────┐
│ Spring Boot API — backend-api                             │
│                                                           │
│  ┌────────┐ ┌─────────┐ ┌───────────┐ ┌────────┐         │
│  │ Salon  │ │ Product │ │ Inventory │ │ Orders │         │
│  └────────┘ └─────────┘ └───────────┘ └────────┘         │
│  ┌─────────────┐ ┌──────────────┐ ┌───────────────────┐  │
│  │ Forecasting │ │ Notification │ │ Shared technical  │  │
│  └─────────────┘ └──────────────┘ │ capabilities      │  │
│                                    └───────────────────┘  │
└──────────┬───────────────────────────────┬────────────────┘
           │ SQL / transactions            │ AMQP events
           ▼                               ▼
┌──────────────────────┐       ┌───────────────────────────┐
│ PostgreSQL           │       │ RabbitMQ                  │
│ source of truth      │       │ exchange / queues / DLQ   │
└──────────────────────┘       └─────────────┬─────────────┘
                                             │ AMQP delivery
                                             ▼
                                  ┌───────────────────────────┐
                                  │ Spring Boot Worker        │
                                  │ backend-worker            │
                                  │ idempotent notification   │
                                  │ processing                │
                                  └─────────────┬─────────────┘
                                                │ SQL if required
                                                ▼
                                  ┌───────────────────────────┐
                                  │ PostgreSQL                │
                                  │ notifications / inbox     │
                                  └───────────────────────────┘
```

## 4. Repository structure

```text
beautystock-crm/
├── backend-api/
│   ├── mvnw
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/beautystock/crm/
│       │   └── resources/
│       │       └── db/migration/
│       └── test/
├── backend-worker/          # planned
│   ├── pom.xml
│   └── src/
├── frontend/                # planned
│   ├── package.json
│   └── src/
├── docker/                  # planned
├── k8s/                     # planned
├── docs/
├── compose.yml              # planned
└── README.md
```

`backend-api` is an independent Maven build with its own wrapper. The repository
does not have a root Maven parent or aggregator at this stage. This keeps the
first deployable component simple and can be revisited when `backend-worker` or
shared build configuration creates a concrete need; see ADR-015 in
[Architecture decisions](DECISIONS.md).

The remaining application and infrastructure directories appear when their owner
implements the corresponding Issues.

## 5. Backend module structure

The exact package layout will be validated during implementation. The preferred
shape is package-by-feature:

```text
com.beautystock.crm.<module>
├── api             # controllers, request/response DTOs, API mapping
├── application     # use cases, transaction boundaries, orchestration
├── domain          # business rules, domain types
└── infrastructure  # JPA entities/repositories and external adapters
```

This is a guideline, not a requirement to create four layers for every trivial
module. Introduce a type only when it has a clear responsibility.

### 5.1 Salon CRM

Owns salon identity, contact data, lead/customer lifecycle, and sales notes.
Order history is queried from the Orders module; Salon does not own order rows.

### 5.2 Product Catalog

Owns products, SKU, brand, category, current commercial price, and active status.
Inventory references products by stable product identity and does not change
catalogue data.

### 5.3 Inventory

Owns available/on-hand quantities, reservations, adjustments, stock movements,
and concurrency protection. It must prevent negative stock through both business
logic and an appropriate persistence strategy.

### 5.4 Orders

Owns order headers, items, price snapshots, status transitions, and status
history. It coordinates with Product Catalog and Inventory through explicit
interfaces/use cases.

### 5.5 Demand Forecasting

Reads order and inventory history to produce recommendations. Initial
recommendations are deterministic and explainable, not machine learning.

### 5.6 Notifications

Defines notification intent and status. Publishing and consuming transport
messages is infrastructure; the business fact remains explicit.

### 5.7 Shared

Contains only proven cross-cutting technical concepts, such as API error support
or time abstractions. It must not become a miscellaneous business-logic package.

## 6. Module boundaries

1. A module owns its tables and persistence repositories.
2. One module must not inject another module's JPA repository.
3. Cross-module synchronous calls use an explicit application-facing interface.
4. Asynchronous communication uses versioned events.
5. REST DTOs and JPA entities do not cross module boundaries.
6. Shared database access is an implementation detail, not permission to create
   arbitrary joins and object graphs across all modules.
7. Foreign keys may protect relational integrity, but ownership of changes remains
   with the owning module.
8. Cyclic dependencies are not allowed.

The project may later use Spring Modulith for automated boundary verification,
but it is a stretch goal rather than a Phase 1 prerequisite.

## 7. HTTP request flow

Example: `POST /api/v1/salons`.

```text
HTTP request
  → Controller parses JSON and triggers Bean Validation
  → Request DTO is mapped to an application command
  → Application service starts the use-case transaction
  → Domain/business rules are evaluated
  → Repository persists a new entity
  → Database constraints provide final integrity protection
  → Entity/result is mapped to a response DTO
  → Controller returns status, headers, and JSON
```

Responsibilities:

- controllers stay thin and contain transport concerns;
- services/use cases own business orchestration and transaction boundaries;
- managed JPA entities are updated through load → modify → commit;
- API responses never expose JPA entities;
- expected failures are mapped by a global `@RestControllerAdvice`;
- database constraints back up application-level checks.

## 8. JPA and transaction model

- Write use cases are transactional at the application-service boundary.
- Updates load an entity in the transaction, modify the managed instance, and rely
  on dirty checking at flush/commit.
- `save()` is mainly used for new aggregates; detached entity reconstruction from
  request data is not the default update strategy.
- Flush is forced only when the use case needs an early database guarantee.
- Lazy associations are not traversed accidentally by API serialization.
- List endpoints use deliberate fetch plans or DTO projections to avoid N+1.
- `CascadeType.ALL` is not a default.
- Relations are excluded from unsafe `toString`, `equals`, and `hashCode`.
- Inventory concurrency starts with an explicitly tested optimistic-locking
  strategy, likely `@Version`, and may evolve if evidence requires another model.

## 9. Event flow

Example: an order is placed.

```text
Place order transaction
  → validate order transition and items
  → reserve inventory
  → persist order and status history
  → create OrderPlaced event intent
  → commit database transaction
  → publish versioned event to RabbitMQ
  → worker receives delivery
  → validate envelope and event version
  → check idempotency key/eventId
  → create or simulate notification
  → acknowledge delivery
```

Event envelope:

- `eventId`;
- `eventType`;
- `eventVersion`;
- `occurredAt`;
- `aggregateId`;
- `correlationId` where available;
- `payload`.

### 9.1 Delivery guarantees

RabbitMQ delivery is treated as at-least-once. Consumers must tolerate redelivery.
Retry and dead-letter behaviour will be explicit and observable.

The first portfolio increment may publish after the database transaction, which
creates a documented dual-write risk. The project must not pretend this is
atomic. An Outbox Pattern is planned after the basic messaging flow is understood
and tested.

## 10. Data ownership and integrity

Planned high-value constraints include:

- unique normalized salon email where required by confirmed business rules;
- unique product SKU;
- non-negative price and quantity checks;
- order item quantity greater than zero;
- valid status values represented consistently;
- foreign keys for required references;
- optimistic version column for concurrent inventory changes.

Indexes will follow verified query patterns, for example:

- salon status and normalized email;
- product SKU, active status, brand/category filters;
- order salon ID, status, and creation date;
- stock movement product ID and timestamp;
- event/notification processing ID for idempotency.

Every index must have a query or constraint rationale. Indexes are not added
speculatively to every column.

## 11. API boundary

- JSON REST under `/api/v1`.
- Request and response DTOs are separate from entities.
- Validation errors and business errors use one error contract.
- The shared immutable `ApiErrorResponse` and `ApiFieldViolation` transport types
  are implemented; global exception-to-response mapping remains planned.
- Pagination is mandatory for potentially unbounded collections.
- Filters are explicit query parameters.
- Timestamps use ISO 8601 and UTC in transport/storage unless a requirement says
  otherwise.
- Breaking API changes require a versioning decision.

See [API plan](API.md).

## 12. Runtime configuration

- Configuration comes from environment variables and profile-specific files.
- `local`, `test`, and production-like profiles have explicit purposes.
- Secrets are never committed.
- Production secrets are injected by the platform.
- Health endpoints distinguish liveness from readiness.
- Logs are structured and include correlation/event identifiers where useful.

## 13. Why not microservices first

Starting with microservices would add network failure modes, distributed tracing,
service discovery, multiple deployments, data ownership complexity, and eventual
consistency before the core domain and transaction model are understood.

A modular monolith provides:

- fast local feedback;
- simpler transactional consistency;
- easier refactoring while requirements are still changing;
- lower infrastructure cost;
- enough architectural discipline to demonstrate module boundaries.

Extraction is justified only when a module needs independent scaling, release
cadence, fault isolation, ownership, or technology. The notification worker is the
first deliberate process boundary because asynchronous consumption naturally
benefits from separate scaling and failure handling.

## 14. Quality attributes

| Attribute | Initial target |
| --- | --- |
| Correctness | Constraints, transaction tests, status-transition tests |
| Maintainability | Feature modules, thin controllers, explicit ownership |
| Reliability | Idempotent consumers, retry/DLQ plan, health checks |
| Performance | Pagination, query review, N+1 checks, indexes from evidence |
| Security | No committed secrets, validated input, safe error output |
| Observability | Structured logs, Actuator, metrics, correlation IDs |
| Portability | Container images, Compose, Kubernetes-ready configuration |

## 15. Deferred decisions

- versions for dependencies and platforms not yet introduced;
- MapStruct versus manual mapping;
- Spring Modulith adoption;
- authentication and authorization scope;
- exact inventory reservation/locking strategy;
- transactional outbox implementation;
- frontend state/query library;
- Azure network and identity topology.

Deferred choices are resolved in focused Issues and recorded in
[Architecture decisions](DECISIONS.md).
