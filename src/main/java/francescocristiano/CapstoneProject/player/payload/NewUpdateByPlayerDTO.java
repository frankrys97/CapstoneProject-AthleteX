package francescocristiano.CapstoneProject.player.payload;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record NewUpdateByPlayerDTO(
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,
        @Size(min = 3, max = 50, message = "Surname must be between 3 and 50 characters")
        String surname,
        String username,
        LocalDate birthDate,
        @Email
        String email,
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
        String password,
        @Max(value = 99, message = "Jersey number must be between 1 and 99")
        @Min(value = 1, message = "Jersey number must be between 1 and 99")
        Integer jerseyNumber,
        Double height,
        Double weight
) {
}
