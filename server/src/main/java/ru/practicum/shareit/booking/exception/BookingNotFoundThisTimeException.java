package ru.practicum.shareit.booking.exception;

public class BookingNotFoundThisTimeException extends RuntimeException {
    public BookingNotFoundThisTimeException(String message) {
        super(message);
    }
}
