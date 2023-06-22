package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)))
@ValidateOnExecution
public class BookingEntryDto {
    private Long id;

    @FutureOrPresent
    @NotNull
    private LocalDateTime start;

    @FutureOrPresent
    @NotNull
    private LocalDateTime end;

    @NotNull
    private Long itemId;
}
