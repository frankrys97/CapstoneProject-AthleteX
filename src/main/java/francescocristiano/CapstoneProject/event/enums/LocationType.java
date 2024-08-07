package francescocristiano.CapstoneProject.event.enums;

import francescocristiano.CapstoneProject.exceptions.BadRequestException;

public enum LocationType {
    GYM, STADIUM, TRAINING_FIELD;

    public static LocationType getLocationType(String locationType) {
        return switch (locationType.toUpperCase()) {
            case "GYM" -> GYM;
            case "STADIUM" -> STADIUM;
            case "TRAINING_FIELD" -> TRAINING_FIELD;
            default -> throw new BadRequestException("Invalid location type, use GYM, STADIUM or TRAINING_FIELD");
        };
    }
}
