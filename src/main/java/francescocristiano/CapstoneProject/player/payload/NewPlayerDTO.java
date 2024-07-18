package francescocristiano.CapstoneProject.player.payload;


import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record NewPlayerDTO(
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,
        @NotBlank(message = "Surname cannot be blank")
        @Size(min = 3, max = 50, message = "Surname must be between 3 and 50 characters")
        String surname,
        @NotNull(message = "Birth date cannot be null")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,
        @NotBlank(message = "Position cannot be blank")
        String position,
        Double weight,
        Double height,
        @Max(value = 99, message = "Jersey number must be between 1 and 99")
        @Min(value = 1, message = "Jersey number must be between 1 and 99")
        Integer jerseyNumber
) {
}
