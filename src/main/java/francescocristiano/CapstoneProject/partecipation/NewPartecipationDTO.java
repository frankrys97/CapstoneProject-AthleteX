package francescocristiano.CapstoneProject.partecipation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NewPartecipationDTO(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String name,

        @NotBlank
        String surname
) {
}
