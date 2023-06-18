package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItems(Long id);

    ItemDto getItemById(Long itemId);

    List<ItemDto> searchItems(String text);

    ItemDto updateItem(Long id, ItemDto itemDto, Long itemId);

    ItemDto addItem(Long id, ItemDto itemDto);

}