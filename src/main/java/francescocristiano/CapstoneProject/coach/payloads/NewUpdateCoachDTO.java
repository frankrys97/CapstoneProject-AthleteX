package francescocristiano.CapstoneProject.coach.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewUpdateCoachDTO(
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,
        @Size(min = 3, max = 50, message = "Surname must be between 3 and 50 characters")
        String surname,

        String username,
        @Email
        String email,
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
        String password
) {
}
