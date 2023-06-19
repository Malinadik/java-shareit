package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.model.Item;

public class BookingMapper {
    public static Booking toBooking(BookingDto bookingDto) {
        BookingDto.Item item = bookingDto.getItem();
        return Booking.builder()
                .id(bookingDto.getId())
                .item(new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getOwner(), item.getRequest()))
                .booker(bookingDto.getBooker())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .build();
    }

    public static BookingDto toBookingDto(Booking booking) {
        Item item = booking.getItem();
        return BookingDto.builder()
                .id(booking.getId())
                .item(new BookingDto.Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getOwner(), item.getRequest()))
                .booker(booking.getBooker())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    public static BookingItemDto toItemBookingDto(Booking booking) {
        return BookingItemDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }

}
