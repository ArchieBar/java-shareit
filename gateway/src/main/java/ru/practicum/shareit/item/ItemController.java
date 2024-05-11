package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable("itemId") Long itemId) {
        log.info("Вызов GET-операции \"getItemById\"");
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsUserById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Вызов GET-операции \"getAllItemUserById\"");
        return itemClient.getAllItemsUserById(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchThingByText(@RequestParam(name = "text") String text,
                                                    @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                    @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Вызов GET-операции \"searchThingByText\"");

        return itemClient.searchThingByText(text, from, size);

    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestBody @Valid ItemDto itemDto,
                                             @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Вызов POST-операции \"createItem\"");
        return itemClient.createItem(ownerId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestBody @Valid CommentCreateDto commentDto,
                                                @PathVariable("itemId") Long itemId,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Вызов POST-операции \"createComment\"");
        return itemClient.createComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                                             @PathVariable("itemId") Long idItem,
                                             @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Вызов PATCH-операции \"updateItem\"");
        return itemClient.updateItem(ownerId, idItem, itemDto);
    }
}
