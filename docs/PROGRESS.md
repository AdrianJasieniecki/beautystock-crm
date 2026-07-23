# Progress

Last updated: 2026-07-23

## Current phase

**Phase 0 — Project foundation and documentation**

The remote repository is functionally empty: `Production` contains only an
initial one-line README. No application or infrastructure implementation exists.

## Completed

- Confirmed repository: `AdrianJasieniecki/beautystock-crm`.
- Confirmed default branch: `Production`.
- Confirmed GitHub integration reports read, push, triage, maintain, and admin
  access.
- Confirmed no open Issues or Pull Requests existed before project setup.
- Defined proposed modular-monolith plus worker architecture.
- Prepared the initial documentation set locally.
- Defined the roadmap, backlog, API plan, review rules, and initial ADRs.
- Prepared copy-ready bodies for all 69 initial GitHub Issues.

## In progress

- Resolve the GitHub connector write-approval failure.
- Publish documentation on `docs/project-foundation`.
- Open a documentation PR to `Production`.
- Create the initial GitHub Issues from the prepared import package.

## Next step

Publish the prepared documentation and Issues when GitHub write operations become
available. The repository owner then reviews the documentation PR and decides
whether to merge it. After that, refine and manually implement:

`[Task] Generate Spring Boot backend-api skeleton`

Suggested owner-created branch:

`feature/backend-api-foundation`

## Open risks and decisions

| Item | Status | Next action |
| --- | --- | --- |
| Exact Spring Boot/dependency versions | Open | Decide in backend skeleton Issue using current official support information |
| Maven monorepo structure | Open | Compare parent multi-module build with independent builds |
| Salon email uniqueness | Open | Confirm business rule during Salon story refinement |
| Order state semantics | Open | Define transition table before implementing placement |
| Inventory locking | Proposed | Validate optimistic locking with concurrency tests |
| Message dual-write | Known risk | Use documented simple phase, then implement Outbox |
| Authentication/authorization | Deferred | Add explicit security milestone before public production use |
| Azure cost/topology | Deferred | Estimate before provisioning any resources |
| GitHub connector writes | Blocked | Internal approval reviewer rejects writes despite explicit owner approval |

## Links

- [Repository](https://github.com/AdrianJasieniecki/beautystock-crm)
- [Issues](https://github.com/AdrianJasieniecki/beautystock-crm/issues)
- [Pull Requests](https://github.com/AdrianJasieniecki/beautystock-crm/pulls)
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
