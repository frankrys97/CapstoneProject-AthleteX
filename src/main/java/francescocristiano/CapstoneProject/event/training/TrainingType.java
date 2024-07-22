package francescocristiano.CapstoneProject.event.training;

import francescocristiano.CapstoneProject.exceptions.BadRequestException;

public enum TrainingType {
    STRENGTH,
    TACTICS,
    TECHNICAL,
    PRE_MATCH;

    public static TrainingType getTrainingType(String trainingType) {
        return switch (trainingType.toUpperCase()) {
            case "STRENGTH" -> STRENGTH;
            case "TACTICS" -> TACTICS;
            case "TECHNICAL" -> TECHNICAL;
            case "PRE_MATCH" -> PRE_MATCH;
            default ->
                    throw new BadRequestException("Invalid training type, must be STRENGTH, TACTICS, TECHNICAL or PRE_MATCH");
        };
    }
}
