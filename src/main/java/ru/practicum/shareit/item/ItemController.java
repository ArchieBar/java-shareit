package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    /*
     * В общем, я вообще не понимаю, как правильно использовать эти Dto классы.
     * Я прочитал, что они используются для передачи данных между слоями приложения, но что это значит?
     * Означает это, то, что к примеру в методе update мне нужно принимать на вход ItemDto и смотреть параметры,
     * которые там есть и заменять их у нужного объекта?
     * Или Dto классы используются для возврата пользователю определённой информации?
     */

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
        return itemService.searchThingByText(text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@Valid @RequestBody Item item,
                              @RequestHeader("X-Sharer-User-Id") Long idOwner) {
        log.info("Вызов POST-операции \"createItem\"");
        return itemService.createItem(item, idOwner);
    }

    /*
     * Может тут лучше из тела запроса создавать ItemDto, а не Item?
     */
    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestBody Item item,
                              @PathVariable("itemId") Long idItem,
                              @RequestHeader("X-Sharer-User-Id") Long idOwner) {
        log.info("Вызов PATCH-операции \"updateItem\"");
        return itemService.updateItem(item, idItem, idOwner);
    }
}
