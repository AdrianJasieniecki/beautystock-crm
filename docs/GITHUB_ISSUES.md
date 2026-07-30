# GitHub Issues — Initial Import Package

This file is a fallback package for manual GitHub Issue creation while connector
write operations are blocked. Each section is ready to copy into a new Issue.
Titles and label names are exact. If custom labels do not yet exist, create them
from the taxonomy in `docs/GITHUB_WORKFLOW.md` or keep the `Labels` section in the
Issue body.

Do not close Issues or merge related PRs without the repository owner's decision.
Application implementation remains the owner's work unless code is explicitly
requested.

---

## Issue 1

**Title:** `[Epic] Project foundation and documentation`

### Business description

Establish a reliable project foundation so implementation work has a clear
purpose, sequence, quality standard, and source of truth.

### Technical goal

Define architecture, API conventions, ADRs, roadmap, backlog, progress tracking,
local/deployment plans, GitHub workflow, and review rules.

### Scope

- Create and connect the complete initial documentation set.
- Define the modular-monolith plus worker direction.
- Prepare the first backlog and Issue package.
- Establish owner/Codex responsibilities.

### User stories and technical tasks

- `[Task] Create documentation foundation`
- `[Task] Define GitHub workflow and review checklist`
- `[Task] Prepare initial architecture decision records`

### Acceptance criteria

- All requested documentation files exist and link to one another.
- Fourteen epics and their dependencies are documented.
- The manual owner implementation rule is explicit.
- Initial Issues use a consistent structure.
- Documentation is proposed in a dedicated PR to `Production`.
- No application or infrastructure implementation is included.

### Risks

- Over-planning delays the first vertical slice.
- Documentation drifts from actual implementation.
- Scope grows beyond an educational portfolio project.

### Definition of Done

The owner reviews and merges the documentation PR, the first backend foundation
task is refined, and future work updates the relevant documentation.

### Labels

`epic`, `documentation`, `portfolio`, `priority-high`

---

## Issue 2

**Title:** `[Task] Create documentation foundation`

### Goal

Create the initial README and documentation files that explain project scope,
architecture, roadmap, backlog, API plan, progress, development, and deployment.

### Scope

- `README.md`
- `docs/ARCHITECTURE.md`
- `docs/ROADMAP.md`
- `docs/BACKLOG.md`
- `docs/API.md`
- `docs/PROGRESS.md`
- `docs/LOCAL_DEVELOPMENT.md`
- `docs/DEPLOYMENT_PLAN.md`

### Acceptance criteria

- Each document has a clear purpose and current status.
- README links to every primary document.
- Proposed repository structure is documented but not implemented.
- Roadmap distinguishes MVP, portfolio-ready, and stretch scope.
- Progress identifies the next manual implementation task.
- No production code or infrastructure configuration is generated.
- Markdown links and headings are checked.

### Suggested steps

1. Draft documents from confirmed project goals.
2. Cross-check architecture, roadmap, and backlog terminology.
3. Verify internal links and required sections.
4. Open a focused documentation PR.

### Review checklist

- No contradictory status or technology decisions.
- No undocumented secrets or real customer data.
- No claim that an unimplemented component is runnable.
- Documentation remains actionable rather than aspirational only.

### Suggested branch

`docs/project-foundation`

### Labels

`task`, `documentation`, `portfolio`, `priority-high`

---

## Issue 3

**Title:** `[Task] Define GitHub workflow and review checklist`

### Goal

Define how Issues, branches, commits, PRs, reviews, and completion decisions work
throughout the project.

### Scope

- Branch and commit naming.
- Issue lifecycle and label taxonomy.
- PR description and self-review requirements.
- Backend, JPA, REST, test, infrastructure, frontend, and security review checks.
- Owner/Codex responsibility boundary.

### Acceptance criteria

- `docs/GITHUB_WORKFLOW.md` documents statuses from Proposed to Done.
- `docs/REVIEW_CHECKLIST.md` covers all requested review areas.
- Only the owner may approve merge and Issue closure.
- Implementation branches are created by the owner.
- Definition of Done requires build, tests, documentation, PR self-review, and no
  blocking findings.
- The process supports follow-up review after corrections.

### Risks

- Checklist becomes mechanical and replaces engineering judgment.
- Workflow overhead is too high for small changes.

### Suggested branch

`docs/github-workflow`

### Labels

`task`, `documentation`, `clean-code`, `review-needed`, `priority-high`

---

## Issue 4

**Title:** `[Task] Prepare initial architecture decision records`

### Goal

Record the initial technical decisions and their consequences so future changes
are deliberate and reviewable.

### Scope

- Modular monolith instead of microservices.
- RabbitMQ before Kafka.
- PostgreSQL and Flyway.
- React + TypeScript.
- Docker Compose before Kubernetes.
- Manual implementation by the project owner.
- DTO/entity separation, transaction boundaries, concurrency, event delivery,
  dual-write risk, API errors, and UTC time.

### Acceptance criteria

- Each ADR has status, context, decision, and consequences.
- Proposed decisions are distinguished from accepted decisions.
- Dual-write risk and future Outbox Pattern are explicit.
- ADRs do not pretend deferred choices are final.
- Superseding decisions will be added rather than silently rewriting history.

### Suggested branch

`docs/architecture-decisions`

### Labels

`task`, `documentation`, `backend`, `infrastructure`, `priority-high`

---

## Issue 5

**Title:** `[Epic] Backend API foundation`

### Business description

Provide a stable technical foundation so business features can be implemented
with consistent API, persistence, validation, testing, and configuration rules.

### Technical goal

Create a Java 21, Spring Boot, and Maven API skeleton with an error contract,
validation, PostgreSQL, Flyway, and focused test foundations.

### Scope

- `backend-api` skeleton.
- Package/module conventions.
- Global exception handling and API error model.
- Bean Validation.
- PostgreSQL and Flyway.
- Local/test configuration without committed secrets.

### User stories and technical tasks

- `[Task] Generate Spring Boot backend-api skeleton`
- `[Task] Add global exception handling contract`
- `[Task] Add API error response model`
- `[Task] Add validation dependency and example request validation`
- `[Task] Add PostgreSQL and Flyway configuration`

### Acceptance criteria

- Java 21 build succeeds.
- Application starts with an explicit local configuration.
- Errors and validation use one documented response contract.
- Flyway migrates an empty PostgreSQL database.
- PostgreSQL behaviour is tested without substituting H2.
- The smallest suitable Spring test context is used.
- No secret is committed.

### Risks

- Premature abstractions.
- Unverified dependency/version choices.
- H2 hiding PostgreSQL-specific behaviour.
- Error responses leaking internal details.

### Definition of Done

The backend foundation is reviewed and merged, tests pass, and the first Salon
story can be implemented without changing the basic conventions.

### Labels

`epic`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 6

**Title:** `[Task] Generate Spring Boot backend-api skeleton`

### Goal

Create the minimal Spring Boot API application that will host the modular
monolith.

### Technical requirements

- Java 21 and Maven.
- Current supported Spring Boot version selected from official documentation.
- Constructor injection.
- Package-by-business-module direction.
- Maven Wrapper committed.
- Minimal dependencies only.
- Basic context/smoke test.

### Acceptance criteria

- `backend-api` builds using the Maven Wrapper and Java 21.
- Application starts without business endpoints.
- Base package supports future `salon`, `product`, `inventory`, `order`,
  `forecasting`, `notification`, and limited `shared` modules.
- No sample entity, repository, or generated demo business logic remains.
- Dependency choices are documented.
- Smoke test verifies the application context or a smaller meaningful bootstrap
  contract.

### Likely files

- `backend-api/pom.xml`
- `backend-api/mvnw`, `backend-api/mvnw.cmd`, `.mvn/`
- application bootstrap class
- base `application.yml`
- initial test class

### Tests

- Maven build on Java 21.
- One minimal bootstrap/smoke test.
- Manual startup with documented profile/configuration.

### Pitfalls

- Adding every planned dependency at once.
- Creating repository-wide technical layers before modules exist.
- Committing IDE files or secrets.

### Suggested branch

`feature/backend-api-foundation`

