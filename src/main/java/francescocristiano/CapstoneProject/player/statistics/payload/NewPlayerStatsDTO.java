package francescocristiano.CapstoneProject.player.statistics.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record NewPlayerStatsDTO(
        @NotNull
        Boolean attendance,
        Integer duration,
        @NotNull
        Integer goals,
        @NotNull
        Integer assists,
        @NotNull
        Integer yellowCards,
        @NotNull
        Integer redCards,
        @Min(value = 0, message = "Coach rating must be between 0 and 100")
        @Max(value = 100, message = "Coach rating must be between 0 and 100")
        Integer coachRating,
        String coachComment) {
}
