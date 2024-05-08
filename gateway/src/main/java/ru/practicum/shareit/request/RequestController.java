package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> getAllRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Вызов GET-операции getAllRequestsByUserId");
        return requestClient.getAllRequestsByUserId(ownerId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Вызов GET-операции getAllRequests");
        return requestClient.getAllRequests(ownerId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable("requestId") Long requestId,
                                                 @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Вызов GET-операции getRequestById");
        return requestClient.getRequestById(ownerId, requestId);
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestBody @Valid ItemRequestCreatedDto requestDto,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Вызов POST-метода createRequest");
        return requestClient.createRequest(userId, requestDto);
    }
}
