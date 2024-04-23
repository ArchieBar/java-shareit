package ru.practicum.shareit.booking.model.state.exception;


public class IllegalArgumentStateException extends RuntimeException {
    public IllegalArgumentStateException(String message) {
        super(String.format("Unknown state: %s", message));
    }
}
