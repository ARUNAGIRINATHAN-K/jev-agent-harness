package com.jevharness.core.domain;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Sealed interface representing domain messages exchanged during an agent execution session.
 */
public sealed interface AgentMessage permits 
        AgentMessage.UserMessage, 
        AgentMessage.AssistantMessage, 
        AgentMessage.SystemMessage, 
        AgentMessage.ToolResponseMessage {

    String content();
    MessageType type();
    Instant timestamp();
    Map<String, Object> metadata();

    enum MessageType {
        USER,
        ASSISTANT,
        SYSTEM,
        TOOL_RESPONSE
    }

    record UserMessage(String content, Instant timestamp, Map<String, Object> metadata) implements AgentMessage {
        public UserMessage {
            Objects.requireNonNull(content, "content must not be null");
            timestamp = timestamp != null ? timestamp : Instant.now();
            metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
        }

        public UserMessage(String content) {
            this(content, Instant.now(), Map.of());
        }

        @Override
        public MessageType type() {
            return MessageType.USER;
        }
    }

    record AssistantMessage(String content, String toolCallName, Map<String, Object> toolCallArgs, Instant timestamp, Map<String, Object> metadata) implements AgentMessage {
        public AssistantMessage {
            Objects.requireNonNull(content, "content must not be null");
            timestamp = timestamp != null ? timestamp : Instant.now();
            metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
            toolCallArgs = toolCallArgs != null ? Map.copyOf(toolCallArgs) : Map.of();
        }

        public AssistantMessage(String content) {
            this(content, null, Map.of(), Instant.now(), Map.of());
        }

        @Override
        public MessageType type() {
            return MessageType.ASSISTANT;
        }
    }

    record SystemMessage(String content, Instant timestamp, Map<String, Object> metadata) implements AgentMessage {
        public SystemMessage {
            Objects.requireNonNull(content, "content must not be null");
            timestamp = timestamp != null ? timestamp : Instant.now();
            metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
        }

        public SystemMessage(String content) {
            this(content, Instant.now(), Map.of());
        }

        @Override
        public MessageType type() {
            return MessageType.SYSTEM;
        }
    }

    record ToolResponseMessage(String toolName, String content, boolean success, Instant timestamp, Map<String, Object> metadata) implements AgentMessage {
        public ToolResponseMessage {
            Objects.requireNonNull(toolName, "toolName must not be null");
            Objects.requireNonNull(content, "content must not be null");
            timestamp = timestamp != null ? timestamp : Instant.now();
            metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
        }

        public ToolResponseMessage(String toolName, String content, boolean success) {
            this(toolName, content, success, Instant.now(), Map.of());
        }

        @Override
        public MessageType type() {
            return MessageType.TOOL_RESPONSE;
        }
    }
}
