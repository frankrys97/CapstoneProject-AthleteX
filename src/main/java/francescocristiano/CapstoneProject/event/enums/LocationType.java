package francescocristiano.CapstoneProject.event.enums;

import francescocristiano.CapstoneProject.exceptions.BadRequestException;

public enum LocationType {
    GYM, STADIUM, TRAINING_FIELD;

    public static LocationType getLocationType(String locationType) {
        switch (locationType.toUpperCase()) {
            case "GYM":
                return GYM;
            case "STADIUM":
                return STADIUM;
            case "TRAINING_FIELD":
                return TRAINING_FIELD;
            default:
                throw new BadRequestException("Invalid location type, use GYM, STADIUM or TRAINING_FIELD");
        }
    }
}
