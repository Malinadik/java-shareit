package ru.practicum.shareit.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@RequiredArgsConstructor
public class ItemRequest {

    private Long id;

    private String description;

    private User requestor;

    private final Timestamp created = Timestamp.valueOf(LocalDateTime.now());
}
