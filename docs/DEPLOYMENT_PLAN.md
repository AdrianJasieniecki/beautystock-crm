# Deployment Plan

## 1. Scope and sequencing

Deployment work follows this order:

1. run processes locally;
2. run dependencies and then the full system with Docker Compose;
3. build stable multi-stage images;
4. deploy to a local/test Kubernetes cluster;
5. plan Azure cost, security, and topology;
6. provision a controlled portfolio environment.

No infrastructure implementation is included in the documentation-foundation
phase.

## 2. Docker images

Planned images:

- `beautystock/backend-api`;
- `beautystock/backend-worker`;
- `beautystock/frontend`.

Requirements:

- multi-stage build;
- reproducible dependency resolution;
- minimal runtime base;
- non-root process where practical;
- no secrets in Dockerfile, build arguments, image labels, or layers;
- explicit JVM/container memory considerations;
- graceful termination;
- useful image version derived from immutable Git state.

## 3. Docker Compose

Planned services:

```text
frontend (optional during early backend work)
    → backend-api
        → postgres
        → rabbitmq
    rabbitmq
        → backend-worker
backend-worker
    → postgres when notification/idempotency persistence requires it
```

Compose must include:

- named local volumes for PostgreSQL and RabbitMQ where useful;
- health checks;
- service dependencies based on health, with application-level retry/fail-fast
  still implemented appropriately;
- safe local-only example credentials;
- explicit ports;
- isolated network;
- documented startup, logs, stop, and narrow reset procedures.

Compose is for local development and demonstrations, not the production security
model.

## 4. Kubernetes resources

### backend-api

- `Deployment`;
- internal `Service`;
- `Ingress` route where public access is needed;
- liveness and readiness probes;
- resource requests and limits;
- environment from ConfigMap/Secret;
- rolling-update and graceful-shutdown settings.

### backend-worker

- `Deployment`;
- no public service unless a management endpoint is required;
- liveness and readiness semantics appropriate for a consumer;
- resource requests and limits;
- queue and database configuration;
- graceful consumer shutdown.

### frontend

- `Deployment` and `Service`, or a later static-hosting decision;
- Ingress/TLS;
- build-time versus runtime API URL strategy.

### shared platform resources

- namespace;
- ConfigMaps;
- Secret templates or external-secret integration;
- Ingress;
- optional NetworkPolicies;
- observability integration;
- PodDisruptionBudget only if replica count/availability justifies it.

## 5. Health checks

### Liveness

Answers: “Should the platform restart this process?”

- must not fail solely because a temporary external dependency is unavailable;
- should detect unrecoverable application state;
- uses a dedicated Actuator liveness group.

### Readiness

Answers: “Can this instance receive/perform work now?”

- API readiness includes required database connectivity/migration state;
- worker readiness reflects broker connectivity and ability to consume;
- failures remove the instance from traffic/work without creating restart loops.

Probe timings must reflect measured startup behaviour rather than copied defaults.

## 6. Configuration and secrets

### ConfigMap candidates

- profile name;
- log level;
- non-secret hostnames/ports;
- queue/exchange names;
- feature flags;
- public frontend configuration.

### Secret candidates

- database password;
- RabbitMQ credentials;
- email/provider credentials;
- private keys/tokens.

Rules:

- never commit real secret values;
- Kubernetes Secret base64 is encoding, not encryption;
- prefer managed identity and a secret store in Azure where feasible;
- restrict access using RBAC;
- plan rotation;
- avoid logging secret-backed values.

## 7. Database migrations

Possible strategies to evaluate:

1. API runs Flyway on startup with one-replica/locking assumptions.
2. Dedicated Kubernetes Job runs migrations before rollout.
3. CI/CD migration step runs with controlled credentials.

The selected strategy must:

- run exactly once logically;
- stop incompatible application rollout on migration failure;
- support forward-compatible rolling deployment where possible;
- expose status and logs;
- define backup/restore and rollback expectations.

## 8. Azure target architecture

Planned portfolio target:

```text
Internet
  → Azure/Kubernetes ingress with TLS
    → frontend
    → backend-api
AKS workloads
  → Azure Database for PostgreSQL
  → RabbitMQ hosting option
Images
  ← Azure Container Registry
Secrets/config
  ← managed identity + Key Vault or controlled Kubernetes integration
Logs/metrics
  → Azure Monitor / Log Analytics or selected stack
```

### Azure Container Registry

- immutable image tags plus human-readable release tags;
- authentication via managed identity where possible;
- image scanning policy;
- retention/cleanup;
- separate build and deployment permissions.

### Azure Kubernetes Service

- choose a low-cost learning topology;
- separate system and application concerns only when justified;
- configure managed identity;
- define ingress/TLS;
- define autoscaling only after resource baselines;
- document cluster creation and teardown.

Before provisioning AKS, reconsider whether Azure Container Apps would provide
better cost/operational fit. If AKS is kept, the educational reason must be
explicit.

### Azure Database for PostgreSQL

- supported PostgreSQL version;
- private or tightly controlled network access;
- TLS required;
- separate application and migration credentials if practical;
- backups and retention;
- storage/compute sizing and cost alerts;
- no public default credentials in repository files.

### RabbitMQ hosting

Options to evaluate:

- RabbitMQ deployed in Kubernetes for learning;
- managed third-party RabbitMQ;
- a documented Azure messaging alternative comparison without replacing the
  RabbitMQ learning goal.

Running RabbitMQ in AKS adds stateful operations; the plan must not call it
production-ready without backup, persistence, upgrades, and monitoring.

## 9. Observability

Minimum production-like signals:

- API request rate, latency, and errors;
- JVM heap, GC, threads, and process metrics;
- database connection-pool metrics;
- RabbitMQ connection, publish, consume, retry, and DLQ metrics;
- order placement and inventory conflict counters with bounded labels;
- structured logs with correlation/event IDs;
- health and deployment events.

Avoid unbounded labels such as salon ID, order ID, or raw endpoint path in metrics.

## 10. JVM runtime and diagnostics

Plan:

- set memory requests/limits before tuning heap;
- verify JVM container awareness;
- select safe out-of-memory behaviour;
- write GC logs to controlled storage/stream;
- define a secured, capacity-checked heap-dump path;
- document `jcmd`/runtime-specific thread and heap diagnostics;
- never upload dumps containing application data to public artifacts.

## 11. Release and rollback

Planned release flow:

1. build and test immutable source revision;
2. scan and push images to ACR;
3. run/verify database migration;
4. deploy versioned workloads;
5. wait for readiness and smoke tests;
6. observe errors and key metrics;
7. promote/declare release;
8. roll back application image when compatible;
9. use forward-fix/data recovery for non-reversible schema changes.

## 12. Cost and teardown guardrails

Before creating Azure resources:

- estimate monthly and idle cost;
- set a budget and alerts;
- choose region intentionally;
- document which resources incur cost while idle;
- tag resources with project/owner/environment;
- create a verified teardown checklist;
- protect persistent data before deletion;
- never destroy resources automatically without owner approval.

## 13. Deployment acceptance criteria

Cloud deployment is complete only when:

- the main business flow works over TLS;
- secrets are external to Git;
- database migrations are controlled;
- liveness/readiness behave correctly;
- logs and key metrics are accessible;
- image versions are traceable to commits;
- rollback and teardown are documented and tested proportionately;
- cost limitations and security limitations are explicit.

