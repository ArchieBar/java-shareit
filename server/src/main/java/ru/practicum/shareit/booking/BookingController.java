package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Вызов GET-операции /bookings");
        return bookingService.getAllBooking(ownerId, state, from, size);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getAllBookingsForOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Вызов GET-операции /bookings/owner");
        return bookingService.getAllBookingFromItemOwner(ownerId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("bookingId") Long bookingId) {
        log.info("Вызов GET-операции /bookings/{bookingId}");
        return bookingService.getBookingById(userId, bookingId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestBody BookingDto bookingDto) {
        log.info("Вызов POST-операции /bookings");
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto updateApprovedBooking(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                    @PathVariable("bookingId") Long bookingId,
                                                    @RequestParam("approved") Boolean approved) {
        log.info("Вызов PATCH-операции /bookings/{bookingId}");
        return bookingService.updateApprovedBooking(ownerId, bookingId, approved);
    }
}
