package com.jevharness.core.exception;

/**
 * Exception thrown when prompt validation, PII redaction, prompt injection check, or tool risk gating fails.
 */
public class GuardrailViolationException extends AgentException {

    private final String stage;
    private final String violationDetails;

    public GuardrailViolationException(String stage, String violationDetails) {
        super(String.format("Guardrail violation at stage [%s]: %s", stage, violationDetails));
        this.stage = stage;
        this.violationDetails = violationDetails;
    }

    public String getStage() {
        return stage;
    }

    public String getViolationDetails() {
        return violationDetails;
    }
}
