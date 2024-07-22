package francescocristiano.CapstoneProject.event.enums;

import francescocristiano.CapstoneProject.exceptions.BadRequestException;

public enum EventType {
    MATCH,
    TRAINING;

    public static EventType getEventType(String type) {
        return switch (type.toUpperCase()) {
            case "MATCH" -> MATCH;
            case "TRAINING" -> TRAINING;
            default -> throw new BadRequestException("Invalid event type, must be MATCH or TRAINING");
        };
    }
}
