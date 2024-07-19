package francescocristiano.CapstoneProject.user.userPayloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAdminDTO(
        @NotBlank
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,

        @NotBlank
        @Size(min = 3, max = 50, message = "Surname must be between 3 and 50 characters")
        String surname,

        @NotBlank
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank
        @Email
        String email,
        @NotBlank
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password
) {
}
