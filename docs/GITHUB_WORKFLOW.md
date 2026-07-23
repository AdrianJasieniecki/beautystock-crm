# GitHub Workflow

## 1. Responsibility model

- The repository owner selects and manually implements learning tasks.
- Codex acts as architect, mentor, backlog/documentation maintainer, and reviewer.
- Codex does not implement application or infrastructure code unless explicitly
  asked to “implement it” or “write the code.”
- Codex may update documentation, Issues, PR descriptions, and review comments.
- Only the owner decides to merge Pull Requests and close Issues.

## 2. Branch strategy

`Production` is the protected integration/default branch.

Create one focused branch per Issue from an up-to-date `Production`.

Recommended patterns:

| Change | Pattern | Example |
| --- | --- | --- |
| Documentation | `docs/<topic>` | `docs/project-foundation` |
| Backend feature | `feature/<topic>` | `feature/salon-crm` |
| Infrastructure | `infra/<topic>` | `infra/docker-compose` |
| Bug fix | `fix/<topic>` | `fix/inventory-lock-conflict` |
| Refactoring | `refactor/<topic>` | `refactor/order-mapping` |
| Testing | `test/<topic>` | `test/order-concurrency` |

Use lower-case kebab-case. Avoid personal names, vague names such as `changes`,
and long lists of scope.

The owner creates implementation branches. Codex may create documentation
branches within the explicitly allowed documentation workflow.

## 3. Commit convention

Use small, imperative commits with an optional conventional prefix:

```text
docs: define project architecture
feat: add salon creation use case
test: cover duplicate salon email
fix: prevent negative available stock
refactor: separate order response mapping
build: add PostgreSQL Testcontainers dependency
infra: add RabbitMQ health check
```

Guidelines:

- one logical reason per commit;
- do not mix formatting of unrelated files;
- do not commit broken checkpoints to the PR branch unless clearly marked and
  later cleaned before review;
- never place secrets in a commit, even if a later commit removes them;
- reference the Issue in the PR rather than forcing an Issue number into every
  commit.

## 4. Issue workflow

1. **Proposed:** idea exists but needs refinement.
2. **Ready:** goal, scope, acceptance criteria, dependencies, tests, and risks are
   clear.
3. **In progress:** owner has selected the Issue and created a branch.
4. **In review:** PR is open to `Production`.
5. **Blocked:** a named dependency/decision prevents progress.
6. **Done:** DoD is met, PR is merged, and owner approves closure.

Before implementation, an Issue should include:

- user/business goal;
- business value;
- technical scope;
- acceptance criteria;
- technical notes;
- suggested implementation steps;
- expected tests;
- likely files/packages;
- risks and pitfalls;
- review checklist;
- proposed branch name;
- labels and dependencies.

## 5. Pull Request rules

- Target `Production` unless the owner explicitly selects another base.
- Open a draft PR when implementation is still in progress.
- Keep PRs small enough for a coherent review.
- Link the Issue with `Relates to #...`; use `Closes #...` only when the owner wants
  automatic closure after merge.
- Do not merge documentation or implementation without owner approval.
- Do not add unrelated cleanup to “save time.”
- If a PR grows, split it by executable dependency rather than arbitrary file
  count.

Suggested PR description:

```markdown
## Goal

What user or technical outcome this PR provides.

## Changes

- Focused change 1
- Focused change 2

## Verification

- [ ] Build
- [ ] Unit tests
- [ ] Integration/slice tests
- [ ] Manual API/UI verification

## Self-review

- [ ] Acceptance criteria checked
- [ ] No entity exposed through API
- [ ] Transaction/query behaviour reviewed
- [ ] No secrets committed
- [ ] Documentation updated

## Risks / follow-ups

Known trade-offs or explicitly deferred work.
```

## 6. Review workflow

Codex review order:

1. compare the PR with the linked Issue and acceptance criteria;
2. inspect architecture and module boundaries;
3. inspect REST/DTO/validation/error behaviour;
4. inspect JPA entities, persistence context, transactions, queries, and N+1;
5. inspect tests and test type selection;
6. inspect configuration, dependencies, and secrets;
7. report findings before applying any fix.

Review output distinguishes:

- good decisions;
- blocking corrections;
- risks;
- optional improvements;
- whether the Issue can be considered Done.

The owner implements review corrections. After updates, Codex compares the new
state with prior findings and performs another review.

## 7. Definition of Done

See the global DoD in [Backlog](BACKLOG.md). In addition:

- PR description includes verification evidence;
- self-review is complete;
- documentation matches the implemented contract;
- no blocking discussion is unresolved;
- owner approves merge and Issue closure.

## 8. Proposed label taxonomy

| Label | Purpose | Suggested color |
| --- | --- | --- |
| `epic` | Multi-story outcome | `6f42c1` |
| `story` | User/business value | `0e8a16` |
| `task` | Technical/documentation work | `1d76db` |
| `backend` | Backend API/worker code | `0052cc` |
| `frontend` | React/TypeScript | `c5def5` |
| `database` | PostgreSQL, SQL, Flyway, JPA mapping | `5319e7` |
| `infrastructure` | Runtime/platform configuration | `006b75` |
| `docker` | Docker/Compose | `0db7ed` |
| `kubernetes` | Kubernetes | `326ce5` |
| `rabbitmq` | Messaging | `ff6600` |
| `testing` | Test design/coverage | `bfe5bf` |
| `documentation` | Docs and diagrams | `0075ca` |
| `clean-code` | Refactoring/design quality | `fbca04` |
| `portfolio` | Presentation/demo evidence | `d4c5f9` |
| `review-needed` | Ready for reviewer attention | `d93f0b` |
| `blocked` | Named blocker exists | `b60205` |
| `priority-high` | Next critical path | `b60205` |
| `priority-medium` | Important, not immediate | `fbca04` |
| `priority-low` | Later/stretch | `c2e0c6` |

If repository automation cannot create labels, list them in the Issue body until
the owner creates them. Do not silently omit classification.

## 9. Issue and PR updates

- Do not close an Issue without owner approval.
- Do not merge a PR without owner approval.
- Update `docs/PROGRESS.md` after a task starts, becomes blocked, enters review,
  or is completed.
- Link architecture-changing PRs to an ADR.
- Record review results in the PR, not only in a private conversation.

## 10. Handling blockers

A blocker must name:

- what cannot proceed;
- evidence;
- dependency or decision owner;
- smallest action that unblocks work.

Use `blocked` only for a real stopped dependency, not for ordinary uncertainty or
difficulty.

