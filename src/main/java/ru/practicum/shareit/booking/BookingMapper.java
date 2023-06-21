package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingEntryDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static Booking toBooking(BookingDto bookingDto) {
        BookingDto.Item item = bookingDto.getItem();
        return Booking.builder()
                .id(bookingDto.getId())
                .item(new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                        User.builder()
                        .id(item.getOwner().getId())
                        .name(item.getOwner().getName())
                        .email(item.getOwner().getEmail()).build(),
                        item.getRequest()))
                .booker(User.builder()
                        .id(bookingDto.getBooker().getId())
                        .name(bookingDto.getBooker().getName())
                        .email(bookingDto.getBooker().getEmail()).build())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .build();
    }

    public static BookingDto toBookingDto(Booking booking) {
        Item item = booking.getItem();
        return BookingDto.builder()
                .id(booking.getId())
                .item(new BookingDto.Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                        new BookingDto.User(item.getOwner().getId(), item.getOwner().getName(), item.getOwner().getEmail()),
                        item.getRequest()))
                .booker(new BookingDto.User(booking.getBooker().getId(), booking.getBooker().getName(), booking.getBooker().getEmail()))
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

    public static Booking toBooking(BookingEntryDto bookingDto, Item item, User booker) {
        Booking booking = Booking.builder().start(bookingDto.getStart()).end(bookingDto.getEnd()).build();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        return booking;
    }
}
