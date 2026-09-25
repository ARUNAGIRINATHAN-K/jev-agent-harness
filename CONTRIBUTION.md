# Contributing to Jev Agent Harness

> **Version:** 0.1.0-draft  
> **Last Updated:** 2026-09-24  
> **License:** Apache License 2.0  
> **Companion Documents:** [PRD.md](PRD.md) · [SRS.md](SRS.md) · [TECH_STACK.md](TECH_STACK.md) · [AGENT.md](AGENT.md) · [ARCHITECTURE.md](ARCHITECTURE.md) · [IMPLEMENTATION_PLAN.md](IMPLEMENTATION_PLAN.md)

---

Thank you for your interest in contributing to the **Jev Agent Harness**! We welcome contributions from developers of all skill levels. Whether you are fixing a bug, adding a new feature, improving documentation, or creating tutorials, your help makes this project better for everyone.

---

## Table of Contents

1. [Code of Conduct](#1-code-of-conduct)
2. [Getting Started](#2-getting-started)
   - [Prerequisites](#prerequisites)
   - [Setting Up Your Local Environment](#setting-up-your-local-environment)
3. [Development Workflow](#3-development-workflow)
   - [Branching Strategy](#branching-strategy)
   - [Commit Message Guidelines](#commit-message-guidelines)
4. [Coding & Architecture Standards](#4-coding--architecture-standards)
   - [Java 21 & Spring Boot Standards](#java-21--spring-boot-standards)
   - [Package Organization](#package-organization)
   - [Exception Handling](#exception-handling)
5. [Testing & Quality Gates](#5-testing--quality-gates)
   - [Unit & Integration Testing](#unit--integration-testing)
   - [Test Coverage Thresholds](#test-coverage-thresholds)
6. [Submitting a Pull Request (PR)](#6-submitting-a-pull-request-pr)
7. [Reporting Bugs & Security Issues](#7-reporting-bugs--security-issues)

---

## 1. Code of Conduct

By participating in this project, you agree to abide by our Code of Conduct:

- **Be Respectful:** Treat all community members with courtesy, empathy, and respect.
- **Collaborate Openly:** Welcome feedback, share knowledge, and foster an inclusive environment.
- **Maintain Quality:** Strive for clean, testable, and well-documented code that aligns with project specifications.

---

## 2. Getting Started

### Prerequisites

To build and run the Jev Agent Harness locally, ensure you have the following installed:

- **Java JDK 21 LTS** or newer (Eclipse Temurin, GraalVM, or Corretto recommended)
- **Maven 3.9+** (or use the included `./mvnw` wrapper)
- **Docker Desktop / Podman** (required for running integration tests via Testcontainers for PostgreSQL & Redis)
- **Git**

### Setting Up Your Local Environment

1. **Fork the Repository:**
   Fork the repository on GitHub to your personal account.

2. **Clone Your Fork:**
   ```bash
   git clone https://github.com/YOUR-USERNAME/jev-agent-harness.git
   cd jev-agent-harness
   ```

3. **Verify the Build:**
   Run the Maven build to verify that all dependencies download and existing tests pass:
   ```bash
   ./mvnw clean test
   ```

---

## 3. Development Workflow

### Branching Strategy

We follow a topic-branch workflow based on `main`:

- Create feature or fix branches off `main`.
- Name branches descriptively using the following prefixes:
  - `feature/` — New agent capabilities, tool support, or framework enhancements (e.g., `feature/summary-chat-memory`)
  - `bugfix/` — Bug fixes and patch resolutions (e.g., `bugfix/jev-timeout-retry`)
  - `docs/` — Documentation updates or additions (e.g., `docs/add-architecture-diagrams`)
  - `refactor/` — Code cleanup without behavior changes (e.g., `refactor/advisor-pipeline`)
  - `test/` — Adding or improving test suites (e.g., `test/guardrail-chain-tests`)

### Commit Message Guidelines

We enforce the **Conventional Commits** specification for clean, readable git history and automated release notes generation:

```
<type>(<scope>): <short summary>

[optional body]

[optional footer(s)]
```

#### Commit Types:
- `feat`: A new feature added to the framework
- `fix`: A bug fix
- `docs`: Documentation-only changes
- `style`: Formatting, missing semi-colons, etc. (no production code change)
- `refactor`: A code change that neither fixes a bug nor adds a feature
- `test`: Adding missing tests or correcting existing tests
- `chore`: Build process, dependency updates, or auxiliary tool changes

#### Examples:
```bash
feat(jev): implement CachingJevClient decorator with Redis backpressure
fix(guardrail): prevent NullPointerException in PiiRedactor on null input
docs(readme): update quickstart section with Java 21 build flags
```

---

## 4. Coding & Architecture Standards

### Java 21 & Spring Boot Standards

- **Use Modern Java Features:**
  - Prefer immutable **Records** (`record`) for DTOs, domain models, and events (`AgentSession`, `StepRecord`, `AgentExecutionConfig`).
  - Use **Sealed Interfaces** (`sealed interface`) for domain hierarchies (`AgentMessage`, `AgentEvent`).
  - Leverage **Pattern Matching for Switch** in state machine handlers.
  - Rely on **Virtual Threads** (Project Loom) for concurrent I/O operations.
- **Spring AI 2.0 Idioms:**
  - Build custom LLM extensions using Spring AI's `CallResponseAdvisor` contract.
  - Define agent tools using standard Spring `@Tool` annotations.
- **Immutability:** Keep domain objects immutable. State mutations should return new record instances (e.g., `AgentSession.transitionTo(...)`).

### Package Organization

Strictly adhere to the hexagonal / modular package structure outlined in [ARCHITECTURE.md](ARCHITECTURE.md):

```
com.jevharness/
├── core/
│   ├── domain/        # Domain models (AgentSession, AgentState, StepRecord)
│   ├── event/         # Reactive event bus & sealed AgentEvent definitions
│   └── exception/     # Core unchecked exception hierarchy
├── jev/               # Jev System One client, decorators & caching
├── llm/               # Spring AI ChatClient advisors & routers
├── tool/              # @Tool scanner, registry & argument augmenters
├── memory/            # Chat memory implementations (Window, Summary, Persistent)
├── guardrail/         # 5-stage validation pipeline
└── api/               # REST controllers, SSE streaming & WebSockets
```

### Exception Handling

- All custom exceptions must extend `com.jevharness.core.exception.AgentException`.
- Never swallow exceptions or return null fallbacks silently.
- Always include relevant context attributes (e.g., `sessionId`, `toolName`, `attemptedState`).

---

## 5. Testing & Quality Gates

### Unit & Integration Testing

- Write unit tests for all public classes using **JUnit 5**, **AssertJ**, and **Mockito**.
- Use **Reactor `StepVerifier`** for testing reactive event bus streams (`InMemoryAgentEventBus`).
- Use **Testcontainers** for integration tests requiring real PostgreSQL or Redis instances.
- Use **WireMock** for stubbing Jev API (`/v1/systemone`) and external LLM REST calls.

### Test Coverage Thresholds

- **Unit Test Coverage:** Minimum **85%** instruction coverage required for all new code.
- **Zero Warnings:** All code must compile cleanly with 0 compiler warnings.

---

## 6. Submitting a Pull Request (PR)

1. **Rebase on `main`:**
   Before opening a PR, rebase your branch on the latest `main`:
   ```bash
   git checkout main
   git pull origin main
   git checkout feature/your-feature-name
   git rebase main
   ```

2. **Run Full Verification:**
   Ensure all tests pass and code compiles cleanly:
   ```bash
   ./mvnw clean test
   ```

3. **Open Pull Request:**
   Push your branch to GitHub and create a PR against the `main` branch.

4. **PR Checklist:**
   Your PR title and description should include:
   - A clear summary of the changes and motivation.
   - Reference to associated GitHub Issues (e.g., `Fixes #42`).
   - Confirmation that `./mvnw clean test` passes locally.
   - Updated documentation if new public APIs or configuration keys were added.

---

## 7. Reporting Bugs & Security Issues

### Reporting Bugs

If you discover a bug, please open a GitHub Issue with:
- A clear, descriptive title.
- Steps to reproduce the issue.
- Expected vs. actual behavior.
- Environment details (JDK version, OS, Spring Boot version).

### Security Vulnerabilities

If you identify a security vulnerability (such as API key exposure or prompt injection bypass), **do not open a public issue**. Please report security issues confidentially to `security@jevharness.org` or submit a private security advisory via GitHub.

---

> **Summary:** We appreciate your contributions to making **Jev Agent Harness** the leading Java framework for intelligent, sub-500ms AI agents!
