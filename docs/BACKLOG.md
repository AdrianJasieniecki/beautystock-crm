# Backlog

## 1. Purpose and sources of truth

This document is the portfolio-level backlog map. GitHub Issues are the source of
truth for executable scope, acceptance criteria, discussion, and implementation
status. `docs/PROGRESS.md` identifies the current focus.

Application implementation belongs to the repository owner. Codex prepares and
reviews work unless the owner explicitly asks for code.

## 2. Status and priority model

### Statuses

| Status | Meaning |
| --- | --- |
| Proposed | Captured but not yet refined or approved |
| Ready | Acceptance criteria and dependencies are clear |
| In progress | Owner is actively implementing the Issue |
| In review | Pull request is open |
| Blocked | Progress requires a resolved dependency or decision |
| Done | Definition of Done is met and the owner approved Issue closure |

### Priorities

| Priority | Meaning |
| --- | --- |
| High | Required for the next usable vertical slice or protects correctness |
| Medium | Important for portfolio completeness or maintainability |
| Low | Valuable enhancement that must not block the main learning path |

## 3. Global Definition of Done

An implementation Issue is Done only when:

- code compiles and relevant automated tests pass;
- the feature meets every acceptance criterion;
- REST endpoints expose DTOs, never JPA entities;
- errors follow the shared API error contract;
- transaction and persistence-context behaviour are deliberate;
- database integrity is protected with appropriate migrations and constraints;
- meaningful tests cover success and important failure paths;
- no secrets or environment-specific credentials are committed;
- relevant documentation is updated;
- the Issue contains a completion summary;
- the PR contains a description and self-review;
- review has no blocking findings;
- the repository owner decides to merge and close the Issue.

Documentation-only Issues adapt the code/test clauses to link, content, and
consistency checks.

## 4. Dependency overview

| Epic | Depends on | Unlocks |
| --- | --- | --- |
| Project foundation | None | All other epics |
| Backend API foundation | Project foundation | Backend business modules |
| Salon CRM | Backend foundation | Orders, frontend salon flow |
| Product Catalog | Backend foundation | Inventory, orders, frontend products |
| Inventory | Product Catalog | Placing/cancelling orders, forecasting |
| Orders | Salon CRM, Product Catalog, Inventory | Events, forecasting, frontend order flow |
| RabbitMQ worker | Orders, local RabbitMQ | Notifications |
| Demand forecasting | Orders, Inventory | Dashboard recommendations |
| Frontend foundation | Stable API slices | Admin panel |
| Docker Compose | Runnable components | Repeatable local system, Kubernetes |
| Observability | Runnable API/worker | Production readiness |
| Kubernetes | Stable images and Compose | Cloud deployment |
| Cloud deployment | Kubernetes/deployment decision | Public demonstration |
| Portfolio polish | Evidence from completed features | Portfolio-ready release |

## 5. Epics

### Epic 1 — Project foundation and documentation

**Business description:** Establish a shared understanding of what BeautyStock CRM
does, how work is selected, and how quality is evaluated.

**Technical goal:** Create the documentation, architecture decisions, backlog, and
GitHub workflow needed for small manual implementation increments.

**Scope:**

- repository purpose and module map;
- architecture and API conventions;
- roadmap, backlog, progress tracking, and review checklist;
- local/deployment plans;
- Issue labels and initial Issues.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Create documentation foundation | Task | High | Done | None |
| Define GitHub workflow and review checklist | Task | High | Done | None |
| Prepare initial architecture decision records | Task | High | Done | None |

Issues #3, #4, and #5 and their parent Epic #2 are closed and `Done`.

**Acceptance criteria:**

- all requested documentation files exist and link to one another;
- the proposed repository and module structure is explicit;
- the owner/Codex responsibility boundary is documented;
- the initial Issues are consistent with the backlog;
- a documentation PR targets `Production`;
- no application or infrastructure implementation is included.

**Risks:**

- over-planning may delay the first vertical slice;
- documentation may drift from implementation;
- scope may become too broad for an educational project.

**Definition of Done:** The owner reviews and merges the documentation PR, the
first implementation Issue is selected, and future changes update the relevant
source of truth.

### Epic 2 — Backend API foundation

**Business description:** Provide a stable technical base on which business
features can be delivered consistently.

**Technical goal:** Create a Java 21/Spring Boot/Maven API skeleton with validation,
error handling, PostgreSQL, Flyway, testing foundations, and configuration rules.

