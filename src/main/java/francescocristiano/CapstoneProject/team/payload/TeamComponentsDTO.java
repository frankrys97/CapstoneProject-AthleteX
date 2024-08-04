package francescocristiano.CapstoneProject.team.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.player.playerClass.Player;

import java.util.List;

public record TeamComponentsDTO(Coach coach,
                                @JsonIgnoreProperties({"team", "statistics", "injuryPeriods"}) List<Player> players) {
}
