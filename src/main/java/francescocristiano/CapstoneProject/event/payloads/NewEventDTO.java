package francescocristiano.CapstoneProject.event.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalTime;

public record NewEventDTO(
        @NotBlank
        String eventType,
        @NotBlank
        String title,
        @NotBlank
        String description,
        @FutureOrPresent
        LocalDate startDate,
        @FutureOrPresent
        LocalDate endDate,
        @FutureOrPresent
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime startTime,
        @FutureOrPresent
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime endTime,
        @FutureOrPresent
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime meetTime,
        @NotBlank
        String locationType,
        String opponent,
        boolean home,
        String formation,
        int duration,
        String trainingType
) {
}
