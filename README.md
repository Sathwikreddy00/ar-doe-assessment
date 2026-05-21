# AlphaRecon DevOps Engineer Assessment

## Purpose

This repository contains the submission for the AlphaRecon DevOps Engineer skills assessment. The base application is a minimal Spring Boot REST API for user management. This submission wraps it with the infrastructure needed for consistent, repeatable execution across environments.

The system is structured around three clearly separated concerns — **build**, **configuration**, and **runtime** — each handled by dedicated tooling with no overlap. The goal is a system that can be cloned and running locally with a single command, with no manual steps or machine-specific setup.

---

## System Architecture

```
┌─────────────────────────────────────────────────────┐
│                    Your Machine                     │
│                                                     │
│   docker compose up --build                         │
│          ↓                                          │
│   ┌──────────────────────────────────────────────┐  │
│   │            Docker Container                  │  │
│   │                                              │  │
│   │   Spring Boot App (port 9090)                │  │
│   │   ├── REST API  (/api/v1/users)              │  │
│   │   ├── Health    (/actuator/health)           │  │
│   │   ├── DB UI     (/h2-console)                │  │
│   │   └── H2 In-Memory Database (embedded)       │  │
│   └──────────────────────────────────────────────┘  │
│          ↑                                          │
│   localhost:9090                                    │
└─────────────────────────────────────────────────────┘
```

The H2 database is embedded inside the application — it is not a separate service or container. It initializes automatically on startup, loads seed data from `data.sql`, and resets when the container stops. This removes the need for a separate database container and simplifies the system significantly for this scope.

---

## Separation of Concerns

The solution is explicitly structured around three independent layers:

### Build — `Dockerfile` + `.github/workflows/ci.yml`
Responsible for compiling source code into a runnable artifact. Nothing in the build layer touches runtime behavior or environment-specific configuration.

The Dockerfile uses a **multi-stage build**:
- **Stage 1 (builder):** Uses `gradle:jdk25-alpine` to compile the source and produce an executable JAR via `./gradlew bootJar`. This stage contains the full build toolchain.
- **Stage 2 (runtime):** Uses `eclipse-temurin:25-jre-alpine` — a slim JRE-only image. Only the compiled JAR is copied across from Stage 1 via `COPY --from=builder`. All build tools, source code, and downloaded dependencies are discarded. The final image is significantly smaller and has a reduced attack surface.

The GitHub Actions pipeline automates this build process on every push to `main`, ensuring the application is always in a buildable and testable state.

### Configuration — `application.yaml` + `.env` + `.env.example`
Responsible for all environment-specific values. No configuration is hardcoded in the application or build files.

`application.yaml` uses Spring Boot's `${VAR:default}` syntax throughout — if an environment variable is set it is used, otherwise the default applies. This means the same Docker image runs identically in any environment by changing only the `.env` file — no code changes, no image rebuilds.

| Variable | Controls | Default |
|---|---|---|
| `SERVER_PORT` | Port the app listens on | `9090` |
| `SPRING_DATASOURCE_URL` | H2 connection string | `jdbc:h2:mem:usersdb` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `sa` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | _(empty)_ |
| `LOGGING_LEVEL_ROOT` | Root log level | `INFO` |
| `LOGGING_LEVEL_APP` | Application log level | `DEBUG` |

The `.env` file is git-ignored and never committed. `.env.example` is committed as a template — it contains all required variable names with safe defaults so anyone cloning the repo knows exactly what to configure.

### Runtime — `docker-compose.yml`
Responsible for how the system starts, stops, and operates. It does not know how the app was built and does not contain any configuration values directly — it reads everything from `.env` and passes it into the container.

Key runtime behaviors defined here:
- Port mapping from host to container
- Environment variable injection from `.env`
- Health check polling `/actuator/health` every 30 seconds
- Automatic restart on unexpected container exit (`restart: unless-stopped`)

---

## Application Endpoints

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/v1/users` | List all users |
| `GET` | `/api/v1/users/{id}` | Get a single user |
| `POST` | `/api/v1/users` | Create a user |
| `PUT` | `/api/v1/users/{id}` | Update a user |
| `DELETE` | `/api/v1/users/{id}` | Delete a user |
| `GET` | `/actuator/health` | Application health status |
| `GET` | `/h2-console` | H2 database browser UI |

---

## CI Pipeline

Defined in `.github/workflows/ci.yml`. Triggers on every push or pull request to `main`.

```
Push to main
     ↓
Job 1: Build and Test
  ├── Checkout code
  ├── Set up JDK 25 (Temurin)
  ├── Restore Gradle dependency cache
  ├── Run ./gradlew build (compile + test)
  └── Upload test reports as artifacts
     ↓ (only if Job 1 passes)
Job 2: Docker Build
  ├── Set up Docker Buildx
  ├── Build Docker image (push: false)
  └── Tag image as ar-assessment:<commit-sha>
