package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class BookingRequestDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingDto.Item item;

    private User booker;

    private Status status;

    @Data
    @AllArgsConstructor
    public static class Item {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private User owner;
        private ItemRequest request;
    }
}
