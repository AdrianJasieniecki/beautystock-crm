# Review Checklist

Use the relevant sections for the change. A small PR does not need every item, but
skipped high-risk areas should be consciously marked not applicable.

## 1. Scope and acceptance criteria

- [ ] The PR links to the correct Issue.
- [ ] Every acceptance criterion is addressed or explicitly deferred.
- [ ] The diff is focused and does not include unrelated refactoring.
- [ ] The implementation matches the documented business behaviour.
- [ ] Out-of-scope behaviour is not silently added.
- [ ] Documentation is updated where the contract or workflow changed.
- [ ] Commit/PR history is understandable and contains no generated noise.

## 2. Backend and clean code

- [ ] Names express business intent.
- [ ] Controllers contain only transport orchestration.
- [ ] Business rules live in domain/application code.
- [ ] Constructor injection is used.
- [ ] Responsibilities are small and cohesive.
- [ ] Abstractions solve a current problem rather than a hypothetical one.
- [ ] Package/module boundaries are respected.
- [ ] One module does not access another module's repository directly.
- [ ] Exceptions are meaningful and are not used for normal branching when a
  result/type would be clearer.
- [ ] Logging uses a logger, never `System.out.println`.
- [ ] Logs do not contain secrets or unnecessary personal data.
- [ ] Time-dependent logic uses an injectable clock where useful.
- [ ] Money does not use `float` or `double`.

## 3. JPA, Hibernate, and transactions

- [ ] API DTOs and JPA entities are separate.
- [ ] No entity is returned from a controller.
- [ ] Update flow is load → modify managed entity → commit.
- [ ] Detached objects are not reconstructed from request data and blindly saved.
- [ ] `save()` use is deliberate, especially for existing entities.
- [ ] Transaction boundary is at the application use case/service.
- [ ] Read/write and propagation choices are understood.
- [ ] Remote calls are not accidentally placed inside long transactions.
- [ ] Flush is not forced without a reason.
- [ ] Lazy loading will not fail after transaction close.
- [ ] List queries are checked for N+1.
- [ ] Fetch joins/entity graphs/projections do not break pagination.
- [ ] Relationships have intentional direction and ownership.
- [ ] `CascadeType.ALL` and orphan removal are justified.
- [ ] JPA relationships are excluded from unsafe `toString`, `equals`, and
  `hashCode`.
- [ ] Entity equality strategy is deliberate for pre/post-persist lifecycle.
- [ ] Database constraints protect important invariants.
- [ ] Indexes match real constraints or query patterns.
- [ ] Optimistic/pessimistic locking behaviour has conflict tests where relevant.
- [ ] Concurrent inventory changes cannot silently lose updates.
- [ ] Rollback behaviour is tested for multi-step writes.

## 4. REST API and DTOs

- [ ] Resource path and method follow `docs/API.md`.
- [ ] Status code matches the outcome.
- [ ] `201` responses include a useful representation and/or `Location` when
  appropriate.
- [ ] Request and response DTOs expose only intended fields.
- [ ] Input cannot overwrite IDs, audit fields, versions, or protected statuses.
- [ ] Bean Validation is applied at the API boundary.
- [ ] Business validation is not incorrectly encoded only as annotations.
- [ ] Error responses use the shared contract.
- [ ] Validation violations identify safe, useful fields.
- [ ] Internal exception, SQL, or stack trace is not exposed.
- [ ] Collection endpoints are paginated and page size is capped.
- [ ] Sort fields are allow-listed.
- [ ] Filtering semantics and empty values are tested.
- [ ] Dates/times and timezone assumptions are explicit.
- [ ] Backward compatibility impact is assessed.
- [ ] OpenAPI/docs examples match actual JSON.

## 5. Database and Flyway

- [ ] Schema change has a Flyway migration.
- [ ] Hibernate automatic schema update is not used as migration.
- [ ] Migration works from an empty database.
- [ ] Migration works from the previous committed schema.
- [ ] Already shared/merged migration history is not casually rewritten.
- [ ] Naming is consistent for tables, columns, constraints, and indexes.
- [ ] Nullability matches business meaning.
- [ ] Unique/check/foreign-key constraints are present where needed.
- [ ] Constraint names are stable enough for diagnostics/mapping.
- [ ] Defaults do not hide missing application behaviour.
- [ ] Data migration/backfill is safe for existing rows.
- [ ] Index cost and selectivity are considered.
- [ ] Query plans are inspected for important list/filter paths when relevant.

## 6. RabbitMQ and asynchronous processing

- [ ] Event is a past-tense business fact.
- [ ] Envelope contains `eventId`, `eventType`, `eventVersion`, `occurredAt`,
  `aggregateId`, and payload.
- [ ] Event schema is versioned and documented.
- [ ] Producer does not publish uncommitted/rolled-back state.
- [ ] Database/message dual-write risk is addressed or explicitly documented.
- [ ] Consumer handles duplicate delivery safely.
- [ ] Acknowledgement mode and failure behaviour are understood.
- [ ] Retryable and non-retryable exceptions are distinguished.
- [ ] Retry count/backoff is bounded.
- [ ] Exhausted or invalid messages reach an observable DLQ.
- [ ] Poison-message replay procedure is considered.
- [ ] Logs include event/correlation ID.
- [ ] Payload excludes secrets and unnecessary personal data.
- [ ] RabbitMQ Testcontainers tests cover representative integration behaviour.

