package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)))
public class UserDto {
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;
}
