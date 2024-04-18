package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@PathVariable("itemId") Long idItem) {
        log.info("Вызов GET-операции \"getItemById\"");
        return itemService.getItemById(idItem);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getAllItemsUserById(@RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("Вызов GET-операции \"getAllItemUserById\"");
        return itemService.getAllItemsUserById(idUser);
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
