# Progress

Last updated: 2026-07-24

## Current phase

**Phase 1 — Backend API foundation**

The documentation and GitHub delivery foundation are published. The first
owner-implemented task, `[Task] Generate Spring Boot backend-api skeleton` (#7),
has been merged and closed. No implementation Issue is currently active.

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

## In progress

- No application implementation task is in progress.
- The next candidate,
  `[Task] Add global exception handling contract` (#8), remains `Proposed` in
  the GitHub Project.

## Next step

Refine
`[Task] Add global exception handling contract` (#8), confirm its implementation
guidance and acceptance criteria, and move it from `Proposed` to `Ready`. The
repository owner can then create its implementation branch.

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
- [Next implementation Issue](https://github.com/AdrianJasieniecki/beautystock-crm/issues/8)
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
