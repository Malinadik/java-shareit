package ru.practicum.shareit.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class ItemRequestDtoJsonTest {

    ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setDateFormat(new StdDateFormat().withColonInTimeZone(true));


    @Test
    public void testSerializeToJson() throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L)
                .description("Test description").requestor(new ItemRequestDto.User())
                .created(Timestamp.valueOf(LocalDateTime.of(2023, 5, 1, 13, 0))).build();

        String expectedJson = "{\"id\":1,\"description\":\"Test description\",\"requestor\"" +
                ":{\"id\":null,\"name\":null,\"email\":null},\"created\":\"2023-05-01T10:00:00.000+00:00\"}";
        String actualJson = objectMapper.writeValueAsString(itemRequestDto);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testDeserializeFromJson() throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        String json = "{\"id\":1,\"description\":\"Test description\"," +
                "\"requestor\":{},\"created\":\"2023-05-01T10:00:00.000+00:00\"}";
        ItemRequestDto expectedItemRequestDto = ItemRequestDto.builder().id(1L).description("Test description").requestor(ItemRequestDto.User.builder().build()).created(Timestamp.valueOf(LocalDateTime.of(2023, 5, 1, 13, 0))).build();

        ItemRequestDto actualItemRequestDto = objectMapper.readValue(json, ItemRequestDto.class);

        assertEquals(expectedItemRequestDto, actualItemRequestDto);
    }
}
