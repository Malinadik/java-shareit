package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface ItemRequestsService {

    ItemRequestDto addRequest(Long id, ItemRequestDto itemRequestDto);

    List<ItemRequestWithItems> getOwnRequests(Long id, int from, int size) throws ValidationException;

    List<ItemRequestWithItems> getAll(Long id, int from, int size) throws ValidationException;

    ItemRequestWithItems getRequestById(Long id, Long requestId);
}
