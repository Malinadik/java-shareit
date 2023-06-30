package ru.practicum.shareit.item.comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)))
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    private String authorName;

    private Long itemId;

    private LocalDateTime created;
}