**Scope:**

- application skeleton and package conventions;
- API error model and global exception handling;
- request validation example;
- PostgreSQL/Flyway local and test configuration;
- baseline health check and test strategy.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Generate Spring Boot backend-api skeleton | Task | High | Done | Epic 1 |
| Add API error response model | Task | High | Done | Skeleton |
| Add global exception handling contract | Task | High | Done | API error model |
| Add validation dependency and example request validation | Task | High | Done | Global handler and API error model |
| Add PostgreSQL and Flyway configuration | Task | High | Done | Skeleton |

The API error model, global exception handling, and request-body Bean Validation
dependencies are complete. PostgreSQL, Flyway, and Testcontainers were completed
through Issue #11 and PR #82. The health endpoint remains a separate foundation
task and does not block the first Salon CRM story.

**Acceptance criteria:**

- Java 21 build succeeds using the chosen Maven structure;
- application starts with an explicit local profile;
- validation and errors return one documented format;
- Flyway creates a schema in PostgreSQL from an empty database;
- tests use the smallest suitable Spring context;
- configuration contains no secrets.

**Risks:**

- premature generic abstractions;
- H2-specific behaviour hiding PostgreSQL issues;
- unstable dependency choices;
- global error handling that leaks internal details.

**Definition of Done:** A small backend foundation PR is merged after review,
tests pass against PostgreSQL where relevant, and the first Salon story can start.

### Epic 3 — Salon CRM

**Business description:** Store potential and active beauty salons as customers,
including contact data and sales notes.

**Technical goal:** Implement the first complete REST/JPA vertical slice with
DTOs, validation, transactions, pagination, constraints, and meaningful tests.

**Scope:**

- salon identity and contact data;
- lead/active/inactive lifecycle;
- notes;
- create/list/details/update APIs;
- history link to Orders when available.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Create salon profile | Story | High | Ready | Epic 2 |
| List salons with pagination and filtering | Story | High | Proposed | Create salon |
| View salon details | Story | High | Proposed | Create salon |
| Update salon contact data | Story | High | Proposed | View salon |
| Add note to salon | Story | Medium | Proposed | Create salon |

Selected delivery order after the persistence foundation:

