package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestWithItemsDto> getAllRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Вызов GET-операции getAllRequestsByUserId");
        return itemRequestService.getAllRequestsByUserId(ownerId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestWithItemsDto> getAllRequests(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Min(0) Integer size) {
        log.info("Вызов GET-операции getAllRequests");
        return itemRequestService.getAllRequests(ownerId, from, size);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestWithItemsDto getRequestById(@PathVariable("requestId") Long requestId,
                                                  @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Вызов GET-операции getRequestById");
        return itemRequestService.getRequestById(requestId, ownerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestWithItemsDto createRequest(@RequestBody @Valid ItemRequestCreatedDto requestDto,
                                                 @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Вызов POST-метода createRequest");
        return itemRequestService.createRequest(requestDto, ownerId);
    }
}
