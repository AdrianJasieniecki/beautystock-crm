# Progress

Last updated: 2026-07-27

## Current phase

**Phase 1 — Backend API foundation**

The documentation and GitHub delivery foundation, the runnable `backend-api`
skeleton, the shared API error response model, and global exception handling are
published. Issues #7, #9, and #8 are complete. The next recommended
implementation task is
`[Task] Add validation dependency and example request validation` (#10).

## Completed

- Confirmed repository: `AdrianJasieniecki/beautystock-crm`.
- Confirmed default branch: `Production`.
- Published the documentation foundation through
  [PR #1](https://github.com/AdrianJasieniecki/beautystock-crm/pull/1).
- Defined the modular-monolith plus worker architecture, roadmap, backlog, API
  plan, review rules, local/deployment plans, and initial ADRs.
- Created the complete agreed label taxonomy.
- Created 69 open GitHub Issues (#2–#70) from the reviewed import package.
- Created and linked the public
  [BeautyStock CRM — Learning & Delivery](https://github.com/users/AdrianJasieniecki/projects/5)
  project.
- Configured `Backlog`, `Current Work`, and `Completed` project views.
- Configured project statuses from `Proposed` through `Done`.
- Configured automatic addition of new open repository Issues to the project.
- Removed automatic Issue closure when an item moves to `Done`; closure remains
  an explicit owner decision.
- Moved Issue #7 to `Ready`.
- Merged the documentation synchronization PR and marked that project item as
  `Done`.
- Installed JDK 21 for the backend toolchain.
- Implemented the minimal `backend-api` skeleton with Spring Boot 4.1.0, Java 21,
  Maven 3.9.16 through the project wrapper, Spring MVC, a local profile, and a
  context smoke test.
- Verified the Java 21 build, context test, local-profile startup on port `8080`,
  and the expected `404` response from `GET /` without a sample endpoint.
- Merged the backend foundation through
  [PR #72](https://github.com/AdrianJasieniecki/beautystock-crm/pull/72).
- Closed
  [Issue #7](https://github.com/AdrianJasieniecki/beautystock-crm/issues/7)
  through the PR's `Closes #7` reference and moved its project item to `Done`.
- Implemented immutable `ApiErrorResponse` and `ApiFieldViolation` records with
  deliberate optional-field serialization and a defensive copy of validation
  violations.
- Verified the API error JSON contract with a focused `@JsonTest`, including the
  exact public schema, numeric status, UTC timestamp, optional fields, and list
  immutability.
- Verified `./mvnw clean verify` on Java 21 with 8 tests and no failures.
- Merged the API error response model through
  [PR #75](https://github.com/AdrianJasieniecki/beautystock-crm/pull/75).
- Closed
  [Issue #9](https://github.com/AdrianJasieniecki/beautystock-crm/issues/9)
  through the PR's `Closes #9` reference and moved its project item to `Done`.
- Implemented one global `@RestControllerAdvice` based on
  `ResponseEntityExceptionHandler`, including safe mappings for malformed JSON,
  application and framework not-found failures, conflicts, built-in MVC
  failures, and unexpected `5xx` failures.
- Preserved Spring-selected statuses and important `Allow`/`Accept` headers,
  omitted unavailable optional fields, and logged unexpected failures once with
  the exception and request path without exposing diagnostics to clients.
- Verified the handler with focused `@WebMvcTest` cases for `400`, `404`, `405`,
  `409`, `415`, and `500`, plus the existing JSON contract and context tests.
- Verified `./mvnw clean verify` on Java 21 with 16 tests and no failures.
- Merged global exception handling through
  [PR #78](https://github.com/AdrianJasieniecki/beautystock-crm/pull/78).
- Closed
  [Issue #8](https://github.com/AdrianJasieniecki/beautystock-crm/issues/8)
  through the PR's `Closes #8` reference and moved its project item to `Done`.
- Closed foundation Issues
  [#4](https://github.com/AdrianJasieniecki/beautystock-crm/issues/4),
  [#5](https://github.com/AdrianJasieniecki/beautystock-crm/issues/5), and
  [#2](https://github.com/AdrianJasieniecki/beautystock-crm/issues/2) after
  confirming their acceptance criteria and moved their project items to `Done`.

## In progress

- No application implementation task is in progress.
- Issue #10 has a reviewed implementation brief, its dependencies on the
  response model and global exception handling are complete, and it is `Ready`
  in the GitHub Project.
- PostgreSQL and Flyway configuration remains proposed after the validation
  foundation.

## Next step

The repository owner creates `feature/api-validation-foundation` from current
`Production` and manually implements
`[Task] Add validation dependency and example request validation` (#10) using
the reviewed Issue brief. Move #10 to `In progress` when implementation begins.

## Open risks and decisions

| Item | Status | Next action |
| --- | --- | --- |
| Backend foundation versions | Resolved for current skeleton | Spring Boot 4.1.0 and Maven 3.9.16 via the project wrapper; review upgrades deliberately |
| Maven monorepo structure | Resolved for current stage | Keep `backend-api` independent; revisit when another build requires aggregation (ADR-015) |
| Salon email uniqueness | Open | Confirm business rule during Salon story refinement |
| Order state semantics | Open | Define transition table before implementing placement |
| Inventory locking | Proposed | Validate optimistic locking with concurrency tests |
| Message dual-write | Known risk | Use documented simple phase, then implement Outbox |
| Authentication/authorization | Deferred | Add explicit security milestone before public production use |
| Azure cost/topology | Deferred | Estimate before provisioning any resources |

## Links

- [Repository](https://github.com/AdrianJasieniecki/beautystock-crm)
- [Issues](https://github.com/AdrianJasieniecki/beautystock-crm/issues)
- [Pull Requests](https://github.com/AdrianJasieniecki/beautystock-crm/pulls)
- [GitHub Project](https://github.com/users/AdrianJasieniecki/projects/5)
- [Documentation foundation PR](https://github.com/AdrianJasieniecki/beautystock-crm/pull/1)
- [Documentation foundation Issue](https://github.com/AdrianJasieniecki/beautystock-crm/issues/3)
- [Next implementation Issue](https://github.com/AdrianJasieniecki/beautystock-crm/issues/10)
- [Roadmap](ROADMAP.md)
- [Backlog](BACKLOG.md)
- [Architecture decisions](DECISIONS.md)
- [Copy-ready GitHub Issues](GITHUB_ISSUES.md)

## Update rules

Update this file when:

- a task moves to In progress or In review;
- an Issue is blocked;
- a PR is merged;
- the next recommended task changes;
- a new architectural risk affects sequencing.

Do not mark an Issue Done or closed without the repository owner's decision.
