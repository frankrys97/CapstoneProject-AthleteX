package francescocristiano.CapstoneProject.player.avalibilities.payload;

import java.time.LocalDate;

public record NewPlayerInjuryDTO(
        LocalDate startDate,
        LocalDate endDate
) {
}
