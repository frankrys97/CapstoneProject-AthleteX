package francescocristiano.CapstoneProject.user.userPayloads;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @NotBlank
        String email,
        @NotBlank
        String password) {
}
