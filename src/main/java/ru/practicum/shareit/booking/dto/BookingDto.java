package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private User booker;

    private Status status;

    @Data
    @Builder
    @AllArgsConstructor
    public static class Item {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private User owner;
        private ItemRequest request;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class User {
        private Long id;
        private String name;
        private String email;
    }
}
