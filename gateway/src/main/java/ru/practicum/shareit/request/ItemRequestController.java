package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final RequestClient client;

    @PostMapping()
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") Long id,
                                     @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return client.addRequest(id, itemRequestDto);
    }

    @Validated
    @GetMapping()
    public ResponseEntity<Object> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long id,
                                                     @RequestParam(defaultValue = "0") @Min(0) int from,
                                                     @RequestParam(defaultValue = "10") int size) throws ValidationException {

        return client.getOwnRequests(id, from, size);
    }

    @Validated
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long id,
                                                     @Min(0) @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) throws ValidationException {
        return client.getAllRequests(id, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id")
                                               Long id, @PathVariable Long requestId) {
        return client.getRequestById(id, requestId);
    }

}
