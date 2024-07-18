package francescocristiano.CapstoneProject.player.payload;

import francescocristiano.CapstoneProject.player.playerClass.PlayerPosition;
import francescocristiano.CapstoneProject.player.playerClass.PlayerStatus;
import francescocristiano.CapstoneProject.user.enums.UserType;

import java.time.LocalDate;
import java.util.UUID;

public record NewPlayerAddManuallyResponseDTO(
        UUID id,
        String name,
        String surname,
        LocalDate birthDate,
        UserType userType,
        PlayerPosition position,
        Integer jerseyNumber,
        Double weight,
        Double height,
        PlayerStatus status,
        String teamName,
        String coachTeamName
) {
}
