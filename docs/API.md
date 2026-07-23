# REST API Plan

## 1. Status

This is a design plan, not an implemented API specification. Endpoint details may
change during Issue refinement. Every implemented endpoint must update this file
or a future generated OpenAPI source of truth.

## 2. General conventions

- Base path: `/api/v1`.
- Media type: `application/json`.
- Resource names: plural nouns, lower-case paths.
- JSON fields: `camelCase`.
- Identifiers: opaque numeric or UUID values selected per module; clients must not
  infer business meaning.
- Timestamps: ISO 8601, UTC for instants.
- DTOs: explicit request and response types; never JPA entities.
- Unknown sensitive/internal exception details are not returned.
- Potentially unbounded collections are paginated.

## 3. Planned resources

### 3.1 Salons

| Method | Path | Purpose | Typical success |
| --- | --- | --- | --- |
| POST | `/api/v1/salons` | Create salon profile | `201 Created` |
| GET | `/api/v1/salons` | List/filter salons | `200 OK` |
| GET | `/api/v1/salons/{salonId}` | View salon details | `200 OK` |
| PATCH | `/api/v1/salons/{salonId}/contact` | Update contact data | `200 OK` |
| PATCH | `/api/v1/salons/{salonId}/status` | Change lifecycle status | `200 OK` |
| POST | `/api/v1/salons/{salonId}/notes` | Add sales note | `201 Created` |
| GET | `/api/v1/salons/{salonId}/notes` | List notes | `200 OK` |
| GET | `/api/v1/salons/{salonId}/orders` | List salon orders | `200 OK` |

Possible salon filters:

- `status`;
- `query` for agreed name/contact search semantics;
- `page`, `size`, `sort`.

### 3.2 Products

| Method | Path | Purpose | Typical success |
| --- | --- | --- | --- |
| POST | `/api/v1/products` | Create product | `201 Created` |
| GET | `/api/v1/products` | List/filter products | `200 OK` |
| GET | `/api/v1/products/{productId}` | View product | `200 OK` |
| PATCH | `/api/v1/products/{productId}/price` | Change current price | `200 OK` |
| POST | `/api/v1/products/{productId}/deactivation` | Deactivate product | `200 OK` |

Possible product filters:

- `sku`;
- `brand`;
- `category`;
- `active`;
- `query`;
- pagination and sort.

### 3.3 Inventory

| Method | Path | Purpose | Typical success |
| --- | --- | --- | --- |
| GET | `/api/v1/inventory` | View current inventory | `200 OK` |
| GET | `/api/v1/inventory/{productId}` | View one product balance | `200 OK` |
| POST | `/api/v1/inventory/{productId}/adjustments` | Apply manual adjustment | `201 Created` |
| GET | `/api/v1/inventory/{productId}/movements` | List movement history | `200 OK` |

Reservation endpoints may remain internal application interfaces used by Orders.
An external endpoint is added only if a real admin use case exists.

### 3.4 Orders

| Method | Path | Purpose | Typical success |
| --- | --- | --- | --- |
| POST | `/api/v1/orders` | Create draft order | `201 Created` |
| GET | `/api/v1/orders` | List/filter orders | `200 OK` |
| GET | `/api/v1/orders/{orderId}` | View order details | `200 OK` |
| POST | `/api/v1/orders/{orderId}/items` | Add draft item | `201 Created` |
| PATCH | `/api/v1/orders/{orderId}/items/{itemId}` | Change draft quantity | `200 OK` |
| DELETE | `/api/v1/orders/{orderId}/items/{itemId}` | Remove draft item | `204 No Content` |
| POST | `/api/v1/orders/{orderId}/placement` | Place order | `200 OK` |
| POST | `/api/v1/orders/{orderId}/cancellation` | Cancel order | `200 OK` |

Action subresources such as `placement` and `cancellation` make business commands
explicit and idempotency semantics reviewable. A generic status patch should not
allow clients to bypass transition rules.

### 3.5 Recommendations

| Method | Path | Purpose | Typical success |
| --- | --- | --- | --- |
| GET | `/api/v1/recommendations/demand` | Demand recommendations | `200 OK` |
| GET | `/api/v1/recommendations/low-stock` | Low-stock recommendations | `200 OK` |

### 3.6 Notifications

| Method | Path | Purpose | Typical success |
| --- | --- | --- | --- |
| GET | `/api/v1/notifications` | List notifications | `200 OK` |
| GET | `/api/v1/notifications/{notificationId}` | View notification | `200 OK` |

## 4. HTTP status rules

| Status | Use |
| --- | --- |
| `200 OK` | Successful read or command returning a representation |
| `201 Created` | New resource created; include `Location` where practical |
| `204 No Content` | Successful operation with no response body |
| `400 Bad Request` | Malformed JSON, type mismatch, or Bean Validation failure |
| `404 Not Found` | Requested resource does not exist or is not visible |
| `409 Conflict` | Uniqueness, invalid state transition, stock, or concurrency conflict |
| `415 Unsupported Media Type` | Unsupported request content type |
| `422 Unprocessable Content` | Reserved; use only after an explicit contract decision |
| `500 Internal Server Error` | Unexpected failure with safe public message |
| `503 Service Unavailable` | Service is temporarily unable to process due to dependency/readiness |

