package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    List<BookingResponseDto> getAllBooking(Long ownerId,
                                           String state);

    List<BookingResponseDto> getAllBookingFromItemOwner(Long ownerId,
                                                        String state);

    BookingResponseDto getBookingById(Long userId,
                                      Long bookingId);

    BookingResponseDto createBooking(Long bookerId,
                                     BookingDto bookingDto);

    BookingResponseDto updateApprovedBooking(Long ownerId,
                                             Long bookingId,
                                             Boolean approved);
}
