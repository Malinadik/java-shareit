package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItem;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.UserMapper.toUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final HashMap<Long, Item> itemsMap = new HashMap<>();

    static HashMap<Long, ItemRequest> requests = new HashMap<>();

    @Autowired
    private final UserService userService;

    private Long id = 1L;

    public List<ItemDto> getItems(Long id) {
        return itemsMap.values().stream()
                .filter(item -> item.getOwner().getId().equals(id))
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public ItemDto getItemById(Long itemId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item not found!");
        }
        return toItemDto(itemsMap.get(itemId));
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()){
            return new ArrayList<>();
        }
        return itemsMap.values()
                .stream()
                .filter(i -> (i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase())) && i.getAvailable())
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto updateItem(Long id, ItemDto itemDto, Long itemId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item not found!");
        }
        Item item = itemsMap.get(itemId);
        if (item.getOwner().getId() != id.longValue()) {
            throw new NotFoundException("Another owner!");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemsMap.put(itemId, item);
        return toItemDto(item);
    }

    public ItemDto addItem(Long userId, ItemDto itemDto) {
        userService.getUserById(userId);
        itemDto.setId(id++);
        User user = toUser(userService.getUserById(userId));
        itemDto.setOwner(new ItemDto.Owner(user.getId(), user.getName(), user.getEmail()));
        itemsMap.put(itemDto.getId(), toItem(itemDto));
        return getItemById(itemDto.getId());
    }


}
