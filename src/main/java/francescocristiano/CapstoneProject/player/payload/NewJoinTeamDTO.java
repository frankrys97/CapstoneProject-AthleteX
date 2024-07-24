package francescocristiano.CapstoneProject.player.payload;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NewJoinTeamDTO(
        @NotNull
        UUID teamId
) {
}
