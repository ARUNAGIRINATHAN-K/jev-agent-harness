package com.jevharness.core.domain;

import java.util.Objects;

/**
 * Immutable configuration options governing an agent execution session.
 *
 * @param maxIterations    Maximum permitted RAOE iterations before loop termination (default: 10).
 * @param timeoutSeconds   Maximum total execution time in seconds (default: 300).
 * @param modelName        LLM model identifier (default: "gpt-4o").
 * @param temperature      LLM temperature sampling parameter (default: 0.7).
 * @param systemPrompt     Custom system instructions or role directives.
 * @param enableGuardrails Flag enabling 5-stage guardrail validation (default: true).
 * @param enableCaching    Flag enabling Jev decision caching (default: true).
 */
public record AgentExecutionConfig(
        int maxIterations,
        long timeoutSeconds,
        String modelName,
        double temperature,
        String systemPrompt,
        boolean enableGuardrails,
        boolean enableCaching
) {

    public static final int DEFAULT_MAX_ITERATIONS = 10;
    public static final long DEFAULT_TIMEOUT_SECONDS = 300L;
    public static final String DEFAULT_MODEL_NAME = "gpt-4o";
    public static final double DEFAULT_TEMPERATURE = 0.7;

    public AgentExecutionConfig {
        if (maxIterations <= 0) {
            throw new IllegalArgumentException("maxIterations must be greater than 0");
        }
        if (timeoutSeconds <= 0) {
            throw new IllegalArgumentException("timeoutSeconds must be greater than 0");
        }
        Objects.requireNonNull(modelName, "modelName must not be null");
        if (temperature < 0.0 || temperature > 2.0) {
            throw new IllegalArgumentException("temperature must be between 0.0 and 2.0");
        }
    }

    /**
     * Creates a default configuration with baseline settings.
     *
     * @return AgentExecutionConfig instance with default values.
     */
    public static AgentExecutionConfig defaultConfig() {
        return new AgentExecutionConfig(
                DEFAULT_MAX_ITERATIONS,
                DEFAULT_TIMEOUT_SECONDS,
                DEFAULT_MODEL_NAME,
                DEFAULT_TEMPERATURE,
                null,
                true,
                true
        );
    }

    /**
     * Builder for constructing custom AgentExecutionConfig instances.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int maxIterations = DEFAULT_MAX_ITERATIONS;
        private long timeoutSeconds = DEFAULT_TIMEOUT_SECONDS;
        private String modelName = DEFAULT_MODEL_NAME;
        private double temperature = DEFAULT_TEMPERATURE;
        private String systemPrompt;
        private boolean enableGuardrails = true;
        private boolean enableCaching = true;

        public Builder maxIterations(int maxIterations) {
            this.maxIterations = maxIterations;
            return this;
        }

        public Builder timeoutSeconds(long timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder systemPrompt(String systemPrompt) {
            this.systemPrompt = systemPrompt;
            return this;
        }

        public Builder enableGuardrails(boolean enableGuardrails) {
            this.enableGuardrails = enableGuardrails;
            return this;
        }

        public Builder enableCaching(boolean enableCaching) {
            this.enableCaching = enableCaching;
            return this;
        }

        public AgentExecutionConfig build() {
            return new AgentExecutionConfig(
                    maxIterations,
                    timeoutSeconds,
                    modelName,
                    temperature,
                    systemPrompt,
                    enableGuardrails,
                    enableCaching
            );
        }
    }
}
