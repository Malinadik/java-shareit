package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequestDto toRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor() != null ?
                        new ItemRequestDto.User(itemRequest.getRequestor().getId(), itemRequest.getRequestor().getName(), itemRequest.getRequestor().getEmail()) : null)
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequest toRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated() != null ? itemRequestDto.getCreated()
                        : Timestamp.valueOf(LocalDateTime.now()))
                .requestor(itemRequestDto.getRequestor() != null ? User.builder()
                        .id(itemRequestDto.getRequestor().getId())
                        .name(itemRequestDto.getRequestor().getName())
                        .email(itemRequestDto.getRequestor().getEmail()).build() : null)
                .build();
    }

    public static ItemRequestWithItems toRequestWithItems(ItemRequest itemRequest) {
        return ItemRequestWithItems.builder()
                .id(itemRequest.getId())
                .created(itemRequest.getCreated())
                .description(itemRequest.getDescription())
                .build();
    }

}
