package ru.practicum.shareit.exception.exception;

public class InvalidArgumentStateException extends RuntimeException {
    public InvalidArgumentStateException(String message) {
        super(String.format("Unknown state: %s", message));
    }
}
