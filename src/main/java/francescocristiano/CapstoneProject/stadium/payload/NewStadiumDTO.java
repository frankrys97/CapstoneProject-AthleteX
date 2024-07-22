package francescocristiano.CapstoneProject.stadium.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewStadiumDTO(
        @NotBlank
        String name,
        @NotBlank
        String city,
        @NotBlank
        String address,
        @NotNull
        Long capacity
) {
}
