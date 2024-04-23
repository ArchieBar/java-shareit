package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemWithBooking;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    @Qualifier("ItemServiceJpa")
    private ItemService itemService;

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemWithBooking getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable("itemId") Long idItem) {
        log.info("Вызов GET-операции \"getItemById\"");
        return itemService.getItemById(idItem, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemWithBooking> getAllItemsUserById(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Вызов GET-операции \"getAllItemUserById\"");
        return itemService.getAllItemsUserById(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchThingByText(@RequestParam("text") String text) {
        log.info("Вызов GET-операции \"searchThingByText\"");
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemService.searchThingByText(text);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long idOwner) {
        log.info("Вызов POST-операции \"createItem\"");
        return itemService.createItem(itemDto, idOwner);
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
