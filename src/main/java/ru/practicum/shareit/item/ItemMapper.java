package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.request.ItemRequest;

import java.util.HashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    private static final HashMap<Long, ItemRequest> requests = new HashMap<>();

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .owner(new ItemDto.Owner(item.getOwner().getId(), item.getOwner().getName(), item.getOwner().getEmail()))
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getRequestId() : null).build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .owner(User.builder()
                        .id(itemDto.getOwner().getId())
                        .name(itemDto.getOwner().getName())
                        .email(itemDto.getOwner().getEmail()).build())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(itemDto.getRequestId() != null ? requests.get(itemDto.getRequestId()) : null).build();
    }
}
