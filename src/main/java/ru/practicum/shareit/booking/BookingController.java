package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getAllBookings(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                   @RequestParam(value = "state",
                                                           required = false,
                                                           defaultValue = "ALL") String state) {
        return bookingService.getAllBooking(ownerId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                           @RequestParam(value = "state",
                                                                   required = false,
                                                                   defaultValue = "ALL") String state) {
        return bookingService.getAllBookingFromItemOwner(ownerId, state);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("bookingId") Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestBody @Valid BookingDto bookingDto) {
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto updateApprovedBooking(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                    @PathVariable("bookingId") Long bookingId,
                                                    @RequestParam("approved") Boolean approved) {
        return bookingService.updateApprovedBooking(ownerId, bookingId, approved);
    }
}
