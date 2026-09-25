package com.jevharness.core.exception;

/**
 * Exception thrown when tool discovery, argument validation, or method execution fails.
 */
public class ToolExecutionException extends AgentException {

    private final String toolName;

    public ToolExecutionException(String toolName, String message) {
        super(String.format("Error executing tool [%s]: %s", toolName, message));
        this.toolName = toolName;
    }

    public ToolExecutionException(String toolName, String message, Throwable cause) {
        super(String.format("Error executing tool [%s]: %s", toolName, message), cause);
        this.toolName = toolName;
    }

    public String getToolName() {
        return toolName;
    }
}
