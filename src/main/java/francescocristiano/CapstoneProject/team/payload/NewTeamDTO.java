package francescocristiano.CapstoneProject.team.payload;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record NewTeamDTO(
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,
        @PastOrPresent(message = "Creation date cannot be in the future")
        LocalDate creationDate,
/*
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
*/
        String phone,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email address")
        String email,
        @NotBlank(message = "Address cannot be blank")
        String address,
        @NotBlank(message = "Country cannot be blank")
        String country,
        @NotBlank(message = "Primary color cannot be blank")
        @Pattern(regexp = "^#?([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Primary color must be a valid hexadecimal color")
        String primaryColor,
        @NotBlank(message = "Secondary color cannot be blank")
        @Pattern(regexp = "^#?([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Secondary color must be a valid hexadecimal color")
        String secondaryColor,
        MultipartFile avatar) {
}
