package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    @Test
    void toBooking() {
        BookingDto bookingDto = BookingDto.builder().id(1L).item(BookingDto.Item.builder().id(1L).owner(BookingDto.User.builder().id(1L).build()).build()).booker(BookingDto.User.builder().id(1L).build()).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(2)).status(Status.WAITING).build();
        Booking booking = BookingMapper.toBooking(bookingDto);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDto.getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.getStart(), booking.getStart());
        assertEquals(bookingDto.getEnd(), booking.getEnd());
        assertEquals(bookingDto.getStatus(), booking.getStatus());
    }

    @Test
    void toBookingDto() {
        Booking booking = Booking.builder().id(1L).item(Item.builder().id(1L).owner(User.builder().id(1L).build()).build()).booker(User.builder().id(1L).build()).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(2)).status(Status.WAITING).build();
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void toItemBookingDto() {
        Booking booking = Booking.builder().id(1L).item(Item.builder().id(1L).build()).booker(User.builder().id(1L).build()).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(2)).status(Status.WAITING).build();
        BookingItemDto bookingItemDto = BookingMapper.toItemBookingDto(booking);
        assertEquals(booking.getId(), bookingItemDto.getId());
        assertEquals(booking.getItem().getId(), bookingItemDto.getItemId());
        assertEquals(booking.getBooker().getId(), bookingItemDto.getBookerId());
        assertEquals(booking.getStart(), bookingItemDto.getStart());
        assertEquals(booking.getEnd(), bookingItemDto.getEnd());
    }
}
