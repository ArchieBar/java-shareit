package ru.practicum.shareit.request.exception;

public class RequestOwnershipException extends RuntimeException {
    public RequestOwnershipException(String message) {
        super(message);
    }

    public RequestOwnershipException(Long ownerId) {
        super(String.format("ID владельца запроса отличный от ID: %s", ownerId));
    }
}
