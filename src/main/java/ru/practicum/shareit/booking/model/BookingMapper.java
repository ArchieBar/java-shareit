package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingResponseDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getItem().getId(),
                booking.getStatus().name(),
                booking.getStartTime(),
                booking.getEndTime()
        );
    }

    public static List<BookingDto> toBookingDtoList(List<Booking> bookingList) {
        return bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                UserMapper.toUserDto(booking.getBooker()),
                ItemMapper.toItemDto(booking.getItem()),
                booking.getStatus(),
                booking.getStartTime(),
                booking.getEndTime()
        );
    }

    public static List<BookingResponseDto> toBookingResponseDtoList(List<Booking> bookingList) {
        return bookingList.stream()
                .map(BookingMapper::toBookingResponseDto)
                .sorted(Comparator.comparing(BookingResponseDto::getStart).reversed())
                .collect(Collectors.toList());
    }
}
