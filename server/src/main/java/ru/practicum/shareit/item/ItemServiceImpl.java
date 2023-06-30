package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestsRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItem;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;
import static ru.practicum.shareit.item.comment.CommentMapper.toCommentDto;
import static ru.practicum.shareit.user.UserMapper.toUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final ItemRequestsRepository requestsRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    private final UserService userService;

    private final UserRepository userRepository;


    public List<ItemDto> getItems(Long id, int from, int size) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User Not found");
        }
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        List<ItemDto> dtoList = itemRepository.findAllByOwnerIdOrderById(id, pageable).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        dtoList.forEach(itemDto -> itemDto.setComments(getComments(itemDto.getId())));
        dtoList.forEach(itemDto -> setBookings(itemDto, id));

        return dtoList;
    }

    public ItemDto getItemById(Long userId, Long itemId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User Not found");
        }
        ItemDto item = toItemDto(itemRepository
                .findById(itemId).orElseThrow(() -> new NotFoundException("Item not found")));
        item = setBookings(item, userId);
        item.setComments(getComments(itemId));
        return item;
    }

    public List<ItemDto> searchItems(String text, int from, int size) {
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        return itemRepository.findByNameOrDescriptionAvailable(text, pageable).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Transactional
    public ItemDto updateItem(Long id, ItemDto itemDto, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("item not found"));
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
        return toItemDto(itemRepository.save(item));
    }

    @Transactional
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        /*if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User Not found");
        }*/
        User user = toUser(userService.getUserById(userId));
        itemDto.setOwner(new ItemDto.Owner(user.getId(), user.getName(), user.getEmail()));
        Item item = toItem(itemDto);
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = requestsRepository.findById(itemDto.getRequestId()).orElseThrow(() -> new NotFoundException("Request not found!"));
            item.setRequest(itemRequest);
        }
        return toItemDto(itemRepository.save(item));
    }

    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        Comment comment = Comment.builder().text(commentDto.getText()).build();
        comment.setAuthor(toUser(userService.getUserById(userId)));
        comment.setItem((itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("No such item"))));
        if (!bookingRepository.existsByBookerIdAndEndBeforeAndStatus(userId, LocalDateTime.now(), Status.APPROVED)) {
            throw new NotAvailableException("You cant comment before use!");
        }

        comment.setCreated(LocalDateTime.now());
        return toCommentDto(commentRepository.save(comment));
    }

    private ItemDto setBookings(ItemDto itemDto, Long userId) {
        if (itemDto.getOwner().getId().longValue() == userId.longValue()) {
            itemDto.setLastBooking(bookingRepository.findByItemIdAndStatus(itemDto.getId(), Sort.by(Sort.Direction.DESC, "start"), Status.APPROVED).stream().filter(booking -> booking.getStart().isBefore(LocalDateTime.now())).map(BookingMapper::toItemBookingDto).max(Comparator.comparing(BookingItemDto::getEnd)).orElse(null));
            itemDto.setNextBooking(bookingRepository.findByItemIdAndStatus(itemDto.getId(), Sort.by(Sort.Direction.ASC, "start"), Status.APPROVED).stream().filter(booking -> !booking.getStatus().equals(Status.REJECTED)).map(BookingMapper::toItemBookingDto).filter(booking -> booking.getStart().isAfter(LocalDateTime.now())).findFirst().orElse(null));
            return itemDto;
        }
        return itemDto;
    }

    private List<CommentDto> getComments(Long itemId) {
        return commentRepository.findByItemId(itemId).stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

}