### Implementation rule

Implement manually. Codex guides and reviews unless explicitly asked to write code.

### Labels

`task`, `backend`, `testing`, `priority-high`

---

## Issue 7

**Title:** `[Task] Add global exception handling contract`

### Goal

Map expected API failures consistently through a global
`@RestControllerAdvice`.

### Scope

- Validation and malformed-request handling.
- Not-found and conflict mapping.
- Safe fallback for unexpected failures.
- Correlation/request information where available.
- Logging policy for expected versus unexpected errors.

### Acceptance criteria

- Controllers do not duplicate exception-to-response mapping.
- Known errors map to documented HTTP statuses and stable error codes.
- Unexpected failures return a safe `500` response without stack trace, SQL, or
  internal class names.
- Logs retain enough context for diagnosis without sensitive data.
- Controller tests cover representative mappings.

### Tests

- `@WebMvcTest` success/error cases.
- Validation failure.
- Not found.
- Conflict.
- Unexpected exception safety.

### Pitfalls

- Catching `Exception` inside controllers.
- Translating every database exception to the same conflict.
- Logging the same exception repeatedly at several layers.

### Suggested branch

`feature/api-exception-handling`

### Labels

`task`, `backend`, `testing`, `clean-code`, `priority-high`

---

## Issue 8

**Title:** `[Task] Add API error response model`

### Goal

Define the stable DTO returned for API errors.

### Proposed fields

- timestamp;
- HTTP status;
- stable error code;
- safe message;
- request path;
- correlation ID;
- optional field violations.

### Acceptance criteria

- The error type is an API DTO, not an exception or persistence model.
- JSON field names and optional-field behaviour are documented.
- Validation violations identify field, validation code, and safe message.
- Rejected values are not returned by default.
- Example JSON in `docs/API.md` matches serialization tests.
- Error codes are machine-readable and not derived from exception class names.

### Tests

- JSON serialization contract.
- Response with and without violations.
- No sensitive/internal fields present.

### Suggested branch

`feature/api-error-model`

### Labels

`task`, `backend`, `testing`, `documentation`, `priority-high`

---

## Issue 9

**Title:** `[Task] Add validation dependency and example request validation`

### Goal

Establish Bean Validation at the REST boundary without implementing a business
module prematurely.

### Scope

- Add validation starter/dependency.
- Add one narrowly scoped example or test-only request contract.
- Demonstrate field and object validation mapping to the common error response.
- Document which rules belong to transport, business, and database layers.

### Acceptance criteria

- Invalid request input returns `400` with field violations.
- Valid input reaches the controller/use-case boundary.
- Validation messages are stable enough for tests without over-coupling to exact
  framework wording.
- Business invariants are not forced into annotations.
- The example does not become unused production demo code.

### Tests

- Missing required field.
- Invalid format/range.
- Valid request.
- Error JSON contract.

### Suggested branch

`feature/api-validation-foundation`

### Labels

`task`, `backend`, `testing`, `priority-high`

---

## Issue 10

**Title:** `[Task] Add PostgreSQL and Flyway configuration`

### Goal

Connect the API to PostgreSQL and make Flyway the authoritative schema migration
mechanism.

### Scope

- PostgreSQL driver and JPA/Flyway dependencies.
- Local and test configuration through environment values.
- Initial migration suitable for the current empty domain.
- Schema validation policy.
- PostgreSQL Testcontainers foundation.

### Acceptance criteria

- Application connects to PostgreSQL using environment-backed configuration.
- Flyway succeeds from an empty database.
- Hibernate does not use `ddl-auto=update` as migration.
- Testcontainers verifies PostgreSQL/Flyway integration.
- Real passwords and `.env` files are not committed.
- Migration naming and forward-only policy are documented.

### Tests

- Empty-database migration.
- Application/repository bootstrap against PostgreSQL.
- Failure is clear when required database configuration is missing.

### Pitfalls

- Using H2 for convenience.
- Placing secrets in YAML.
- Creating business tables before their Issues define constraints.

### Suggested branch

`feature/postgresql-flyway-foundation`

### Labels

`task`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 11

**Title:** `[Epic] Salon CRM`

### Business description

Manage beauty salons as potential or active wholesale customers, including
contact data, lifecycle status, and sales notes.

### Technical goal

Deliver the first complete REST/JPA vertical slice with DTOs, validation,
constraints, transactions, pagination, filtering, and focused tests.

### Scope

- Create and view salon profiles.
- Paginated/filterable salon list.
- Update contact data.
- Add and list sales notes.
- Prepare stable salon identity for Orders.

### User stories

- `[Story] Create salon profile`
- `[Story] List salons with pagination and filtering`
- `[Story] View salon details`
- `[Story] Update salon contact data`
- `[Story] Add note to salon`

### Acceptance criteria

- No JPA entity is exposed by the API.
- Input validation and database constraints are complementary.
- Updates use load → modify managed entity → commit.
- List/note endpoints are paginated where needed.
- Important queries are reviewed for N+1 and index needs.
- Unit, controller, and PostgreSQL integration tests cover important flows.

### Risks

- Email normalization and case-insensitive uniqueness must remain consistent
  between the application and PostgreSQL.
- Notes may grow without pagination.
- Eager relationships may create N+1 queries.

### Definition of Done

High-priority Salon stories meet global DoD and Orders can reference a stable
salon identifier.

### Labels

`epic`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 12

**Title:** `[Story] Create salon profile`

### User story

As a sales/admin user, I want to create a salon profile so that I can manage it as
a potential or active customer.

### Business value

The CRM can store salons and later associate notes, recommendations, and orders
with them.

### Scope

- Create request DTO and response DTO.
- Accept required `name` and `email`, plus optional `phone`.
- Normalize email to trimmed lowercase and blank phone to `null`.
- Assign server-owned `LEAD` status, generated ID, and UTC timestamps.
- Add salon persistence model and Flyway migration.
- Add repository and application service/use case.
- Add REST endpoint and mapping.
- Add focused tests.

### Acceptance criteria

- `POST /api/v1/salons` returns `201 Created`.
- Response contains a generated positive `Long` ID, normalized fields, `LEAD`
  status, and UTC timestamps and does not expose a JPA entity.
- Invalid email and missing required fields return `400`.
- Email is globally unique case-insensitively for this MVP; duplicates return
  `409`.
- Controller stays thin and service owns orchestration.
- Database constraint independently protects uniqueness.
- Tests cover success, validation, and duplicate conflict.

### Technical notes

- Create `V2__create_salons_table.sql`; do not modify the merged V1 baseline.
- Keep address and client-selected lifecycle status out of the create request.
- Use constructor injection.
- Persist new entities deliberately; do not reuse the entity as a request DTO.
- Use UTC/auditing conventions selected by the project.

### Likely files

- Salon request/response DTOs and mapper.
- Salon controller.
- Salon application service/use case.
- Salon entity and repository.
- Flyway migration.
- Unit, web slice, and PostgreSQL integration tests.

### Suggested implementation steps

1. Write the fixed request/response and validation tests.
2. Create DTOs and deliberate mappings.
3. Add the V2 migration and entity mapping.
4. Add repository and transactional create use case.
5. Map recognized duplicate-email failures to the existing conflict exception.
6. Add the thin controller with `Location`.
7. Add focused PostgreSQL integration tests and run the complete build.

### Suggested branch

`feature/create-salon-profile`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 13

**Title:** `[Story] List salons with pagination and filtering`

### User story

As a sales/admin user, I want to browse and filter salons so that I can quickly
find relevant customers and leads.

### Scope

- Paginated list endpoint.
- Filter by lifecycle status.
- Agreed name/contact search semantics.
- Allow-listed sorting.
- Summary response DTO/projection.

### Acceptance criteria

- `GET /api/v1/salons` returns a stable page DTO.
- Defaults and maximum page size follow `docs/API.md`.
- Status and text filters can be combined.
- Invalid sort/filter values return a documented client error.
- Results have deterministic ordering.
- Query is checked for N+1 and relevant indexes.
- Tests cover empty, populated, filtered, paginated, and invalid inputs.

### Technical notes

