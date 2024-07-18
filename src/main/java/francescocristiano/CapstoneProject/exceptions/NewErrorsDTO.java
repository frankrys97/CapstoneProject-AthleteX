package francescocristiano.CapstoneProject.exceptions;

import java.time.LocalDateTime;


public record NewErrorsDTO(String message, LocalDateTime dateTime) {
}
