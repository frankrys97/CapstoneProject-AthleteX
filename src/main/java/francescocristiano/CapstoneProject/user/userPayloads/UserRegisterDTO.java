package francescocristiano.CapstoneProject.user.userPayloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterDTO(
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,
        @NotBlank(message = "Surname cannot be blank")
        @Size(min = 3, max = 50, message = "Surname must be between 3 and 50 characters")
        String surname,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email")
        String email,
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
        String username,
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
        String password,
        @NotBlank(message = "User type cannot be blank")
        String userType
) {
}