- Do not expose Spring `Page` serialization accidentally.
- Prefer a DTO projection/read model if it keeps the list query explicit.
- Specify case and normalization rules for text search.

### Suggested branch

`feature/list-salons`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 14

**Title:** `[Story] View salon details`

### User story

As a sales/admin user, I want to view one salon so that I can understand its
contact and CRM status.

### Scope

- Salon detail endpoint.
- Response DTO with intended fields only.
- Not-found mapping.
- Clear boundary for notes/order-history summaries.

### Acceptance criteria

- `GET /api/v1/salons/{salonId}` returns `200` for an existing salon.
- Unknown ID returns the shared `404` error.
- Response does not serialize entity relationships.
- Notes and orders are linked or summarized intentionally, not loaded
  accidentally.
- Controller and integration tests cover success and not found.

### Suggested branch

`feature/view-salon-details`

### Labels

`story`, `backend`, `testing`, `priority-high`

---

## Issue 15

**Title:** `[Story] Update salon contact data`

### User story

As a sales/admin user, I want to update salon contact data so that customer
records remain accurate.

### Scope

- Dedicated contact update request.
- Load existing salon in a transaction.
- Modify the managed entity.
- Preserve ID, audit data, notes, and status unless explicitly included.

### Acceptance criteria

- `PATCH /api/v1/salons/{salonId}/contact` returns the updated response.
- Invalid fields return `400`.
- Unknown salon returns `404`.
- Confirmed uniqueness conflict returns `409`.
- Update uses load → modify → commit, not detached reconstruction plus blind
  `save()`.
- Omitted/non-contact fields cannot be overwritten.
- Tests verify dirty checking, validation, conflict, and not found.

### Suggested branch

`feature/update-salon-contact`

### Labels

`story`, `backend`, `database`, `testing`, `clean-code`, `priority-high`

---

## Issue 16

**Title:** `[Story] Add note to salon`

### User story

As a sales user, I want to add a note to a salon so that commercial context is
available for future contact.

### Scope

- Add note command/endpoint.
- Note author representation decision.
- Note text validation and creation timestamp.
- Paginated note retrieval plan.

### Acceptance criteria

- `POST /api/v1/salons/{salonId}/notes` returns `201`.
- Blank or oversized notes return `400`.
- Unknown salon returns `404`.
- Existing notes are not overwritten or deleted.
- Note timestamps use project time conventions.
- Note relationships do not cause recursion/N+1 in salon responses.
- Tests cover creation, validation, ordering, and not found.

### Risks

- No authentication exists yet to establish a trusted author identity.
- Unbounded note history can degrade detail queries.

### Suggested branch

`feature/add-salon-note`

### Labels

`story`, `backend`, `database`, `testing`, `priority-medium`

---

## Issue 17

**Title:** `[Epic] Product Catalog`

### Business description

Maintain the products offered to beauty salons, including SKU, brand/category,
price, and lifecycle.

### Technical goal

Provide stable product identity and commercial data for Inventory and Orders with
strong database integrity and clear API contracts.

### Scope

- Product creation.
- Paginated/filterable list.
- Price update.
- Product deactivation.
- Deferred CSV import.

### User stories

- `[Story] Create product`
- `[Story] List products with filtering`
- `[Story] Update product price`
- `[Story] Deactivate product`

### Acceptance criteria

- SKU uniqueness is protected by PostgreSQL.
- Money never uses floating point.
- Deactivation preserves historical references.
- List queries are paginated and reviewed for indexes/N+1.
- API DTOs and JPA entities stay separate.
- Representative tests cover lifecycle and constraints.

### Risks

- Over-modelling brand/category.
- Historical order values changing with current prices.
- Hard deletion breaking inventory/order history.

### Definition of Done

Inventory and Orders can depend on stable, tested product IDs and current
commercial data.

### Labels

`epic`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 18

**Title:** `[Story] Create product`

### User story

As an admin user, I want to create a product so that it can be offered and stocked.

### Scope

- Product create request/response DTOs.
- SKU, name, brand/category, price, currency, and active state decisions.
- Entity/repository/migration/use case/controller.
- Validation and constraints.

### Acceptance criteria

- `POST /api/v1/products` returns `201 Created`.
- SKU is normalized consistently and duplicate SKU returns `409`.
- Invalid/non-positive price or missing required fields returns `400`.
- Money uses `BigDecimal` or an explicit money value, never floating point.
- Response is a DTO.
- Database constraints protect SKU and numeric integrity.
- Tests cover success, validation, duplicate SKU, and mapping.

### Pitfalls

- Using current product price as future historical order price.
- Encoding currency inconsistently.
- Creating unnecessary entities for every catalogue attribute before requirements.

### Suggested branch

`feature/create-product`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 19

**Title:** `[Story] List products with filtering`

### User story

As an admin user, I want to browse and filter products so that I can manage the
catalogue efficiently.

### Scope

- Paginated product list.
- Filters for SKU, brand, category, active state, and text query as confirmed.
- Allow-listed sort fields.
- Summary response DTO/projection.

### Acceptance criteria

- `GET /api/v1/products` returns the stable page contract.
- Default and maximum sizes follow API conventions.
- Filters combine predictably.
- Active/inactive behaviour is explicit.
- Results have deterministic ordering.
- Query is reviewed for N+1 and justified indexes.
- Tests cover empty, filtered, paginated, and invalid input cases.

### Suggested branch

`feature/list-products`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 20

**Title:** `[Story] Update product price`

### User story

As an admin user, I want to update a product's current price so that new orders use
the correct commercial value.

### Scope

- Dedicated price update request.
- Managed-entity update within a transaction.
- Explicit currency behaviour.
- Preserve historical order item price snapshots.

### Acceptance criteria

- `PATCH /api/v1/products/{productId}/price` returns the updated product DTO.
- Non-positive/invalid value returns `400`.
- Unknown product returns `404`.
- Update uses load → modify → commit.
- Existing order items are unchanged.
- Precision/scale behaviour is tested against PostgreSQL.

### Suggested branch

`feature/update-product-price`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 21

**Title:** `[Story] Deactivate product`

### User story

As an admin user, I want to deactivate a product so that it cannot be used in new
orders while historical data remains valid.

### Scope

- Explicit deactivation command.
- Product lifecycle validation.
- Catalogue filtering behaviour.
- Order rule for inactive products.

### Acceptance criteria

- Deactivation returns the updated product state.
- Unknown product returns `404`.
- Repeated deactivation has defined idempotent/conflict behaviour.
- Inactive product cannot be added to a new draft/order according to the confirmed
  business rule.
- Existing inventory, movements, and order history remain referencable.
- No hard delete is used.
- Tests cover transition and downstream rule.

### Suggested branch

`feature/deactivate-product`

### Labels

`story`, `backend`, `database`, `testing`, `priority-medium`

---

## Issue 22

**Title:** `[Epic] Inventory`

### Business description

Show accurate warehouse stock and prevent the wholesaler from selling inventory
that is not available.

### Technical goal

Model balances, reservations, immutable movements, non-negative invariants, and
concurrent modifications with explicit transaction behaviour.

### Scope

- Current stock view.
- Manual adjustments.
- Movement history.
- Reserve/release stock for orders.
- Optimistic-locking evaluation and concurrency tests.

### User stories

- `[Story] View current inventory`
- `[Story] Adjust stock level`
- `[Story] Track stock movements`
- `[Story] Reserve stock for order`

### Acceptance criteria

- On-hand, reserved, and available quantities have precise definitions.
- Stock cannot become negative.
- Every change has an auditable movement/reason.
- Reservation changes are transactional.
- Concurrent updates cannot silently lose data or oversell.
- PostgreSQL integration tests demonstrate the locking behaviour.

### Risks

- Race conditions and lost updates.
- Balance diverging from movement history.
- Blind optimistic-lock retries violating business intent.

### Definition of Done

Inventory integrity holds under representative concurrency and Orders can reserve
stock transactionally.

### Labels

`epic`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 23

**Title:** `[Story] View current inventory`

### User story

As a warehouse/admin user, I want to view current inventory so that I know which
products are available.

### Scope

