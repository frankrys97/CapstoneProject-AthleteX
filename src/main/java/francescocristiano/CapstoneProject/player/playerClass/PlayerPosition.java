package francescocristiano.CapstoneProject.player.playerClass;

import francescocristiano.CapstoneProject.exceptions.BadRequestException;

public enum PlayerPosition {
    GOALKEEPER,
    DEFENDER,
    MIDFIELDER,
    STRIKER;

    public static PlayerPosition getPlayerPosition(String position) {
        return switch (position.toUpperCase()) {
            case "GOALKEEPER" -> GOALKEEPER;
            case "DEFENDER" -> DEFENDER;
            case "MIDFIELDER" -> MIDFIELDER;
            case "STRIKER" -> STRIKER;
            default ->
                    throw new BadRequestException("Invalid player position, must be GOALKEEPER, DEFENDER, MIDFIELDER or STRIKER");
        };
    }
}
