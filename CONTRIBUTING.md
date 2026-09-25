# Contributing to Jev Agent Harness

**Version:** 0.1.0-draft | **License:** Apache 2.0 | **Last Updated:** 2026-09-24

Thank you for contributing! We welcome bug fixes, features, documentation, and tests from developers of all levels.

---

## Quick Start

### Prerequisites
- Java JDK 21 LTS+
- Maven 3.9+
- Docker (for Testcontainers integration tests)
- Git

### Setup
```bash
git clone https://github.com/ARUNAGIRINATHAN-K/jev-agent-harness.git
cd jev-agent-harness
./mvnw clean test
```

---

## Code of Conduct

Be respectful, collaborative, and maintain high code quality. Treat all community members with courtesy and empathy.

---

## Development Workflow

### Branch Naming
- `feature/` — New capabilities (e.g., `feature/summary-chat-memory`)
- `bugfix/` — Bug fixes (e.g., `bugfix/jev-timeout-retry`)
- `docs/` — Documentation updates
- `refactor/` — Code cleanup
- `test/` — Test additions/improvements

### Commit Messages (Conventional Commits)
```
<type>(<scope>): <summary>

[optional body]
```

**Types:** `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

**Example:**
```
feat(jev): implement CachingJevClient decorator with Redis backpressure
fix(guardrail): prevent NullPointerException in PiiRedactor on null input
```

---

## Code Standards

### Java 21 & Spring Boot
- Use **Records** for DTOs and domain models
- Use **Sealed Interfaces** for type hierarchies
- Prefer **Pattern Matching** in state machine handlers
- Leverage **Virtual Threads** for concurrent I/O
- Use Spring AI 2.0 `CallResponseAdvisor` and `@Tool` annotations

### Package Structure
```
com.jevharness/
├── core/          # Domain models, events, exceptions
├── jev/           # Jev System One client & caching
├── llm/           # LLM advisors & routers
├── tool/          # Tool registry & augmenters
├── memory/        # Chat memory implementations
├── guardrail/     # Validation pipeline
└── api/           # REST controllers & streaming
```

### Exception Handling
- Extend `com.jevharness.core.exception.AgentException`
- Never swallow exceptions or return null silently
- Include context (sessionId, toolName, etc.)

---

## Testing & Quality

- **Unit Tests:** JUnit 5, AssertJ, Mockito
- **Reactive:** Reactor `StepVerifier`
- **Integration:** Testcontainers (PostgreSQL, Redis)
- **API Mocking:** WireMock
- **Coverage:** Minimum **85%** instruction coverage
- **Warnings:** Zero compiler warnings required

---

## Submitting a Pull Request

1. **Rebase on main:**
   ```bash
   git checkout main && git pull origin main
   git checkout your-branch && git rebase main
   ```

2. **Verify locally:**
   ```bash
   ./mvnw clean test
   ```

3. **Create PR** with:
   - Clear summary and motivation
   - Reference to GitHub issues (e.g., `Fixes #42`)
   - Confirmation tests pass
   - Updated docs (if needed)

---

## Reporting Issues

### Bugs
Open a GitHub Issue with:
- Clear title and description
- Steps to reproduce
- Expected vs. actual behavior
- Environment (JDK, OS, Spring Boot version)

### Security Vulnerabilities
**Do not open public issues.** Report privately to `security@jevharness.org` or use GitHub's private security advisory feature.

---

See [ARCHITECTURE.md](docs/ARCHITECTURE.md), [TECH_STACK.md](docs/TECH_STACK.md), and [IMPLEMENTATION_PLAN.md](docs/IMPLEMENTATION_PLAN.md) for detailed guidance.