- Paginated inventory list.
- Per-product balance detail.
- On-hand, reserved, and available quantities.
- Product summary without leaking cross-module entities.

### Acceptance criteria

- `GET /api/v1/inventory` returns paginated inventory DTOs.
- Quantities are non-negative and internally consistent.
- Filtering/sorting needs are explicit and allow-listed.
- Product information is obtained through an intentional query/read model.
- Query is checked for N+1.
- Tests cover empty stock, populated stock, pagination, and mapping.

### Suggested branch

`feature/view-inventory`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 24

**Title:** `[Story] Adjust stock level`

### User story

As a warehouse user, I want to apply a stock adjustment so that deliveries,
corrections, and losses are reflected accurately.

### Scope

- Adjustment command with signed quantity or explicit increase/decrease model.
- Required reason and optional reference.
- Balance update and movement creation in one transaction.
- Non-negative invariant.

### Acceptance criteria

- `POST /api/v1/inventory/{productId}/adjustments` returns `201`.
- Zero or invalid adjustment returns `400`.
- Unknown product/inventory returns the appropriate documented error.
- Adjustment that would create negative stock returns `409`.
- Balance and movement persist atomically or both roll back.
- Update uses a managed inventory entity.
- Tests cover increase, decrease, invalid, insufficient, and rollback paths.

### Suggested branch

`feature/adjust-stock`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 25

**Title:** `[Story] Track stock movements`

### User story

As a warehouse/admin user, I want to view stock movements so that every balance
change can be explained.

### Scope

- Immutable movement record.
- Movement type, quantity, reason, timestamp, and reference.
- Paginated, ordered history per product.

### Acceptance criteria

- Every implemented adjustment/reservation/release creates the expected movement.
- Movement records are not edited as a way to change current stock.
- `GET /api/v1/inventory/{productId}/movements` is paginated.
- Ordering is deterministic, newest-first or explicitly documented.
- Movement query has an appropriate product/timestamp index.
- Tests reconcile representative movements with balance changes.

### Suggested branch

`feature/stock-movements`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 26

**Title:** `[Story] Reserve stock for order`

### User story

As the order process, I want to reserve available stock so that a placed order
cannot be oversold.

### Scope

- Internal Inventory application interface for reservation.
- Multi-item reservation semantics.
- Reservation identity linked to an order.
- Movement/history.
- Optimistic-lock conflict handling.

### Acceptance criteria

- Reservation succeeds only when all required quantities are available, unless a
  different partial-reservation rule is explicitly approved.
- Failed multi-item reservation leaves no partial state.
- Available quantity decreases while on-hand remains correctly defined.
- Duplicate reservation for the same order is idempotent or rejected safely.
- Concurrent reservation test proves overselling is prevented.
- Conflict maps to an explicit business/API outcome.

### Pitfalls

- Reserving items in inconsistent order and increasing deadlock risk.
- Retrying conflicts without re-evaluating availability.
- Orders accessing the Inventory repository directly.

### Suggested branch

`feature/reserve-order-stock`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 27

**Title:** `[Epic] Orders`

### Business description

Allow staff to prepare, place, inspect, and cancel salon orders while keeping
inventory and status history consistent.

### Technical goal

Implement an order aggregate and transition model coordinating Salon, Product,
and Inventory through explicit application boundaries.

### Scope

- Draft creation and item editing.
- Price snapshots.
- Placement and stock reservation.
- Cancellation and reservation release.
- Status history and order details.

### User stories

- `[Story] Create draft order`
- `[Story] Add item to draft order`
- `[Story] Place order`
- `[Story] Cancel order`
- `[Story] View order details`

### Acceptance criteria

- Allowed transitions for `DRAFT`, `PLACED`, `RESERVED`, `CONFIRMED`,
  `CANCELLED`, and `FULFILLED` are documented before implementation.
- Price is snapshotted on each order item.
- Placement/reservation is atomic.
- Cancellation releases stock exactly once when applicable.
- Status history is immutable and ordered.
- Integration tests cover rollback and invalid transitions.

### Risks

- Ambiguous status semantics.
- Partial reservations.
- Aggregate becoming too large.
- Duplicate commands repeating side effects.

### Definition of Done

The MVP order flow works end to end with correct inventory behaviour and stable
REST DTOs.

### Labels

`epic`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 28

**Title:** `[Story] Create draft order`

### User story

As a sales/admin user, I want to create a draft order for a salon so that I can
build it before committing stock.

### Scope

- Create order request with salon reference.
- `DRAFT` initial state.
- Order number/identifier policy.
- Entity, repository, migration, use case, controller, DTOs.

### Acceptance criteria

- `POST /api/v1/orders` returns `201 Created`.
- Existing eligible salon is required.
- Unknown/ineligible salon returns the documented error.
- New order starts in `DRAFT`.
- No stock is reserved by draft creation.
- Response is a DTO and includes stable identifier/status.
- Tests cover success, invalid salon, and persistence mapping.

### Suggested branch

`feature/create-draft-order`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 29

**Title:** `[Story] Add item to draft order`

### User story

As a sales/admin user, I want to add a product and quantity to a draft order so
that I can prepare the requested items.

### Scope

- Add-item request and response.
- Product eligibility and quantity validation.
- Current price lookup and order-item price snapshot.
- Duplicate-product behaviour.

### Acceptance criteria

- `POST /api/v1/orders/{orderId}/items` returns `201`.
- Only a `DRAFT` order can be edited.
- Product must exist and be active.
- Quantity must be greater than zero.
- Unit price and currency are snapshotted.
- Adding the same product has an explicit merge/reject rule.
- No stock reservation occurs yet unless the state model explicitly changes.
- Tests cover success, invalid status, inactive product, invalid quantity, and
  duplicate product.

### Suggested branch

`feature/add-draft-order-item`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 30

**Title:** `[Story] Place order`

### User story

As a sales/admin user, I want to place a complete draft order so that stock is
secured and fulfilment can begin.

### Scope

- Validate draft state and non-empty items.
- Revalidate product/order rules.
- Reserve inventory.
- Change status according to the approved transition model.
- Append status history.

### Acceptance criteria

- `POST /api/v1/orders/{orderId}/placement` returns the placed/reserved order.
- Empty or non-draft order cannot be placed.
- All stock reservations succeed atomically or the entire use case rolls back.
- Insufficient stock returns `409` with stable code.
- Repeated placement is idempotent or safely rejected.
- Status history records the transition once.
- Tests cover success, empty order, invalid status, insufficient stock,
  concurrency, and rollback.

### Technical notes

- Transaction boundary belongs to the application use case.
- Do not access Inventory repositories from Orders.
- Event publication is a later Issue; document the future hook without coupling
  the core use case to RabbitMQ.

### Suggested branch

`feature/place-order`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 31

**Title:** `[Story] Cancel order`

### User story

As a sales/admin user, I want to cancel an eligible order so that reserved stock is
released and the order cannot proceed.

### Scope

- Allowed cancellation states.
- Reservation release.
- Status/history update.
- Idempotent or explicit repeated-cancellation behaviour.

### Acceptance criteria

- `POST /api/v1/orders/{orderId}/cancellation` returns the cancelled order.
- Unknown order returns `404`.
- Ineligible transition returns `409`.
- Reserved stock is released exactly once.
- Order state, history, inventory balance, and movements change atomically.
- Repeated request cannot double-release stock.
- Tests cover eligible states, ineligible states, duplicate cancellation, and
  rollback.

### Suggested branch

`feature/cancel-order`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 32

**Title:** `[Story] View order details`

### User story

As a sales/admin user, I want to view order details so that I can understand its
items, totals, status, salon, and history.

### Scope

- Order detail DTO.
- Item price snapshots and totals.
- Salon summary.
- Ordered status history.

### Acceptance criteria

- `GET /api/v1/orders/{orderId}` returns `200` for an existing order.
- Unknown order returns the shared `404`.
- Response exposes DTOs, not the order JPA graph.
- Totals use correct decimal/currency handling.
- Query has an intentional fetch plan and is checked for N+1/cartesian expansion.
- Tests cover draft and placed/cancelled representations.

### Suggested branch

`feature/view-order-details`