1. create salon profile (#13);
2. view salon details (#15);
3. list salons with pagination and filtering (#14);
4. update salon contact data (#16);
5. add a note to a salon (#17).

Issue #13 is the only dependency-ready Salon implementation item and moves to
`Ready`; later stories remain `Proposed` until their prerequisites and business
decisions are confirmed.

**Acceptance criteria:**

- APIs use request/response DTOs and stable HTTP semantics;
- invalid inputs and duplicate business keys return documented errors;
- updates use load → modify managed entity → commit;
- unbounded lists are paginated and filters are indexed when justified;
- tests cover validation, persistence constraints, and main API flows.

**Risks:**

- case-insensitive email uniqueness must be implemented consistently in the
  application and PostgreSQL;
- notes may grow without pagination;
- entities may accidentally leak through serialization;
- eager relationships may create N+1 queries.

**Definition of Done:** All high-priority Salon stories meet global DoD, the module
has a coherent API, and order work can reference a stable salon ID.

### Epic 4 — Product Catalog

**Business description:** Maintain the sellable products offered to salons.

**Technical goal:** Model SKU, commercial data, lifecycle, filtering, and price
changes with database integrity and clear API contracts.

**Scope:**

- products, SKU, name, description;
- brands and categories;
- price and currency decision;
- active/inactive lifecycle;
- later CSV import.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Create product | Story | High | Proposed | Epic 2 |
| List products with filtering | Story | High | Proposed | Create product |
| Update product price | Story | High | Proposed | Create product |
| Deactivate product | Story | Medium | Proposed | Create product |
| Import products from CSV | Story | Low | Proposed | Stable catalogue |

**Acceptance criteria:**

- SKU is unique and protected by a database constraint;
- money representation avoids floating-point values;
- deactivated products remain historically referencable;
- list endpoint supports pagination and useful filters;
- price and lifecycle changes are tested.

**Risks:**

- over-modelling brand/category too early;
- using floating point for money;
- changing product price may incorrectly change historical order value;
- hard deletion may break references.

**Definition of Done:** The catalogue provides stable product IDs and price data
for Inventory and Orders, with clear lifecycle behaviour.

### Epic 5 — Inventory

**Business description:** Show available stock and protect the wholesaler from
selling inventory that does not exist.

**Technical goal:** Model on-hand stock, reservations, immutable movements, and
concurrent modifications with explicit transaction behaviour.

**Scope:**

- current stock view;
- manual adjustments;
- movement history;
- reserve and release for orders;
- non-negative invariant;
- optimistic locking.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| View current inventory | Story | High | Proposed | Product Catalog |
| Adjust stock level | Story | High | Proposed | Inventory schema |
| Track stock movements | Story | High | Proposed | Adjust stock |
| Reserve stock for order | Story | High | Proposed | Orders draft model |
| Release reservation on cancellation | Story | High | Proposed | Reserve stock |
| Add optimistic locking concurrency tests | Task | High | Proposed | Reserve stock |

**Acceptance criteria:**

- quantities cannot become negative;
- adjustment and reservation changes are auditable;
- reservation failures roll back the use case;
- concurrent updates have a tested, deterministic outcome;
- APIs distinguish on-hand, reserved, and available quantities.

**Risks:**

- race conditions and lost updates;
- unclear definition of available stock;
- movement history diverging from current balance;
- optimistic-lock retries masking business conflicts.

**Definition of Done:** Inventory integrity holds under tested concurrency and
order placement can reserve stock transactionally.

### Epic 6 — Orders

**Business description:** Let staff prepare, place, track, and cancel salon orders.

**Technical goal:** Implement a status-driven order aggregate coordinating salon,
product, and inventory boundaries safely.

**Scope:**

- statuses `DRAFT`, `PLACED`, `RESERVED`, `CONFIRMED`, `CANCELLED`, `FULFILLED`;
- draft creation and item editing;
- placement and cancellation;
- price snapshots;
- status history;
- order detail and paginated list.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Create draft order | Story | High | Proposed | Salon, Product |
| Add item to draft order | Story | High | Proposed | Draft order |
| Place order | Story | High | Proposed | Inventory reservation |
| Cancel order | Story | High | Proposed | Place order |
| View order details | Story | High | Proposed | Draft order |
| List orders with filters | Story | Medium | Proposed | Order persistence |
| Confirm and fulfil order | Story | Medium | Proposed | Placement stable |

**Acceptance criteria:**

- invalid status transitions are rejected;
- product price is snapshotted on order items;
- placement reserves stock atomically or rolls back;
- cancellation releases eligible reservations exactly once;
- status history is immutable and ordered;
- tests cover success, insufficient stock, invalid transition, and rollback.

**Risks:**

- ambiguous meaning/order of `PLACED`, `RESERVED`, and `CONFIRMED`;
- aggregate becoming too large;
- partial stock reservations;
- double cancellation or duplicate commands.

**Definition of Done:** The MVP order flow works end to end with transactional
inventory consistency and documented state transitions.

### Epic 7 — RabbitMQ worker and notifications

**Business description:** Notify staff or customers about important events without
making the order request wait for notification processing.

**Technical goal:** Introduce versioned events, RabbitMQ topology, a separately
deployable consumer, idempotency, retry, and dead-letter handling.

**Scope:**

- RabbitMQ local service;
- event envelope;
- `OrderPlaced` publication and consumption;
- notification persistence or deterministic email simulation;
- retries, DLQ, idempotency;
- future Outbox Pattern.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Add RabbitMQ to Docker Compose | Task | High | Proposed | Compose foundation |
| Define event envelope contract | Task | High | Proposed | Architecture decision |
| Publish OrderPlaced event | Story | High | Proposed | Place order |
| Consume OrderPlaced event in worker | Story | High | Proposed | Event contract |
| Add retry and dead-letter queue plan | Task | High | Proposed | Consumer |
| Add consumer idempotency mechanism | Task | High | Proposed | Consumer |
| Implement transactional outbox | Task | Low | Proposed | Basic flow stable |

**Acceptance criteria:**

- envelope contains stable metadata and versioned payload;
- duplicate delivery does not produce an incorrect duplicate side effect;
- retryable and terminal failures are distinguished;
- poison messages reach an inspectable DLQ;
- dual-write risk is documented until outbox exists;
- RabbitMQ Testcontainers tests cover representative behaviour.

**Risks:**

- losing an event between database commit and publish;
- infinite retries;
- non-idempotent email/notification effects;
- incompatible event changes.

**Definition of Done:** A placed order produces one logical notification outcome
under normal and duplicate delivery, with observable failure handling.

### Epic 8 — Demand forecasting

**Business description:** Help staff anticipate replenishment and salon demand
using historical orders and current inventory.

**Technical goal:** Implement deterministic, explainable calculations over order
and inventory history.

**Scope:**

- average consumption;
- expected next order date;
- low-stock recommendation;
- insufficient-data handling;
- dashboard/API presentation.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Generate basic demand recommendation | Story | Medium | Proposed | Order history |
| Show low stock recommendation | Story | Medium | Proposed | Inventory + consumption |
| Document forecasting assumptions | Task | Medium | Proposed | Algorithm decision |

**Acceptance criteria:**

- inputs, time window, and formula are documented;
- recommendations are reproducible and explainable;
- missing/insufficient data is not presented as certainty;
- calculations have deterministic unit tests;
- recommendations do not automatically mutate orders or inventory.

**Risks:**

- misleading precision from small datasets;
- timezone/date boundary errors;
- expensive aggregation queries;
- accidental presentation as machine learning.

**Definition of Done:** Users can view tested recommendations with visible
assumptions and limitations.

### Epic 9 — Frontend foundation

**Business description:** Give an admin user a usable interface for the principal
CRM, catalogue, order, and notification workflows.

**Technical goal:** Create a maintainable React + TypeScript application with
routing, typed API calls, forms, and complete UI states.

**Scope:**

- app shell, routing, and API client;
- dashboard;
- salon/product lists and salon form/details;
- order creation/details;
- notification list;
- loading/error/empty/success states.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Generate React TypeScript frontend | Task | Medium | Proposed | API conventions |
| Display salons list in frontend | Story | Medium | Proposed | Salon list API |
| Create salon form in frontend | Story | Medium | Proposed | Create salon API |
| Display products list in frontend | Story | Medium | Proposed | Product list API |
| Create order from frontend | Story | Medium | Proposed | Order MVP API |
| Display order details | Story | Medium | Proposed | Order details API |
| Display notifications | Story | Low | Proposed | Notifications API |

**Acceptance criteria:**

- API models are typed;
- forms expose client validation while backend remains source of truth;
- loading, empty, validation, server error, and success states are visible;
- routing supports the planned views;
- no secrets are bundled into frontend assets.

**Risks:**

- frontend scope distracting from backend goals;
- duplicated business logic;
- ad hoc API calls and inconsistent error handling;
- polishing visual design before flows work.

**Definition of Done:** A reviewer can demonstrate the MVP through the browser,
with readable structure and basic automated checks.

### Epic 10 — Docker Compose environment

**Business description:** Make the full project easy to run for the owner and a
portfolio reviewer.

**Technical goal:** Containerize components and orchestrate local dependencies
with safe configuration and health checks.

**Scope:**

- multi-stage Dockerfiles;
- PostgreSQL and RabbitMQ;
- API and worker;
- optional frontend;
- networks, volumes, health checks, environment examples.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Add Dockerfile for backend-api | Task | Medium | Proposed | Runnable API |
| Add Dockerfile for backend-worker | Task | Medium | Proposed | Runnable worker |
| Add Dockerfile for frontend | Task | Medium | Proposed | Runnable frontend |
| Add compose.yml for local development | Task | High | Proposed | Service images/dependencies |

**Acceptance criteria:**

- a documented workflow starts required services;
- images use multi-stage builds and non-root runtime where practical;
- health checks represent actual readiness;
- real secrets and `.env` files are not committed;
- data persistence and reset behaviour are documented.

**Risks:**

- relying only on startup order rather than readiness;
- oversized images;
- platform-specific assumptions;
- development credentials reused as production secrets.

**Definition of Done:** A clean checkout can start a healthy local environment
using documented configuration.

### Epic 11 — Kubernetes deployment

**Business description:** Demonstrate how the application would run on an
orchestrated platform.

**Technical goal:** Define deployable Kubernetes resources with configuration,
health, resource, networking, and migration concerns handled explicitly.

**Scope:**

- Deployments, Services, Ingress;
- ConfigMaps and Secret templates;
- liveness/readiness probes;
- resource requests/limits;
- database migration strategy;
- local cluster validation.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Add Kubernetes manifests for backend-api | Task | Low | Proposed | Stable API image |
| Add Kubernetes manifests for backend-worker | Task | Low | Proposed | Stable worker image |
| Add ConfigMap and Secret templates | Task | Low | Proposed | Runtime config known |
| Add liveness and readiness probes | Task | Medium | Proposed | Actuator endpoints |
| Add Services and Ingress | Task | Low | Proposed | Deployments |
| Add resource requests and limits | Task | Medium | Proposed | Runtime observations |

**Acceptance criteria:**

- manifests deploy to the chosen validation cluster;
- probes use appropriate endpoints and timing;
- configuration and secrets are separated;
- resources are specified and justified;
- rollout, migration, and rollback are documented.

**Risks:**

- Kubernetes work before application stability;
- secrets committed in base64 and mistaken for encryption;
- unsafe migration execution from every replica;
- probes causing restart loops.

**Definition of Done:** The stable containerized system can be deployed,
observed, and rolled back from documented manifests.

### Epic 12 — Cloud deployment

**Business description:** Make a controlled portfolio demonstration available in
Azure.

**Technical goal:** Plan and execute image registry, compute, managed database,
configuration, observability, security, and cost controls.

**Scope:**

- Azure Container Registry;
- AKS or documented compute reassessment;
- Azure Database for PostgreSQL;
- RabbitMQ hosting decision;
- identities/secrets/networking;
- cost budget and teardown.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Prepare Azure deployment plan | Task | Low | Proposed | Kubernetes/compute decision |
| Prepare container registry plan | Task | Low | Proposed | Stable images |
| Prepare managed PostgreSQL plan | Task | Low | Proposed | Schema stable |
| Decide RabbitMQ hosting strategy | Task | Low | Proposed | Messaging stable |
| Implement controlled cloud deployment | Task | Low | Proposed | Plans approved |

**Acceptance criteria:**

- architecture and data flow are documented;
- credentials are not stored in repository files;
- database connectivity uses secure transport;
- deployment has health, logging, and rollback paths;
- costs and teardown steps are visible before resources are created.

**Risks:**

- unexpected cost;
- public exposure and weak access controls;
- operational overhead of AKS;
- cloud work adding little educational value before core completion.

**Definition of Done:** A reproducible, cost-controlled, observable deployment is
demonstrated or a justified simpler Azure alternative is documented.

### Epic 13 — Observability and JVM diagnostics

**Business description:** Enable developers and operators to understand whether
the system is healthy and diagnose failures.

**Technical goal:** Add health, metrics, structured logs, correlation, and safe JVM
diagnostic procedures.

**Scope:**

- Actuator liveness/readiness;
- structured logs;
- application and messaging metrics;
- correlation/event IDs;
- GC log, heap dump, and thread dump notes;
- diagnostic exercise.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Add Actuator health endpoints | Task | Medium | Proposed | Backend foundation |
| Add structured logging plan | Task | Medium | Proposed | Runnable flows |
| Add GC log and heap dump diagnostic notes | Task | Medium | Proposed | JVM runtime |
| Add thread dump diagnostic exercise | Task | Low | Proposed | Runnable application |
| Add key business and RabbitMQ metrics | Task | Medium | Proposed | Relevant flows |

**Acceptance criteria:**

- health groups distinguish alive from ready;
- logs are structured and avoid sensitive values;
- key request/event identifiers can be correlated;
- diagnostic steps state storage and privacy risks;
- at least one failure scenario is diagnosed and documented.

**Risks:**

- logging personal or secret data;
- excessive metric cardinality;
- exposing management endpoints publicly;
- heap dumps containing sensitive application data.

**Definition of Done:** Health and core flows are observable and a reviewer can
follow documented JVM diagnostic steps safely.

### Epic 14 — Portfolio polish and README

**Business description:** Present the project so a recruiter or engineer can
quickly understand its value and verify the work.

**Technical goal:** Curate architecture, screenshots, demonstrations, tests,
trade-offs, and setup guidance without hiding limitations.

**Scope:**

- final README and navigation;
- architecture diagram;
- screenshots/demo;
- test and quality summary;
- technical trade-offs and lessons learned.

**Stories and tasks:**

| Item | Type | Priority | Status | Depends on |
| --- | --- | --- | --- | --- |
| Add architecture diagram to README | Task | Medium | Proposed | Architecture stable |
| Add screenshots section | Task | Medium | Proposed | Frontend works |
| Add final portfolio explanation | Task | Medium | Proposed | Core features complete |
| Add demo data and walkthrough | Task | Low | Proposed | Stable deployment |

**Acceptance criteria:**

- README explains business value, architecture, stack, and main flow;
- setup instructions work from a clean checkout;
- screenshots/demo show real application behaviour;
- tests and engineering decisions are evidenced with links;
- limitations and future work are explicit.

**Risks:**

- polishing before correctness;
- screenshots becoming stale;
- claims not supported by working functionality;
- documentation duplication.

**Definition of Done:** A reviewer can understand, run, and evaluate the project
with clear evidence of Mid-level backend skills.

## 6. Initial Issue package

The initial GitHub Issue package contains the 69 requested items:

Complete copy-ready bodies are available in
[GitHub Issues — Initial Import Package](GITHUB_ISSUES.md).

1. `[Epic] Project foundation and documentation`
2. `[Task] Create documentation foundation`
3. `[Task] Define GitHub workflow and review checklist`
4. `[Task] Prepare initial architecture decision records`
5. `[Epic] Backend API foundation`
6. `[Task] Generate Spring Boot backend-api skeleton`
7. `[Task] Add global exception handling contract`
8. `[Task] Add API error response model`
9. `[Task] Add validation dependency and example request validation`
10. `[Task] Add PostgreSQL and Flyway configuration`
11. `[Epic] Salon CRM`
12. `[Story] Create salon profile`
13. `[Story] List salons with pagination and filtering`
14. `[Story] View salon details`
15. `[Story] Update salon contact data`
16. `[Story] Add note to salon`
17. `[Epic] Product Catalog`
18. `[Story] Create product`
19. `[Story] List products with filtering`
20. `[Story] Update product price`
21. `[Story] Deactivate product`
22. `[Epic] Inventory`
23. `[Story] View current inventory`
24. `[Story] Adjust stock level`
25. `[Story] Track stock movements`
26. `[Story] Reserve stock for order`
27. `[Epic] Orders`
28. `[Story] Create draft order`
29. `[Story] Add item to draft order`
30. `[Story] Place order`
31. `[Story] Cancel order`
32. `[Story] View order details`
33. `[Epic] RabbitMQ worker and notifications`
34. `[Task] Add RabbitMQ to Docker Compose`
35. `[Task] Define event envelope contract`
36. `[Story] Publish OrderPlaced event`
37. `[Story] Consume OrderPlaced event in worker`
38. `[Task] Add retry and dead-letter queue plan`
39. `[Epic] Demand forecasting`
40. `[Story] Generate basic demand recommendation`
41. `[Story] Show low stock recommendation`
42. `[Epic] Frontend foundation`
43. `[Task] Generate React TypeScript frontend`
44. `[Story] Display salons list in frontend`
45. `[Story] Create salon form in frontend`
46. `[Story] Display products list in frontend`
47. `[Story] Create order from frontend`
48. `[Epic] Docker Compose environment`
49. `[Task] Add Dockerfile for backend-api`
50. `[Task] Add Dockerfile for backend-worker`
51. `[Task] Add Dockerfile for frontend`
52. `[Task] Add compose.yml for local development`
53. `[Epic] Kubernetes deployment`
54. `[Task] Add Kubernetes manifests for backend-api`
55. `[Task] Add Kubernetes manifests for backend-worker`
56. `[Task] Add ConfigMap and Secret templates`
57. `[Task] Add liveness and readiness probes`
58. `[Epic] Cloud deployment`
59. `[Task] Prepare Azure deployment plan`
60. `[Task] Prepare container registry plan`
61. `[Task] Prepare managed PostgreSQL plan`
62. `[Epic] Observability and JVM diagnostics`
63. `[Task] Add Actuator health endpoints`
64. `[Task] Add structured logging plan`
65. `[Task] Add GC log and heap dump diagnostic notes`
66. `[Epic] Portfolio polish and README`
67. `[Task] Add architecture diagram to README`
68. `[Task] Add screenshots section`
69. `[Task] Add final portfolio explanation`

## 7. Refinement rule

Before the owner starts any implementation Issue, the Issue must state:

- business and technical goal;
- in-scope and out-of-scope behaviour;
- acceptance criteria;
- likely files/packages;
- test scenarios and appropriate test types;
- dependencies and risks;
- suggested owner-created branch name;
- step-by-step implementation checklist;
- pre-PR checklist.

Refinement must guide the owner without providing a complete production
implementation unless the owner explicitly requests code.
