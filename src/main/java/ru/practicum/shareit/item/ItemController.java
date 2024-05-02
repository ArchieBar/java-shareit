package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.CommentDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(@Qualifier("itemServiceJpa") ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemWithBookingDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable("itemId") Long idItem) {
        log.info("Вызов GET-операции \"getItemById\"");
        return itemService.getItemById(idItem, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemWithBookingDto> getAllItemsUserById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Вызов GET-операции \"getAllItemUserById\"");
        return itemService.getAllItemsUserById(userId, from, size);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchThingByText(@RequestParam("text") String text,
                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Вызов GET-операции \"searchThingByText\"");
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemService.searchThingByText(text, from, size);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long idOwner) {
        log.info("Вызов POST-операции \"createItem\"");
        return itemService.createItem(itemDto, idOwner);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public Comment createComment(@RequestBody @Valid CommentDto commentDto,
                                 @PathVariable("itemId") Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Вызов POST-операции \"createComment\"");
        return itemService.createComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable("itemId") Long idItem,
                              @RequestHeader("X-Sharer-User-Id") Long idOwner) {
        log.info("Вызов PATCH-операции \"updateItem\"");
        return itemService.updateItem(itemDto, idItem, idOwner);
    }
}