### Labels

`story`, `backend`, `database`, `testing`, `priority-high`

---

## Issue 33

**Title:** `[Epic] RabbitMQ worker and notifications`

### Business description

Process order and low-stock notifications asynchronously so synchronous business
requests do not wait for notification work.

### Technical goal

Introduce RabbitMQ topology, a versioned event contract, a separate consumer,
idempotency, bounded retry, and dead-letter handling.

### Scope

- RabbitMQ in Compose.
- Event envelope.
- `OrderPlaced` publication.
- Worker consumption and notification outcome.
- Retry/DLQ and idempotency.
- Documented dual-write risk and later Outbox Pattern.

### Stories and tasks

- `[Task] Add RabbitMQ to Docker Compose`
- `[Task] Define event envelope contract`
- `[Story] Publish OrderPlaced event`
- `[Story] Consume OrderPlaced event in worker`
- `[Task] Add retry and dead-letter queue plan`

### Acceptance criteria

- Event contract is versioned.
- Consumer tolerates duplicate delivery.
- Retry is bounded and poison messages reach an observable DLQ.
- Dual-write risk is not hidden.
- RabbitMQ Testcontainers covers representative flows.
- Logs include event/correlation identifiers without sensitive payloads.

### Risks

- Event loss after database commit.
- Infinite retry loops.
- Duplicate notification effects.
- Breaking event-schema changes.

### Definition of Done

One logical notification outcome is produced for a placed order under normal and
duplicate delivery, with inspectable failure handling.

### Labels

`epic`, `backend`, `rabbitmq`, `testing`, `priority-high`

---

## Issue 34

**Title:** `[Task] Add RabbitMQ to Docker Compose`

### Goal

Provide a healthy local RabbitMQ broker and management UI for development and
integration exercises.

### Scope

- RabbitMQ image/version decision.
- Local-only user/vhost configuration.
- AMQP and management ports.
- Health check and persistent volume decision.
- Safe environment template.

### Acceptance criteria

- Compose starts RabbitMQ and reports healthy only when the broker is ready.
- Management UI is reachable with documented local-only credentials.
- Credentials are not baked into images or committed as real secrets.
- API/worker configuration can reference the service by environment values.
- Start, inspect, stop, and narrow reset instructions are documented.

### Suggested branch

`infra/rabbitmq-compose`

### Labels

`task`, `infrastructure`, `docker`, `rabbitmq`, `priority-high`

---

## Issue 35

**Title:** `[Task] Define event envelope contract`

### Goal

Define a stable, versioned transport envelope for RabbitMQ events.

### Required metadata

- `eventId`
- `eventType`
- `eventVersion`
- `occurredAt`
- `aggregateId`
- optional `correlationId`
- `payload`

### Acceptance criteria

- Field types, required values, timestamp format, and versioning policy are
  documented.
- `OrderPlaced` v1 payload is defined without exposing JPA entities.
- Event name is a past-tense business fact.
- Producer and consumer serialization contract tests use the same published
  schema expectations.
- Compatibility strategy for additive/breaking change is explicit.
- Payload excludes secrets and unnecessary personal data.

### Suggested branch

`feature/event-envelope-contract`

### Labels

`task`, `backend`, `rabbitmq`, `documentation`, `testing`, `priority-high`

---

## Issue 36

**Title:** `[Story] Publish OrderPlaced event`

### User story

As the notification workflow, I want an `OrderPlaced` event so that asynchronous
consumers can react to a committed order.

### Scope

- Build event from an order placement result.
- Publish through an application-facing port/adaptor.
- Correlation/event metadata.
- Document initial transaction/publication semantics.

### Acceptance criteria

- Event is produced only for a successful placement.
- Rolled-back placement does not produce the event.
- Payload contains stable IDs and required facts, not entities.
- Publication failure behaviour is explicit and tested proportionately.
- Initial post-commit loss window is documented until Outbox exists.
- RabbitMQ integration test verifies routing and serialization.

### Pitfalls

- Publishing inside the transaction before commit.
- Claiming atomic database/message delivery.
- Coupling the Order domain to Spring AMQP types.

### Suggested branch

`feature/publish-order-placed`

### Labels

`story`, `backend`, `rabbitmq`, `testing`, `priority-high`

---

## Issue 37

**Title:** `[Story] Consume OrderPlaced event in worker`

### User story

As an operations user, I want the worker to process placed-order events so that a
notification is recorded or a deterministic email send is simulated.

### Scope

- `backend-worker` consumer.
- Envelope/version validation.
- Idempotency check using `eventId`.
- Notification persistence or simulation.
- Acknowledgement/failure policy.

### Acceptance criteria

- Valid event creates one logical notification outcome.
- Duplicate delivery does not repeat an unsafe side effect.
- Unsupported/invalid event is handled according to retry/DLQ policy.
- Message is acknowledged only after successful processing.
- Logs include event ID and aggregate ID.
- RabbitMQ Testcontainers tests success, duplicate, and failure behaviour.

### Suggested branch

`feature/order-placed-worker`

### Labels

`story`, `backend`, `rabbitmq`, `testing`, `priority-high`

---

## Issue 38

**Title:** `[Task] Add retry and dead-letter queue plan`

### Goal

Define bounded, observable handling for transient and terminal consumer failures.

### Scope

- Retryable/non-retryable error classification.
- Attempt count and backoff.
- Dead-letter exchange/queue topology.
- Poison-message inspection and replay procedure.
- Metrics/logging.

### Acceptance criteria

- Retry is finite and does not hot-loop.
- Invalid schema/business messages do not retry indefinitely.
- Exhausted messages reach a named DLQ with useful metadata.
- Original event ID/correlation is preserved.
- Manual replay is documented and remains idempotent.
- Integration tests prove retry and DLQ routing.

### Suggested branch

`feature/rabbitmq-retry-dlq`

### Labels

`task`, `backend`, `rabbitmq`, `testing`, `infrastructure`, `priority-high`

---

## Issue 39

**Title:** `[Epic] Demand forecasting`

### Business description

Help staff anticipate product demand and low-stock risk using historical orders
and current inventory.

### Technical goal

Implement deterministic, explainable recommendation calculations rather than
opaque machine learning.

### Scope

- Average consumption over a defined window.
- Expected next-order date.
- Low-stock recommendation.
- Insufficient-data handling.

### User stories

- `[Story] Generate basic demand recommendation`
- `[Story] Show low stock recommendation`

### Acceptance criteria

- Formula, input data, time window, and assumptions are documented.
- Recommendations are reproducible.
- Insufficient data is represented explicitly.
- Time-dependent logic uses deterministic tests.
- Recommendations never mutate orders/inventory automatically.

### Risks

- Misleading precision.
- Timezone/date boundary errors.
- Expensive aggregation queries.

### Definition of Done

Users can view explainable, tested recommendations with visible limitations.

### Labels

`epic`, `backend`, `database`, `testing`, `priority-medium`

---

## Issue 40

**Title:** `[Story] Generate basic demand recommendation`

### User story

As a sales/warehouse user, I want a basic demand recommendation so that I can
anticipate which products may be ordered again.

### Scope

- Confirm aggregation level: product, salon-product, or both.
- Define history window and minimum observations.
- Calculate average consumption/order interval.
- Estimate next-order date with confidence/insufficient-data state.

### Acceptance criteria

- Formula and assumptions are documented.
- Same input produces the same output.
- Fewer than the required observations returns an explicit insufficient-data
  result.
- Cancelled/non-relevant order statuses are excluded deliberately.
- Time uses an injected clock and correct date/instant semantics.
- Unit tests cover typical, sparse, zero, cancelled, and boundary histories.

### Suggested branch

`feature/demand-recommendation`

### Labels

`story`, `backend`, `database`, `testing`, `priority-medium`

---

## Issue 41

**Title:** `[Story] Show low stock recommendation`

### User story

As a warehouse user, I want to see low-stock recommendations so that I can plan
replenishment before expected demand exceeds availability.

### Scope

- Combine available inventory with chosen demand estimate.
- Define threshold/horizon.
- Expose paginated or bounded recommendation response.
- Explain recommendation inputs.

