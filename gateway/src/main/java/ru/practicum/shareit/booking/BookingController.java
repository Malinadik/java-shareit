package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.exceptions.NotSupportedStateException;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping()
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                 @Valid @RequestBody BookItemRequestDto bookingDto) throws ValidationException {
        return bookingClient.bookItem(id, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                     @PathVariable Long bookingId, @RequestParam Boolean approved) {
        return bookingClient.approveBooking(id, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") Long id, @PathVariable Long bookingId) {
        return bookingClient.getBooking(id, bookingId);
    }

    @Validated
    @GetMapping()
    public ResponseEntity<Object> getAllBookingByState(@RequestHeader("X-Sharer-User-Id") Long id,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @RequestParam(defaultValue = "0") @Min(0) int from,
                                                 @RequestParam(defaultValue = "20") @Positive int size) throws ValidationException {
        return bookingClient.getAllBookingByState(id, convert(state), from, size);
    }

    @Validated
    @GetMapping("/owner")
    public ResponseEntity<Object> getAllItemsBookings(@RequestHeader("X-Sharer-User-Id") Long id,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @Min(0) @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "20") @Positive int size) throws ValidationException {
        return bookingClient.getAllOwnersBookingByState(id, convert(state), from, size);
    }

    public static BookingState convert(String state) {
        try {
            return BookingState.valueOf(state);
        } catch (Exception e) {
            throw new NotSupportedStateException("Unknown state: " + state);
        }
    }

}
