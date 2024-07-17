package francescocristiano.CapstoneProject.user.enums;

import francescocristiano.CapstoneProject.exceptions.BadRequestException;

public enum UserType {
    COACH, PLAYER;

    public static UserType getUserType(String type) {
        return switch (type) {
            case "COACH" -> COACH;
            case "PLAYER" -> PLAYER;
            default -> throw new BadRequestException("Invalid user type, must be COACH or PLAYER");
        };
    }
}
