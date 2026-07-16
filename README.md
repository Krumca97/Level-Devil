# Level Devil Project

**Author:** Aleš Laník (LAN0229)
**Course:** Java 2 — Semester Project
**Institution:** VŠB – Technical University of Ostrava, FEI

---

## Overview

This project extends *Level Devil*, a platformer known for its deliberately unfair, trap-based level design, into a full client-server application. The original single-process JavaFX game has been restructured into a three-tier architecture consisting of a Spring Boot REST backend, a JavaFX client, and a persistent SQLite database accessed through JPA/Hibernate.

The objective of the project was not merely to implement a playable game, but to demonstrate a complete client-server architecture with proper separation of concerns: a stateless REST API, a relational persistence layer, and a JavaFX frontend that communicates with the backend exclusively over HTTP.

---

## Architecture

```
┌─────────────────┐        HTTP/JSON         ┌──────────────────┐        JPA/Hibernate      ┌──────────────┐
│  JavaFX Client  │ ───────────────────────▶ │  Spring Boot API │ ─────────────────────────▶│    SQLite    │
│  (HttpClient +  │ ◀─────────────────────── │  (REST + Swagger)│ ◀─────────────────────────│              │
│      Gson)      │                          └──────────────────┘                           └──────────────┘
└─────────────────┘
```

The frontend and backend run as independent processes. The JavaFX client accesses all application state — players, game records, level progress, and settings — through a centralized `ApiClient` component; no state is shared directly between the two layers.

### Data Model

| Entity | Relationship | Description |
|---|---|---|
| `Player` | 1:N `GameRecord`, 1:1 `PlayerSettings` | Core player identity |
| `GameRecord` | N:1 `Player` | Individual play attempts and scores |
| `LevelsProgress` | N:1 `Player` | Levels completed by the player |
| `PlayerSettings` | 1:1 `Player` | User preferences (audio, controls, etc.) |

---

## Technical Challenges and Solutions

Several non-trivial issues were encountered and resolved during development:

- **Java Module System vs. Spring Boot CGLIB proxies.** Spring generates proxy classes at runtime, which conflicts with the constraints of the Java Platform Module System. Resolved using `--add-reads` and explicit `opens` directives in `module-info.java`.
- **SQLite dialect support in Hibernate.** SQLite is not natively supported by Hibernate and requires the `hibernate-community-dialects` module along with explicit configuration of `SQLiteDialect`.
- **Cyclic JSON serialization.** Bidirectional entity relationships (e.g., `Player ↔ GameRecord`) caused infinite recursion during serialization; resolved using `@JsonIgnore` on back-references.
- **State management across JavaFX scenes.** JavaFX instantiates a new controller for each loaded scene, which prevents state from persisting naturally across views. Shared state (e.g., the currently logged-in player, completed levels) is therefore maintained in static fields (`Game.completedLevels`, `MenuController.currentPlayer`). This is a deliberate design trade-off rather than an oversight, given the constraints of the JavaFX controller lifecycle, and is discussed further below.
- **HTTP status code inconsistencies.** Certain POST endpoints returned `200 OK` instead of the expected `201 Created`; the `ApiClient` was updated to treat both as valid success responses.
- **Inconsistent JSON structure for `PlayerSettings`.** The endpoint occasionally returned a JSON array instead of a single object; resolved using Gson's `TypeToken` for flexible list-based parsing.

---

## Technology Stack

**Backend**
- Spring Boot
- Spring Data JPA / Hibernate
- SQLite
- springdoc-openapi (Swagger UI)

**Frontend**
- JavaFX (with the Java Platform Module System)
- `java.net.http.HttpClient`
- Gson

---

## Running the Application

Both the backend and frontend are started concurrently via a shell script:

```bash
./run.sh
```

The script also clears port 8080 if it is already in use by a previous instance:

```bash
kill $(lsof -t -i:8080)
```

Once running, API documentation is available through Swagger UI at:
`http://localhost:8080/swagger-ui.html` (path may vary depending on springdoc configuration).

---
