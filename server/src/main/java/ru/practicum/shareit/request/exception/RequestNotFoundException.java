package ru.practicum.shareit.request.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(Long requestId) {
        super(String.format("Запрос вещи с ID: %s не найден", requestId));
    }
}
