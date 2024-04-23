package ru.practicum.shareit.booking.model.state.exception;

public class IllegalArgumentsStateException extends RuntimeException {
    public IllegalArgumentsStateException(String message) {
        super(String.format("Unknown state: %s", message));
    }
}