## 7. Tests

- [ ] Test type is the smallest suitable type.
- [ ] Pure domain logic is tested without Spring where possible.
- [ ] Unit tests assert behaviour, not only mock interactions.
- [ ] `@WebMvcTest` covers HTTP mapping, validation, JSON, and error handling where
  useful.
- [ ] `@DataJpaTest`/focused integration test covers mappings, queries, and
  constraints.
- [ ] PostgreSQL Testcontainers is used when database behaviour matters.
- [ ] RabbitMQ Testcontainers is used when broker behaviour matters.
- [ ] `@SpringBootTest` is reserved for meaningful cross-layer flows.
- [ ] Success, boundary, and important failure scenarios are covered.
- [ ] Tests use descriptive names and Arrange/Act/Assert readability.
- [ ] Assertions verify important state and output.
- [ ] Tests are deterministic and independent.
- [ ] Time, random IDs, and concurrency are controlled where required.
- [ ] Test data builders/fixtures improve readability without hiding intent.
- [ ] Concurrency tests fail for the broken behaviour and pass for the fix.
- [ ] No ignored/flaky test is introduced without an Issue and explanation.

## 8. Docker and Docker Compose

- [ ] Dockerfile uses a multi-stage build.
- [ ] Runtime image contains only required artifacts.
- [ ] Process runs as non-root where practical.
- [ ] Image has an explicit, reproducible build.
- [ ] Secrets are not baked into layers or build arguments.
- [ ] `.dockerignore` excludes irrelevant and sensitive files.
- [ ] Compose health checks represent dependency readiness.
- [ ] Service configuration comes from environment variables.
- [ ] Example development values are clearly non-production.
- [ ] Volumes, ports, and network exposure are minimal and documented.
- [ ] Clean startup and reset workflows are tested.

## 9. Kubernetes

- [ ] Deployment and Service selectors match.
- [ ] Liveness and readiness probes have different operational meaning.
- [ ] Probe paths, delays, timeouts, and thresholds are appropriate.
- [ ] Resource requests and limits are set and justified.
- [ ] ConfigMap contains non-secret configuration only.
- [ ] Secret template contains placeholders, not real values.
- [ ] Base64 is not described as encryption.
- [ ] Ingress/TLS and public exposure are intentional.
- [ ] Database migration runs once with a controlled strategy.
- [ ] Graceful shutdown and termination grace period are considered.
- [ ] Rolling update cannot serve incompatible schema/API state.
- [ ] Manifests validate and deploy to the documented cluster.

## 10. Frontend

- [ ] Components have clear responsibilities.
- [ ] Props and API models are typed.
- [ ] Routing supports direct navigation and refresh.
- [ ] Forms show field and server errors.
- [ ] Loading, empty, success, and failure states are present.
- [ ] API error handling uses the shared backend contract.
- [ ] Backend remains the business-rule source of truth.
- [ ] Accessibility basics are considered: labels, keyboard use, headings,
  contrast.
- [ ] No secret is shipped in browser configuration.
- [ ] Network requests are not duplicated accidentally.
- [ ] Lists use stable keys and pagination.
- [ ] Tests focus on meaningful user behaviour.

## 11. Security and secrets

- [ ] No password, token, private key, connection secret, or real `.env` is in the
  diff or history.
- [ ] Example credentials are clearly local-only.
- [ ] Sensitive values are not logged or returned in errors.
- [ ] Input length and format are bounded.
- [ ] CORS is explicit and environment-aware.
- [ ] SQL uses parameter binding through safe APIs.
- [ ] File/CSV input is size- and content-validated when introduced.
- [ ] Management endpoints are appropriately exposed.
- [ ] Dependencies have no known blocking vulnerabilities.
- [ ] Authentication/authorization limitations are documented if not implemented.
- [ ] Heap dumps and diagnostic artifacts are treated as sensitive.

## 12. Observability and JVM

- [ ] Logs make the use case traceable without excessive noise.
- [ ] Correlation/event IDs propagate where useful.
- [ ] Metrics have bounded cardinality.
- [ ] Health indicators do not leak secret configuration.
- [ ] Readiness reacts correctly to required dependencies.
- [ ] GC/heap/thread diagnostic settings match the runtime and storage limits.
- [ ] Heap dump path is writable, bounded, and secured.
- [ ] Shutdown and consumer interruption are visible and graceful.

## 13. Pre-merge decision

- [ ] No blocking review comment remains unresolved.
- [ ] Required checks pass.
- [ ] Manual verification steps and results are in the PR.
- [ ] The Issue has a completion summary.
- [ ] `docs/PROGRESS.md` and relevant contracts are current.
- [ ] The repository owner explicitly approves merge.
- [ ] The repository owner explicitly decides whether to close the Issue.