### Acceptance criteria

- Recommendation distinguishes on-hand, reserved, and available quantities.
- Threshold/horizon is configurable or explicitly documented.
- Products without sufficient history have defined fallback behaviour.
- Result includes enough data to explain why it was produced.
- No inventory/order mutation occurs.
- Tests cover low, adequate, zero-stock, reserved-stock, and insufficient-data
  cases.

### Suggested branch

`feature/low-stock-recommendation`

### Labels

`story`, `backend`, `database`, `testing`, `priority-medium`

---

## Issue 42

**Title:** `[Epic] Frontend foundation`

### Business description

Provide a simple, usable admin interface for the CRM, catalogue, orders, and
notifications.

### Technical goal

Create a React + TypeScript application with routing, typed API integration,
forms, and complete loading/error/empty/success states.

### Scope

- Frontend skeleton and app shell.
- Salon list and form.
- Product list.
- Order creation.
- Later order details and notifications.

### Stories and tasks

- `[Task] Generate React TypeScript frontend`
- `[Story] Display salons list in frontend`
- `[Story] Create salon form in frontend`
- `[Story] Display products list in frontend`
- `[Story] Create order from frontend`

### Acceptance criteria

- API models are typed.
- Forms show client and server validation feedback.
- Loading, empty, error, and success states exist.
- Backend remains the business-rule source of truth.
- No secret is shipped in browser assets.
- Main flow is usable without advanced visual design.

### Risks

- Frontend scope distracts from backend learning.
- Business logic is duplicated in the browser.
- API error handling becomes inconsistent.

### Definition of Done

The main portfolio workflow can be demonstrated through a browser with readable
frontend structure.

### Labels

`epic`, `frontend`, `portfolio`, `priority-medium`

---

## Issue 43

**Title:** `[Task] Generate React TypeScript frontend`

### Goal

Create the minimal frontend foundation for the admin panel.

### Technical requirements

- React + TypeScript.
- Current supported build tool selected deliberately.
- One package manager and committed lockfile.
- Routing/app shell.
- Environment-aware API base URL without secrets.
- Lint, type-check, build, and minimal test.

### Acceptance criteria

- Frontend installs and builds from a clean checkout.
- TypeScript strictness decision is documented.
- Routes support future dashboard, salons, products, orders, and notifications.
- No generated demo business UI remains.
- API configuration is environment-driven.
- Loading/error primitives have a clear home without premature design-system work.

### Suggested branch

`feature/frontend-foundation`

### Labels

`task`, `frontend`, `testing`, `priority-medium`

---

## Issue 44

**Title:** `[Story] Display salons list in frontend`

### User story

As an admin user, I want to see salons in the frontend so that I can browse CRM
customers without using an API client.

### Acceptance criteria

- Salon list calls the paginated backend endpoint.
- Loading, empty, server-error, and populated states are visible.
- Pagination and confirmed filters are usable.
- API error contract is handled consistently.
- Rows use stable identifiers and link to details when available.
- Component tests cover meaningful user-visible states.

### Suggested branch

`feature/frontend-salons-list`

### Labels

`story`, `frontend`, `testing`, `priority-medium`

---

## Issue 45

**Title:** `[Story] Create salon form in frontend`

### User story

As an admin user, I want to create a salon through a form so that I can add CRM
customers without manually sending JSON.

### Acceptance criteria

- Form covers the confirmed create-salon fields.
- Labels, validation messages, submit state, and keyboard use are accessible.
- Client validation improves feedback but backend errors remain authoritative.
- Field violations from the API map to the correct inputs.
- Conflict and unexpected errors have visible, safe feedback.
- Successful creation navigates or confirms the created salon.
- Tests cover success, client validation, server validation, and conflict.

### Suggested branch

`feature/frontend-create-salon`

### Labels

`story`, `frontend`, `testing`, `priority-medium`

---

## Issue 46

**Title:** `[Story] Display products list in frontend`

### User story

As an admin user, I want to browse products in the frontend so that I can inspect
catalogue availability and commercial data.

### Acceptance criteria

- Product list uses backend pagination and supported filters.
- Active/inactive state and price/currency are displayed clearly.
- Loading, empty, and error states are present.
- TypeScript models match the documented API DTO.
- Pagination/filter interactions do not cause accidental duplicate requests.
- Tests cover representative states.

### Suggested branch

`feature/frontend-products-list`

### Labels

`story`, `frontend`, `testing`, `priority-medium`

---

## Issue 47

**Title:** `[Story] Create order from frontend`

### User story

As a sales/admin user, I want to prepare and place an order in the frontend so
that the main business workflow is demonstrable end to end.

### Scope

- Select salon.
- Add active products and quantities.
- Show price snapshots/totals.
- Create draft, add items, and place using backend commands.
- Handle stock/status conflicts.

### Acceptance criteria

- User can create a valid draft and add/remove/edit items.
- Invalid quantities and backend validation are visible.
- Placement has a clear pending/success/failure state.
- Insufficient-stock `409` is explained without losing the draft UI state.
- Duplicate submission is prevented or handled safely.
- Frontend does not reimplement authoritative stock/status rules.
- Tests cover the main success flow and representative failures.

### Suggested branch

`feature/frontend-create-order`

### Labels

`story`, `frontend`, `testing`, `portfolio`, `priority-medium`

---

## Issue 48

**Title:** `[Epic] Docker Compose environment`

### Business description

Make BeautyStock CRM easy to start for development, review, and portfolio
demonstration.

### Technical goal

Containerize runnable components and orchestrate them with PostgreSQL and RabbitMQ
using safe configuration and meaningful health checks.

### Scope

- Multi-stage Dockerfiles for API, worker, and frontend.
- `compose.yml`.
- PostgreSQL and RabbitMQ services.
- Health checks, volumes, network, and environment templates.

### Tasks

- `[Task] Add Dockerfile for backend-api`
- `[Task] Add Dockerfile for backend-worker`
- `[Task] Add Dockerfile for frontend`
- `[Task] Add compose.yml for local development`

### Acceptance criteria

- A clean checkout can start the documented system.
- Images are multi-stage and minimal.
- Processes run non-root where practical.
- Health checks represent readiness rather than simple process existence.
- Real secrets are not committed or baked into images.
- Start, logs, stop, and narrow reset workflows are documented.

### Risks

- Startup order mistaken for readiness.
- Oversized images.
- Development credentials reused outside local environment.

### Definition of Done

The stable local application starts predictably through documented Compose
commands and reports healthy dependencies/components.

### Labels

`epic`, `infrastructure`, `docker`, `priority-high`

---

## Issue 49

**Title:** `[Task] Add Dockerfile for backend-api`

### Goal

Build a reproducible, minimal runtime image for `backend-api`.

### Acceptance criteria

- Dockerfile uses separate build and runtime stages.
- Build uses the Maven Wrapper and selected Java 21 images.
- Runtime contains only required artifacts.
- Application runs as non-root where practical.
- No secret is present in Dockerfile, ARG, ENV, label, or layer.
- JVM options are supplied at runtime.
- Image starts and exposes the documented health/application port.
- `.dockerignore` excludes build output, IDE files, VCS data, and secrets.

### Verification

- Clean image build.
- Container startup against local dependencies.
- Health/readiness check.
- Inspect image size and runtime user.

### Suggested branch

`infra/backend-api-dockerfile`

### Labels

`task`, `backend`, `infrastructure`, `docker`, `testing`, `priority-medium`

---

## Issue 50

**Title:** `[Task] Add Dockerfile for backend-worker`

### Goal

Build a reproducible, minimal runtime image for the RabbitMQ worker.

### Acceptance criteria

- Multi-stage Java 21 build uses the Maven Wrapper.
- Runtime image contains only the worker artifact and required runtime.
- Worker runs non-root where practical.
- Broker/database values are runtime configuration.
- Graceful termination stops message consumption safely.
- Health semantics are defined for a consumer.
- No secret is baked into the image.

### Verification

- Clean image build.
- Worker connects to local RabbitMQ.
- Order event is consumed.
- Termination does not abandon/incorrectly acknowledge in-flight work.

### Suggested branch

