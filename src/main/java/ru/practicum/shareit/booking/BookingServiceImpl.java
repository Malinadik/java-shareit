package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingEntryDto;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotSupportedStateException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingMapper.toBooking;
import static ru.practicum.shareit.booking.BookingMapper.toBookingDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingDto addBooking(Long id, BookingEntryDto bookingDto) {
        validateDate(bookingDto);
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new NotFoundException("item not found"));
        if (id == item.getOwner().getId().longValue()) {
            throw new NotFoundException("YOu cant book your own item");
        }
        if (!item.getAvailable()) {
            throw new NotAvailableException("Not available");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));
        Booking booking = toBooking(bookingDto, item, user);
        return toBookingDto(bookingRepository.save(booking));
    }

    private void validateDate(BookingEntryDto bookingDto) {
        if (bookingDto.getStart().isEqual(bookingDto.getEnd()) || bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new NotAvailableException("Incorrect date!");
        }
    }

    @Transactional
    @Override
    public BookingDto approveBooking(Long id, Long bookingId, Boolean approved) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("No such booking"));
        if (booking.getItem().getOwner().getId().longValue() != id.longValue()) {
            throw new NotFoundException("Ur not the owner!");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new NotAvailableException("Already approved!");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
            Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow();
            itemRepository.save(item);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto getBookingById(Long id, Long bookingId) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("No such booking"));
        if (booking.getItem().getOwner().getId() != id.longValue() && booking.getBooker().getId() != id.longValue()) {
            throw new NotFoundException("Ur not the owner or booker!");
        }
        return toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingByState(Long id, State state) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found!");
        }
        List<Booking> bookingList = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerId(id, sort);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(id, now, now, sort);
                break;
            case PAST:
                bookingList = bookingRepository.findAllByBookerIdAndEndBefore(id, now, sort);
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByBookerIdAndStartAfter(id, now, sort);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByBookerIdAndStatus(id, Status.WAITING, sort);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByBookerIdAndStatus(id, Status.REJECTED, sort);
                break;
        }
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    public List<BookingDto> getAllOwnersBookingByState(Long id, State state) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found!");
        }
        List<Booking> bookingList = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByItemOwnerId(id, sort);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(id, now, now, sort);
                break;
            case PAST:
                bookingList = bookingRepository.findAllByItemOwnerIdAndEndBefore(id, now, sort);
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStartAfter(id, now, sort);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStatus(id, Status.WAITING, sort);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStatus(id, Status.REJECTED, sort);
                break;
        }
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}
