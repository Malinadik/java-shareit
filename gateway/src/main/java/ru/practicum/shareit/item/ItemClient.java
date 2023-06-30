package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX)).requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public ResponseEntity<Object> getItems(Long id, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/?from={from}&size={size}", id, parameters);
    }

    public ResponseEntity<Object> getItemById(Long id, Long itemId) {
        return get("/" + itemId, id);
    }

    public ResponseEntity<Object> searchItems(Long id, String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", id, parameters);
    }

    public ResponseEntity<Object> updateItem(Long id, ItemDto itemDto, Long itemId) {
        return patch("/" + itemId, id, itemDto);
    }

    public ResponseEntity<Object> addItem(Long id, ItemDto itemDto) {
        return post("", id, itemDto);
    }

    public ResponseEntity<Object> addComment(Long id, Long itemId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", id, commentDto);
    }


}
