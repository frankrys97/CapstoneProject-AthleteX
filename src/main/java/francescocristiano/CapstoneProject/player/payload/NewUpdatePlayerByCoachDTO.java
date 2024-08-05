package francescocristiano.CapstoneProject.player.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record NewUpdatePlayerByCoachDTO(@Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
                                        String name,
                                        @Size(min = 3, max = 50, message = "Surname must be between 3 and 50 characters")
                                        String surname,
                                        LocalDate birthDate,
                                        @Email
                                        String email,
                                        String position,
                                        Integer jerseyNumber,
                                        Double height,
                                        Double weight) {
}
