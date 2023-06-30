package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemForRequest {

    private Long id;

    private Long ownerId;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;

}