Do not choose status codes per controller ad hoc. The global error mapping owns
the convention.

## 5. Error response

Proposed contract:

```json
{
  "timestamp": "2026-07-23T12:34:56.789Z",
  "status": 400,
  "code": "VALIDATION_FAILED",
  "message": "Request validation failed.",
  "path": "/api/v1/salons",
  "correlationId": "7bc9e4e1-8d86-4f76-91cc-0a8ef53a22a6",
  "violations": [
    {
      "field": "email",
      "code": "Email",
      "message": "must be a well-formed email address"
    }
  ]
}
```

Rules:

- `code` is stable enough for client decisions;
- `message` is safe and readable, not a stack trace;
- `violations` is present only for field/object validation errors;
- rejected values are omitted by default because they may contain secrets or
  personal data;
- log details use the correlation ID;
- constraint violations are translated carefully without exposing SQL.

## 6. DTO rules

1. Create separate request and response DTOs.
2. Prefer immutable Java records where they fit framework and validation needs.
3. Do not put JPA annotations on API DTOs.
4. Do not serialize entities.
5. Map only fields intended by the endpoint; a request must not overwrite
   ownership, version, audit, or status fields accidentally.
6. Use a deliberate mapper. Manual mapping is acceptable; MapStruct is a deferred
   decision.
7. Represent money with decimal value and explicit currency policy, never
   `double`/`float`.
8. Use enums in transport only after defining unknown-value compatibility.
9. Avoid deeply nested graphs. Link or summarize related resources.
10. Do not accept server-generated fields in create requests.

## 7. Validation rules

Validation has multiple layers:

- **transport validation:** shape, required values, length, numeric ranges, email
  syntax;
- **business validation:** status transitions, active product, order ownership,
  sufficient available stock;
- **database integrity:** unique, not-null, check, foreign-key constraints.

Bean Validation at the controller boundary does not replace business or database
rules. Database exceptions should map to stable business/API conflicts only when
the violated constraint is recognized.

Example create-salon request:

```json
{
  "name": "Studio Urody Aurora",
  "email": "orders@aurora.example",
  "phone": "+48 600 000 000",
  "status": "LEAD",
  "address": {
    "street": "Example 10",
    "postalCode": "00-001",
    "city": "Warsaw",
    "countryCode": "PL"
  }
}
```

Example response:

```json
{
  "id": 42,
  "name": "Studio Urody Aurora",
  "email": "orders@aurora.example",
  "phone": "+48 600 000 000",
  "status": "LEAD",
  "address": {
    "street": "Example 10",
    "postalCode": "00-001",
    "city": "Warsaw",
    "countryCode": "PL"
  },
  "createdAt": "2026-07-23T12:34:56.789Z",
  "updatedAt": "2026-07-23T12:34:56.789Z"
}
```

Exact required/optional fields require business confirmation during the Salon
Issue refinement.

## 8. Pagination

Default query model:

- `page`: zero-based, default `0`;
- `size`: default `20`, maximum `100`;
- `sort`: allow-listed field and direction, for example `createdAt,desc`.

Proposed page response:

```json
{
  "content": [],
  "page": {
    "number": 0,
    "size": 20,
    "totalElements": 0,
    "totalPages": 0
  }
}
```

Rules:

- never expose Spring `Page` serialization as an accidental contract;
- allow-list sort fields to avoid leaking persistence details;
- define stable secondary ordering for equal values;
- cap page size;
- use cursor pagination later only when scale/query behaviour justifies it.

## 9. Filtering and search

- Query parameters use explicit documented semantics.
- Empty filter values are either ignored or rejected consistently.
- Text matching case/normalization rules must be specified.
- Combinations are tested.
- Indexes follow actual query plans and selectivity.
- Generic “search every column” behaviour is out of scope for the first version.

## 10. Concurrency and idempotency

- Inventory write conflicts should return a stable `409` error.
- Commands that may be retried by clients or messages need an explicit
  idempotency decision.
- Optimistic version values are not exposed unless the API uses them deliberately
  (for example via ETag or a version field).
- Duplicate order placement/cancellation must not repeat side effects.

## 11. Security baseline

- Validate input size and shape.
- Never return secrets, stack traces, SQL, or internal class names.
- Treat salon contact details as personal/business-sensitive data in logs.
- CORS is configured explicitly when frontend origin is known.
- Authentication/authorization is a deferred scope decision, but endpoints must
  not be described as production-secure until it exists.
- Management endpoints are not exposed publicly without controls.

## 12. Testing expectations

For each resource, choose the smallest useful set:

- pure unit tests for business rules and mappings;
- Bean Validation tests where constraints are non-trivial;
- `@WebMvcTest` for HTTP status, JSON, validation, and exception mapping;
- `@DataJpaTest` or focused integration tests with PostgreSQL Testcontainers for
  mappings, constraints, queries, and locking;
- selected end-to-end `@SpringBootTest` flows only where multiple layers and
  transaction behaviour matter.

Tests should assert observable behaviour, not merely that mocks were invoked.

