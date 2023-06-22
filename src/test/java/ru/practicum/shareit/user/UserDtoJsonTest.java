package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class UserDtoJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerializeToJson() throws JsonProcessingException {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("johndoe@example.com")
                .build();

        String expectedJson = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"johndoe@example.com\"}";
        String actualJson = objectMapper.writeValueAsString(userDto);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testDeserializeFromJson() throws JsonProcessingException {
        String json = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"johndoe@example.com\"}";

        UserDto expectedUserDto = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("johndoe@example.com")
                .build();

        UserDto actualUserDto = objectMapper.readValue(json, UserDto.class);

        assertEquals(expectedUserDto, actualUserDto);
    }
}
