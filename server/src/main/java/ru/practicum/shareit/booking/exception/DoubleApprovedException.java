package ru.practicum.shareit.booking.exception;

public class DoubleApprovedException extends RuntimeException {
    public DoubleApprovedException(String message) {
        super(message);
    }
}