```

Job 2 only runs if Job 1 passes — a broken build never produces an image. The image is tagged with the commit SHA making every build fully traceable back to its source commit. No registry push is configured as that would require external credentials beyond this scope.

---

## Project Structure

```
ar-doe-assessment/
├── .github/
│   └── workflows/
│       └── ci.yml                  # CI pipeline — build concern
├── src/
│   └── main/
│       ├── java/                   # Application source code
│       └── resources/
│           ├── application.yaml    # Externalized config — configuration concern
│           ├── data.sql            # Seed data (5 users loaded on startup)
│           └── banner.txt          # Startup banner
├── .env.example                    # Config template — configuration concern
├── .gitignore                      # Excludes .env, build artifacts, IDE files
├── .gitattributes                  # Enforces Unix line endings on gradlew
├── docker-compose.yml              # Runtime orchestration — runtime concern
├── Dockerfile                      # Container build definition — build concern
├── build.gradle                    # Gradle build definition — build concern
└── README.md
```

---

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (includes Docker Compose)
- Git

No local Java or Gradle installation required. The entire build runs inside Docker.

---

## Running Locally

**1. Clone the repository**
```bash
git clone https://github.com/<your-username>/ar-doe-assessment.git
cd ar-doe-assessment
```

**2. Create your environment file**
```bash
cp .env.example .env
```
The defaults work out of the box for local runs. No changes required.

**3. Start the application**
```bash
docker compose up --build
```

Wait until you see:
```
Started AssessmentApplication in X.XXX seconds
Tomcat started on port 9090
```

**4. Verify the application is healthy**
```bash
curl http://localhost:9090/actuator/health
```
Expected:
```json
{"status":"UP","groups":["liveness","readiness"]}
```

**5. Test the API**
```bash
# List all users (5 seeded on startup)
curl http://localhost:9090/api/v1/users

# Get a single user
curl http://localhost:9090/api/v1/users/1

# Create a user
curl -X POST http://localhost:9090/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Doe","email":"jane@example.com","note":"Test user"}'

# Update a user
curl -X PUT http://localhost:9090/api/v1/users/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Alice","lastName":"Updated","email":"alice.updated@example.com","note":"Updated"}'

# Delete a user
curl -X DELETE http://localhost:9090/api/v1/users/1
```

**6. Stop the application**
```bash
docker compose down
```

---

## Tool Decisions and Reasoning

**Docker + multi-stage build** — Containerization guarantees the app runs identically regardless of the host machine. Multi-stage builds separate the build environment from the runtime environment — the final image contains only what is needed to run the app, nothing more.

**gradle:jdk25-alpine + eclipse-temurin:25-jre-alpine** — Alpine-based images are significantly smaller than their Ubuntu counterparts. JRE (not JDK) is used at runtime because no compilation happens after the build stage. Both images match the Java 25 requirement of the application.

**Docker Compose** — The simplest, most universally understood tool for orchestrating a single-service system. It handles environment injection, port mapping, health checks, and restart policy in one readable file. Kubernetes or Swarm would be excessive for a single container at this scope.

**GitHub Actions** — Native to GitHub, zero external tooling or credentials required for CI. The pipeline lives alongside the code it builds. Gradle and Docker layer caching are both configured to keep subsequent runs fast.

**Environment variables via `.env`** — The twelve-factor app methodology recommends strict separation of config from code. Using `.env` with Docker Compose is the standard pattern for local development. The same variables would be injected via a secrets manager (AWS Secrets Manager, Vault) in a production environment.

**H2 in-memory database (unchanged)** — The embedded database removes the need for a separate container, simplifying the system significantly. The tradeoff — data does not persist across restarts — is acceptable for this scope and clearly documented.

---

## Tradeoffs and Limitations

**No image registry push** — The CI pipeline builds and validates the Docker image but does not push it to a registry. This would require external credentials and a registry setup outside the scope of this assessment.

**H2 is in-memory** — Data resets on every container restart. In a production system this would be replaced with a persistent database (PostgreSQL, MySQL) running as a separate service with a named Docker volume for persistence.

**No HTTPS** — The app runs over plain HTTP. Production would require TLS termination, typically handled by a reverse proxy (nginx, Traefik) in front of the container.

**Single environment** — Only one environment (local) is configured. A real system would have separate configurations per environment, managed via a secrets manager rather than `.env` files.

**open-in-view warning** — Spring logs a warning about `spring.jpa.open-in-view` being enabled by default. This is a known Spring Boot behavior that can cause lazy-loading database queries during view rendering. It has no impact at this scope but would be explicitly disabled (`spring.jpa.open-in-view: false`) in production.

---

## How It Could Be Extended

- Add PostgreSQL to `docker-compose.yml` with a named volume for data persistence, replacing H2
- Push the Docker image to a registry (Docker Hub, ECR) on merge to `main` using stored GitHub secrets
- Add environment-specific Compose override files (`docker-compose.staging.yml`, `docker-compose.prod.yml`)
- Add a reverse proxy (nginx or Traefik) for TLS termination and load balancing
- Introduce Kubernetes manifests (Deployment, Service, ConfigMap, Secret) for production-grade orchestration
- Replace `.env` file secrets with a proper secrets manager (AWS Secrets Manager, HashiCorp Vault)
