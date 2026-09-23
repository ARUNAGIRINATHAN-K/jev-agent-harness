# Technology Stack — Jev Agent Harness

> **Version:** 0.1.0-draft  
> **Last Updated:** 2026-09-23  
> **Status:** Draft  
> **Companion Documents:** [PRD.md](PRD.md) · [SRS.md](SRS.md) · [AGENT.md](AGENT.md) · [ARCHITECTURE.md](ARCHITECTURE.md)

---

## Table of Contents

1. [Executive Summary](#1-executive-summary)
2. [Technology Stack Matrix](#2-technology-stack-matrix)
3. [Core Runtime & Frameworks](#3-core-runtime--frameworks)
4. [AI & Decision Engine Layer](#4-ai--decision-engine-layer)
5. [Persistence & Vector Tier](#5-persistence--vector-tier)
6. [Resilience & Security Stack](#6-resilience--security-stack)
7. [Observability & Telemetry](#7-observability--telemetry)
8. [Build, CI/CD & Testing Infrastructure](#8-build-cicd--testing-infrastructure)
9. [Rationale & Architectural Trade-Offs](#9-rationale--architectural-trade-offs)

---

## 1. Executive Summary

The **Jev Agent Harness** is an enterprise-grade Java Spring Boot framework designed for building autonomous, production-ready AI agents. It integrates **Spring AI 2.0+** for open-ended LLM orchestration with **Jev** (TypeSafe AI's System One model) for sub-500ms deterministic routing, classification, risk-gating, and evaluation.

The technology stack is selected to provide maximum performance, strict type safety, modular pluggability, and seamless operation within modern cloud-native environments.

```
+-----------------------------------------------------------------------------+
|                            Jev Agent Harness                                |
|                                                                             |
|  +---------------------+   +--------------------+   +--------------------+  |
|  |     Java 21 LTS     |   |  Spring Boot 4.x   |   |   Spring AI 2.0+   |  |
|  |  (Virtual Threads)  |   |  (Spring Framework)|   | (Advisor Pipeline) |  |
|  +---------------------+   +--------------------+   +--------------------+  |
|                                                                             |
|  +---------------------+   +--------------------+   +--------------------+  |
|  |     Jev Engine      |   |   Resilience4j     |   |   OpenTelemetry    |  |
|  |  (System One AI)    |   | (Circuit Breaker)  |   | (Micrometer Tracing|  |
|  +---------------------+   +--------------------+   +--------------------+  |
|                                                                             |
|  +---------------------+   +--------------------+   +--------------------+  |
|  |   PostgreSQL / PGV  |   |   Redis (Lettuce)  |   |  Gradle 8.x (KTS)  |  |
|  |   (Persistence/Vec) |   | (Caching/Sessions) |   | (Build System)     |  |
|  +---------------------+   +--------------------+   +--------------------+  |
+-----------------------------------------------------------------------------+
```

---

## 2. Technology Stack Matrix

### 2.1 Core Platform

| Component | Technology | Version | License | Rationale / Usage |
|:---|:---|:---|:---|:---|
| **Language Runtime** | Java OpenJDK | 21 LTS | GPLv2 + CE | Virtual Threads (Project Loom), Records, Pattern Matching, Sealed Types |
| **Application Framework** | Spring Boot | 4.x | Apache 2.0 | Auto-configuration, Dependency Injection, Native Executable capability |
| **Base Framework** | Spring Framework | 7.x | Apache 2.0 | Core IoC, WebFlux, AOP, SpEL |
| **AI Orchestration** | Spring AI | 2.0+ | Apache 2.0 | ChatClient API, Advisor Chain, Tool Calling, VectorStore SPI, MCP |
| **Build Tool** | Gradle | 8.x | Apache 2.0 | Kotlin DSL (`build.gradle.kts`), parallel builds, dependency management |

---

### 2.2 AI & Decision Engine Stack

| Component | Technology | Version / Protocol | License | Role in Harness |
|:---|:---|:---|:---|:---|
| **Decision Engine (System 1)** | Jev API (TypeSafe AI) | v1 (`/v1/systemone`) | Proprietary API | Fast sub-500ms probabilistic classification, intent routing, and risk-gating |
| **Jev HTTP Client** | `java.net.http.HttpClient` | Java 21 Built-in | GPLv2 + CE | High-performance asynchronous HTTP/2 client for Jev API calls |
| **JSON Parser** | Jackson | 3.x | Apache 2.0 | Type-safe JSON serialization/deserialization for Jev decision payload |
| **LLM Provider Integration** | Spring AI ChatModel | 2.0+ | Apache 2.0 | Abstraction layer for OpenAI, Anthropic Claude, Google Vertex AI, Azure OpenAI, Ollama |
| **Protocol Integration** | Model Context Protocol (MCP) | 1.0 Spec | MIT | Standardized tool and resource sharing across agent ecosystems |

---

### 2.3 Persistence & Data Tier

| Layer | Technology | Version | License | Usage |
|:---|:---|:---|:---|:---|
| **Relational Database** | PostgreSQL | 16+ | PostgreSQL License | Primary persistence store for session metadata, audit logs, and agent runs |
| **Data Access** | Spring Data JDBC / JPA | Spring Boot 4.x | Apache 2.0 | Lightweight database access without ORM overhead |
| **Vector Storage** | `pgvector` / Qdrant / Milvus | Latest stable | Open Source | Vector embeddings for long-term agent memory and RAG |
| **In-Memory Store** | VectorStore (Spring AI) | 2.0+ | Apache 2.0 | Fast, ephemeral in-memory vector storage for local testing and lightweight sessions |
| **Cache & State** | Redis (Lettuce Client) | 7.x | BSD-3-Clause | Distributed Jev decision caching, rate limit counters, session state |

---

### 2.4 Resilience, Observability & Security

| Category | Technology | Version | Purpose |
|:---|:---|:---|:---|
| **Circuit Breakers & Retry** | Resilience4j | 2.x | Circuit breaker, retry with exponential backoff, rate limiting for Jev & LLM calls |
| **Distributed Tracing** | OpenTelemetry Java Agent | 1.32+ | W3C TraceContext propagation across agent steps, tool executions, and external API calls |
| **Metrics Collector** | Micrometer | 1.12+ | Execution latency timers, Jev decision outcome counters, token consumption counters |
| **Structured Logging** | SLF4J + Logback | 2.x | JSON-formatted logging with MDC injection (`sessionId`, `runId`, `stepId`, `agentId`) |
| **Authentication & Security** | Spring Security | 7.x | OAuth2 Resource Server, JWT verification, RBAC for REST/SSE/WebSocket endpoints |
| **Secrets Management** | Spring Vault / K8s Secrets | Latest | Dynamic secret resolution and secure API key storage for LLM and Jev credentials |

---

### 2.5 Testing & CI/CD Infrastructure

| Purpose | Tool / Library | Version | Usage |
|:---|:---|:---|:---|
| **Unit Testing** | JUnit 5 Jupiter | 5.10+ | Core agent loop, advisor chain, and tool contract testing |
| **Assertions** | AssertJ | 3.25+ | Fluent assertions for complex agent session states and decision structures |
| **Mocking** | Mockito | 5.x | Unit test mocking for SPI implementations |
| **Integration Testing** | Testcontainers | 1.19+ | Containerized PostgreSQL, Redis, and vector stores during integration runs |
| **API Wire Mocking** | WireMock | 3.x | Deterministic mock server for Jev API and LLM endpoints |
| **Contract Verification** | Spring Cloud Contract | Latest | Ensures schema adherence between Harness API clients and agent hosts |
| **Load Testing** | Gatling | 3.10+ | Benchmarking agent step throughput under high concurrent load |
| **Architectural Rules** | ArchUnit | 1.2+ | Enforces package dependency rules and hexagonal architecture constraints |

---

## 3. Core Runtime & Frameworks

### 3.1 Java 21 LTS

Java 21 provides the foundation for low-latency, high-concurrency agent execution:

- **Virtual Threads (Project Loom):** Enables handling thousands of concurrent agent execution loops without thread pool starvation during blocking I/O calls to LLM and Jev APIs.
- **Record Patterns & Sealed Interfaces:** Used to model immutable domain objects, such as `JevDecision` (`Choice`, `Score`, `Noul`), `AgentState`, and tool execution results with pattern matching for clear, type-safe control flow.
- **Pattern Matching for Switch:** Used extensively in the Agent State Machine (`AgentEngine`) to handle state transitions (`INITIALIZING` -> `REASONING` -> `ACTING` -> `OBSERVING` -> `EVALUATING` -> `COMPLETED`).

### 3.2 Spring Boot 4.x & Spring Framework 7.x

- **Auto-Configuration:** Simplifies agent deployment by auto-configuring `JevClient`, Spring AI `ChatClient`, `ChatMemory`, `GuardrailChain`, and `ToolRegistry` via `@EnableJevAgentHarness`.
- **Spring WebFlux & SSE / WebSockets:** Provides reactive streaming endpoints for real-time token delivery, tool progress feedback, and live agent status streams.
- **GraalVM Native Image Readiness:** Ensures fast startup times (<200ms) and reduced memory footprints for serverless/containerized deployments.

---

## 4. AI & Decision Engine Layer

```
                        +----------------------------+
                        |     Spring AI ChatClient   |
                        +----------------------------+
                                      |
                         [CallResponseAdvisor Chain]
                                      |
           +--------------------------+--------------------------+
           |                                                     |
           v                                                     v
+--------------------+                                 +--------------------+
|  LLM Providers     |                                 | Jev Decision Client|
| (System Two AI)    |                                 | (System One AI)    |
| - OpenAI           |                                 | - Sub-500ms SLA    |
| - Anthropic        |                                 | - Intent Routing   |
| - Vertex AI        |                                 | - Tool Risk Gate   |
| - Ollama           |                                 | - Fallback/Cache   |
+--------------------+                                 +--------------------+
```

### 4.1 Jev Client Architecture

The Jev client operates as a System One decision engine for fast, sub-500ms classification, routing, and risk evaluation:

1. **`TypeSafeJevClient`**: Primary implementation backed by `java.net.http.HttpClient` pointing to `/v1/systemone`.
2. **`CachingJevClient`**: Decorator using Redis/In-Memory caches to return instantaneous responses for duplicate input signatures.
3. **`FallbackJevClient`**: Resilience decorator triggering fallback heuristic rules or lightweight local classifiers if the Jev service is unreachable.

### 4.2 Spring AI 2.0+ Advisor Pipeline

Spring AI's `CallResponseAdvisor` contract powers cross-cutting agent capabilities:

- **`SystemPromptAdvisor`**: Dynamically injects session context, memory snapshots, and active tool schemas.
- **`GuardrailAdvisor`**: Executes pre-input, pre-LLM, post-LLM, and post-action security and safety checks.
- **`MemoryAdvisor`**: Manages windowed, summarized, or persistent conversation history.
- **`ToolAugmentationAdvisor`**: Injects Jev-selected tools into the LLM request payload.

---

## 5. Persistence & Vector Tier

```
                   +-----------------------------------+
                   |     ChatMemoryManager (SPI)       |
                   +-----------------------------------+
                                     |
       +-----------------------------+-----------------------------+
       |                             |                             |
       v                             v                             v
+--------------+              +--------------+              +--------------+
|  PostgreSQL  |              |    Redis     |              | Vector Store |
|  (JDBC Repo) |              |  (Lettuce)   |              | (PGVector /  |
| - Sessions   |              | - Decision   |              |  Qdrant)     |
| - Audit Logs |              |   Cache      |              | - RAG Memory |
+--------------+              +--------------+              +--------------+
```

1. **Relational Storage:** PostgreSQL handles persistent agent runs, event streams, tool execution history, and audit trails.
2. **Vector Storage:** Pluggable `VectorStore` implementations (`pgvector`, Qdrant, Milvus) provide semantic memory retrieval across agent sessions.
3. **Distributed Caching:** Redis caches Jev classification results, session tokens, and rate-limiting bucket state.

---

## 6. Resilience & Security Stack

### 6.1 Resilience Strategy

- **Resilience4j Circuit Breakers:** Protects external Jev and LLM API endpoints against cascading failures.
- **Exponential Backoff Retry:** Automatically retries transient 5xx HTTP errors or rate limits (429) from AI providers.
- **Bulkhead Isolation:** Restricts maximum concurrent tool executions to prevent resource exhaustion.

### 6.2 Security Framework

- **Spring Security 7.x:** Protects REST and WebSocket agent endpoints.
- **Tool Risk Gating:** Jev evaluates tool arguments for risk levels before execution (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`), requiring human-in-the-loop approval for `HIGH` / `CRITICAL` tools.
- **PII & Injection Guardrails:** Redacts sensitive data (regex + Jev classification) and blocks prompt injection payloads before LLM invocation.

---

## 7. Observability & Telemetry

```
+------------------+     OTLP     +-----------------------+     Metrics     +-------------------+
| Jev Agent Engine | -----------> | OpenTelemetry Collector| -------------> | Prometheus /      |
| Trace & Metrics  |              +-----------------------+                 | Grafana Dashboards|
+------------------+                                                        +-------------------+
         |
         | JSON Logs (MDC: sessionId, runId, stepId)
         v
+------------------+
| SLF4J / Logback  |
+------------------+
```

- **Metrics Tracked:**
  - `agent.run.duration`: Overall latency per agent run.
  - `agent.step.duration`: Latency broken down by RAOE step.
  - `jev.decision.latency`: Latency of System One Jev calls.
  - `jev.cache.hit_ratio`: Ratio of cached vs. remote Jev calls.
  - `llm.tokens.prompt` / `llm.tokens.completion`: Token utilization counters.
  - `tool.execution.count` / `tool.execution.error`: Tool reliability metrics.

- **Distributed Tracing:** Tracing spans cover the complete agent flow (`AgentSession` -> `JevDecision` -> `GuardrailChain` -> `ChatClient` -> `ToolExecutor`).

---

## 8. Build, CI/CD & Testing Infrastructure

### 8.1 Build System Configuration (`build.gradle.kts`)

```kotlin
plugins {
    java
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
}

group = "com.jevharness"
version = "0.1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    // Core Spring Boot & Spring AI
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.ai:spring-ai-starter-model-provider:2.0.0")
    
    // Integration & Resilience
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:3.0.0")
    
    // Observability
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:3.0.1")
}
```

---

## 9. Rationale & Architectural Trade-Offs

| Decision | Selected Option | Alternative Considered | Rationale for Choice |
|:---|:---|:---|:---|
| **JVM Platform** | Java 21 LTS | Java 17 LTS / Python | Virtual Threads allow high-concurrency I/O loops without complex reactive programming models, while maintaining seamless enterprise Java ecosystem integration. |
| **AI Framework** | Spring AI 2.0+ | LangChain4j / Custom HTTP | Spring AI provides native Spring Boot integration, auto-configurations, advisor chains, and first-class tool calling primitives. |
| **System One AI** | Jev Engine | Custom Fine-Tuned Model | Jev provides sub-500ms guaranteed type-safe decisions, significantly reducing agent execution latency and LLM token costs. |
| **Build System** | Gradle (Kotlin DSL) | Maven | Superior build caching, incremental compilation, readable `build.gradle.kts` configuration syntax, and flexible multi-module management. |
| **Data Tier** | Spring Data JDBC + PG | Hibernate / JPA | Avoids lazy-loading pitfalls and heavy ORM caching layers in high-throughput stateless agent execution threads. |
| **Observability** | OpenTelemetry | Custom Logging | Industry-standard open specification for tracing across distributed AI microservices, compatible with Grafana Tempo, Jaeger, and Datadog. |

---

> **Summary:** The Jev Agent Harness technology stack combines Java 21, Spring Boot 4.x, Spring AI 2.0+, and the Jev decision engine into a robust, high-performance foundation for enterprise AI agents.