`infra/backend-worker-dockerfile`

### Labels

`task`, `backend`, `infrastructure`, `docker`, `rabbitmq`, `priority-medium`

---

## Issue 51

**Title:** `[Task] Add Dockerfile for frontend`

### Goal

Build a small production-like image for the React frontend.

### Acceptance criteria

- Build and runtime stages are separate.
- Locked dependencies are installed reproducibly.
- Runtime serves static assets using the selected minimal server.
- API URL strategy is explicit: build-time or runtime configuration.
- No secret is included in browser assets.
- Non-root execution and health check are used where supported.
- Direct route refresh works with frontend routing.

### Verification

- Clean build.
- Static asset serving.
- Direct navigation/refresh.
- API connectivity in Compose.

### Suggested branch

`infra/frontend-dockerfile`

### Labels

`task`, `frontend`, `infrastructure`, `docker`, `priority-medium`

---

## Issue 52

**Title:** `[Task] Add compose.yml for local development`

### Goal

Provide one documented local environment for API, worker, PostgreSQL, RabbitMQ,
and optionally frontend.

### Acceptance criteria

- Compose defines the currently runnable components and required dependencies.
- PostgreSQL and RabbitMQ have meaningful health checks.
- API/worker do not rely only on container creation order.
- Environment values come from safe defaults/templates; real `.env` is ignored.
- Named volumes and their narrow reset procedure are documented.
- Ports and networks expose only what local development needs.
- `docker compose config` validates.
- A clean startup and main smoke flow are verified.

### Suggested branch

`infra/docker-compose`

### Labels

`task`, `infrastructure`, `docker`, `database`, `rabbitmq`, `priority-high`

---

## Issue 53

**Title:** `[Epic] Kubernetes deployment`

### Business description

Demonstrate how the stable containerized application can run on an orchestrated
platform.

### Technical goal

Define Kubernetes workloads, networking, configuration, secrets, health probes,
resources, and migration/rollout behaviour.

### Scope

- API and worker Deployments.
- Services and Ingress.
- ConfigMap and Secret templates.
- Liveness/readiness probes.
- Resource requests/limits.
- Migration and rollback plan.

### Tasks

- `[Task] Add Kubernetes manifests for backend-api`
- `[Task] Add Kubernetes manifests for backend-worker`
- `[Task] Add ConfigMap and Secret templates`
- `[Task] Add liveness and readiness probes`

### Acceptance criteria

- Manifests validate and deploy to the documented cluster.
- Configuration and secrets are separated.
- Probes have correct operational meaning.
- Resources are specified and justified.
- Database migration is controlled.
- Rollout and rollback are documented.

### Risks

- Kubernetes work begins before application stability.
- Base64 values are mistaken for secret protection.
- Probe failures cause restart loops.
- Multiple replicas run unsafe migrations.

### Definition of Done

The stable images deploy, become ready, can be observed, and can be rolled back in
the documented environment.

### Labels

`epic`, `infrastructure`, `kubernetes`, `priority-low`

---

## Issue 54

**Title:** `[Task] Add Kubernetes manifests for backend-api`

### Goal

Deploy `backend-api` with an internal Service and controlled rollout.

### Acceptance criteria

- Deployment uses an immutable/versioned image reference.
- Labels/selectors and Service targeting match.
- Configuration comes from ConfigMap/Secret references.
- Container port, graceful shutdown, and termination period are defined.
- Resource requests/limits are present.
- Rolling-update settings are compatible with readiness and schema strategy.
- Manifest validates and API becomes reachable in the test cluster.

### Suggested branch

`infra/kubernetes-backend-api`

### Labels

`task`, `backend`, `infrastructure`, `kubernetes`, `priority-low`

---

## Issue 55

**Title:** `[Task] Add Kubernetes manifests for backend-worker`

### Goal

Deploy the RabbitMQ worker with safe configuration and consumer lifecycle.

### Acceptance criteria

- Deployment uses an immutable/versioned worker image.
- No public Service is added without a real requirement.
- Broker/database configuration uses ConfigMap/Secret references.
- Graceful termination stops consumption correctly.
- Resource requests/limits and replica count are justified.
- Health/readiness behaviour reflects broker connectivity and processing ability.
- Manifest validates and consumes a test event in the target cluster.

### Suggested branch

`infra/kubernetes-backend-worker`

### Labels

`task`, `backend`, `infrastructure`, `kubernetes`, `rabbitmq`, `priority-low`

---

## Issue 56

**Title:** `[Task] Add ConfigMap and Secret templates`

### Goal

Separate non-secret configuration from secret placeholders for Kubernetes
deployments.

### Acceptance criteria

- ConfigMap includes only non-sensitive values.
- Secret manifest contains placeholders/example keys, never real credentials.
- Documentation states that base64 is encoding, not encryption.
- Required environment variables map consistently to application properties.
- Missing required values fail clearly.
- Azure secret-store/managed-identity integration remains a documented follow-up.

### Suggested branch

`infra/kubernetes-config`

### Labels

`task`, `infrastructure`, `kubernetes`, `documentation`, `priority-low`

---

## Issue 57

**Title:** `[Task] Add liveness and readiness probes`

### Goal

Configure probes that restart unrecoverable processes and route work only to
ready instances.

### Acceptance criteria

- API liveness does not fail solely because PostgreSQL is temporarily unavailable.
- API readiness reflects required dependency/migration state.
- Worker readiness reflects its ability to consume required queues.
- Probe paths use controlled Actuator health groups.
- Initial delay, timeout, period, and threshold values are measured/justified.
- Tests or deployment exercise demonstrate dependency loss and recovery without
  restart loops.

### Suggested branch

`infra/kubernetes-health-probes`

### Labels

`task`, `infrastructure`, `kubernetes`, `backend`, `testing`, `priority-medium`

---

## Issue 58

**Title:** `[Epic] Cloud deployment`

### Business description

Make a controlled, observable BeautyStock CRM portfolio environment available in
Azure.

### Technical goal

Plan and implement registry, compute, managed PostgreSQL, messaging, secrets,
networking, monitoring, cost controls, and teardown.

### Scope

- Azure deployment plan.
- Azure Container Registry.
- AKS or deliberately reconsidered compute.
- Azure Database for PostgreSQL.
- RabbitMQ hosting decision.
- Identity, secrets, observability, budget, and teardown.

### Tasks

- `[Task] Prepare Azure deployment plan`
- `[Task] Prepare container registry plan`
- `[Task] Prepare managed PostgreSQL plan`

### Acceptance criteria

- Architecture and data flow are documented before provisioning.
- Cost estimate, budget alert, tags, and teardown plan exist.
- Credentials are external to Git.
- Database uses secure transport and controlled network access.
- Deployment has health, logs, metrics, and rollback path.
- Any public exposure has explicit security limitations.

### Risks

- Unexpected cost.
- Weak public access controls.
- AKS operational overhead outweighing portfolio value.

### Definition of Done

A reproducible, cost-controlled deployment is demonstrated, or a justified
simpler Azure compute choice is recorded and used.

### Labels

`epic`, `infrastructure`, `kubernetes`, `portfolio`, `priority-low`

---

## Issue 59

**Title:** `[Task] Prepare Azure deployment plan`

### Goal

Select and document the Azure topology before any billable resources are created.

### Scope

- Region and environment.
- AKS versus Azure Container Apps comparison.
- Networking and TLS.
- RabbitMQ hosting.
- Key Vault/managed identity.
- Observability.
- Cost estimate and teardown.

### Acceptance criteria

- Component and network diagram exists.
- Compute decision includes educational value, complexity, and cost.
- Required Azure resources and ownership are listed.
- Secret/configuration flow is documented.
- Deployment, rollback, backup, and teardown sequences are described.
- Budget/alert threshold is chosen before provisioning.

### Suggested branch

`docs/azure-deployment-plan`

### Labels

`task`, `documentation`, `infrastructure`, `kubernetes`, `portfolio`, `priority-low`

---

## Issue 60

**Title:** `[Task] Prepare container registry plan`

### Goal

Define how versioned API, worker, and frontend images are stored and promoted in
Azure Container Registry.

### Acceptance criteria

