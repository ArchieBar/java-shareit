package ru.practicum.shareit.booking.model.state.exception;


public class InvalidArgumentStateException extends RuntimeException {
    public InvalidArgumentStateException(String message) {
        super(String.format("Unknown state: %s", message));
    }
}