- Image repositories and naming convention are documented.
- Immutable commit-based tag and human-readable release tag strategy is defined.
- Authentication uses managed identity/service principal with least privilege.
- Build and deployment permissions are separated where practical.
- Image scanning, retention, and cleanup are planned.
- Rollback can identify the exact previous image.

### Suggested branch

`docs/container-registry-plan`

### Labels

`task`, `documentation`, `infrastructure`, `docker`, `priority-low`

---

## Issue 61

**Title:** `[Task] Prepare managed PostgreSQL plan`

### Goal

Plan Azure Database for PostgreSQL connectivity, security, backup, migration, and
cost.

### Acceptance criteria

- Supported PostgreSQL version and sizing assumption are recorded.
- TLS and network access are mandatory.
- Application and migration credential strategy is defined.
- Backup/retention and restore exercise are planned.
- Connection pool and failover considerations are documented.
- Secrets remain outside Git.
- Monthly cost and teardown/data-retention implications are estimated.

### Suggested branch

`docs/azure-postgresql-plan`

### Labels

`task`, `documentation`, `database`, `infrastructure`, `priority-low`

---

## Issue 62

**Title:** `[Epic] Observability and JVM diagnostics`

### Business description

Make the system's health and failures understandable to developers and operators.

### Technical goal

Add health, structured logs, metrics, correlation, and safe JVM diagnostic
procedures.

### Scope

- Actuator health.
- Structured logging.
- JVM/application/RabbitMQ metrics.
- Correlation/event IDs.
- GC logs, heap dumps, and thread dumps.
- One documented diagnostic exercise.

### Tasks

- `[Task] Add Actuator health endpoints`
- `[Task] Add structured logging plan`
- `[Task] Add GC log and heap dump diagnostic notes`

### Acceptance criteria

- Liveness and readiness have different meanings.
- Logs avoid secrets/personal data and support flow correlation.
- Metrics use bounded-cardinality tags.
- Management exposure is safe.
- Diagnostic artifacts are treated as sensitive.
- At least one failure is diagnosed using documented evidence.

### Risks

- Sensitive data in logs/dumps.
- Unbounded metric cardinality.
- Publicly exposed management endpoints.

### Definition of Done

Core requests/events can be traced and a reviewer can follow safe JVM diagnosis
steps.

### Labels

`epic`, `backend`, `infrastructure`, `portfolio`, `priority-medium`

---

## Issue 63

**Title:** `[Task] Add Actuator health endpoints`

### Goal

Expose safe application health information for local operations and future
platform probes.

### Acceptance criteria

- Actuator dependency/exposure is minimal and documented.
- Liveness and readiness health groups are configured separately.
- Readiness includes required dependencies only.
- Health response does not leak credentials, internal topology, or stack traces.
- Management endpoints are not broadly exposed by default.
- Tests cover healthy state and representative dependency failure/recovery.

### Suggested branch

`feature/actuator-health`

### Labels

`task`, `backend`, `infrastructure`, `testing`, `priority-medium`

---

## Issue 64

**Title:** `[Task] Add structured logging plan`

### Goal

Define a consistent log format and context strategy before adding production-like
logging configuration.

### Scope

- JSON/structured fields.
- Log levels and exception policy.
- Correlation ID for HTTP.
- Event ID/correlation for RabbitMQ.
- Sensitive-data exclusions.
- Retention/aggregation direction.

### Acceptance criteria

- Required log fields and environment differences are documented.
- Request/event flows can be correlated.
- Passwords, tokens, full contact data, and message payloads are excluded by
  default.
- Expected business errors are not logged as noisy stack traces.
- Unexpected failures retain diagnostic context.
- Metric/log cardinality and volume risks are addressed.

### Suggested branch

`docs/structured-logging-plan`

### Labels

`task`, `documentation`, `backend`, `infrastructure`, `priority-medium`

---

## Issue 65

**Title:** `[Task] Add GC log and heap dump diagnostic notes`

### Goal

Document safe, repeatable JVM diagnostics for memory and garbage-collection
problems.

### Scope

- Heap, stack, and GC conceptual summary.
- Java 21 GC log configuration.
- Heap dump on OOM and manual dump.
- Thread dump procedure.
- Container memory/resource context.
- Secure artifact storage and cleanup.

### Acceptance criteria

- Commands/options match the selected Java 21 runtime and are verified.
- Notes explain when to use GC logs, heap dumps, and thread dumps.
- Heap-dump path capacity and permissions are addressed.
- Dumps/logs are explicitly treated as potentially sensitive.
- One controlled diagnostic exercise is described with expected observations.
- No diagnostic artifact is committed to Git.

### Suggested branch

`docs/jvm-diagnostics`

### Labels

`task`, `documentation`, `backend`, `portfolio`, `priority-medium`

---

## Issue 66

**Title:** `[Epic] Portfolio polish and README`

### Business description

Present BeautyStock CRM so a recruiter or engineer can quickly understand its
business value, architecture, quality, and working evidence.

### Technical goal

Curate diagrams, screenshots, demonstrations, test strategy, decisions,
limitations, and reliable setup instructions.

### Scope

- Final README/navigation.
- Architecture diagram.
- Screenshots/demo.
- Test and quality evidence.
- Trade-offs, limitations, and lessons learned.

### Tasks

- `[Task] Add architecture diagram to README`
- `[Task] Add screenshots section`
- `[Task] Add final portfolio explanation`

### Acceptance criteria

- README explains the business problem, architecture, stack, and main flow.
- Setup works from a clean checkout.
- Visuals show real functionality, not mock claims.
- Engineering decisions and representative tests are linked.
- Limitations and future work are honest.
- Repository history demonstrates incremental implementation and review.

### Risks

- Polishing before correctness.
- Stale screenshots.
- Unsupported claims.

### Definition of Done

A reviewer can understand, run, and evaluate the project as evidence of Mid Java
Backend Developer skills.

### Labels

`epic`, `documentation`, `portfolio`, `priority-medium`

---

## Issue 67

**Title:** `[Task] Add architecture diagram to README`

### Goal

Make the system boundary and main data/event flows understandable at a glance.

### Acceptance criteria

- Diagram shows frontend, API modules, PostgreSQL, RabbitMQ, and worker.
- Synchronous request and asynchronous event paths are distinguishable.
- Diagram matches implemented reality at the time it is added.
- Source is editable and stored/referenced consistently.
- README remains readable on GitHub and has accessible explanatory text.
- Architecture document provides the deeper explanation.

### Suggested branch

`docs/readme-architecture-diagram`

### Labels

`task`, `documentation`, `portfolio`, `priority-medium`

---

## Issue 68

**Title:** `[Task] Add screenshots section`

### Goal

Show credible visual evidence of the working admin workflows.

### Acceptance criteria

- Screenshots cover the most representative completed views.
- Data is synthetic and contains no real customer information.
- Images have meaningful filenames, captions, and alt text.
- Screenshots match the current UI and are reasonably optimized.
- README does not become dominated by large images.
- Update responsibility is noted when UI changes.

### Suggested branch

`docs/readme-screenshots`

### Labels

`task`, `documentation`, `frontend`, `portfolio`, `priority-medium`

---

## Issue 69

**Title:** `[Task] Add final portfolio explanation`

### Goal

Explain what the project proves, how key decisions were made, and what the owner
learned.

### Scope

- Business problem and main workflow.
- Architecture and module boundaries.
- JPA/transaction/concurrency evidence.
- RabbitMQ delivery/idempotency/outbox trade-off.
- Test strategy.
- Docker/Kubernetes/Azure path.
- JVM diagnostics.
- Limitations and next improvements.

### Acceptance criteria

- Claims link to working code, tests, documentation, Issues, or PRs.
- Explanation distinguishes completed work from planned work.
- Key trade-offs include consequences, not technology-name lists.
- Owner's manual implementation and review process is evident.
- Known limitations such as security or dual-write phase are explicit.
- Text is concise enough for a portfolio reviewer while linking to deeper docs.

### Suggested branch

`docs/final-portfolio-explanation`

### Labels

`task`, `documentation`, `portfolio`, `priority-medium`
