package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.state.exception.InvalidArgumentStateException;
import ru.practicum.shareit.exception.model.ErrorResponse;
import ru.practicum.shareit.item.exception.ItemDuplicateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnershipException;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.exception.RequestOwnershipException;
import ru.practicum.shareit.user.exception.UserDuplicateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({
            ItemNotFoundException.class,
            UserNotFoundException.class,
            BookingNotFoundException.class,
            BookingOwnershipException.class,
            BookingYourOwnException.class,
            RequestNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse notFoundObjectHandler(RuntimeException exception) {
        log.info("Объект не найден: {}", exception.getMessage());
        return new ErrorResponse("Объект не найден", exception.getMessage());
    }

    @ExceptionHandler({ItemNotAvailableForBookingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorResponse notAvailableBookingHandler(RuntimeException e) {
        log.info("Вещь не доступна для бронирования: {}", e.getMessage());
        return new ErrorResponse("Вещь не доступна для бронирования", e.getMessage());
    }

    @ExceptionHandler({
            ItemDuplicateException.class,
            UserDuplicateException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    private ErrorResponse illegalArgumentHandler(RuntimeException exception) {
        log.info("Недопустимый аргумент объекта: {}", exception.getMessage());
        return new ErrorResponse("Недопустимый аргумент объекта", exception.getMessage());
    }

    /*
     * Иначе тесты в постамане не проходят...
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentsStateException(InvalidArgumentStateException e) {
        log.info(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getMessage());
    }

    @ExceptionHandler({
            ItemOwnershipException.class,
            RequestOwnershipException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private ErrorResponse objectAccessDeniedHandler(RuntimeException exception) {
        log.info("Отказ в доступе к объекту: {}", exception.getMessage());
        return new ErrorResponse("Отказ в доступе к объекту", exception.getMessage());
    }

    //TODO
    // Изменить обработку ошибок по примеру из этой статьи:
    // https://struchkov.dev/blog/ru/spring-boot-validation/#%D0%BA%D0%BE%D0%BD%D0%BA%D1%80%D0%B5%D1%82%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D1%8F-%D0%BE%D1%88%D0%B8%D0%B1%D0%BE%D0%BA
    @ExceptionHandler({
            ValidationException.class,
            MethodArgumentNotValidException.class,
            BookingStartTimeException.class,
            DoubleApprovedException.class,
            BookingNotFoundThisTimeException.class,
            IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse objectValidationException(Exception exception) {
        log.info("Ошибка валидации: {}", exception.getMessage());
        return new ErrorResponse("Ошибка валидации", exception.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ErrorResponse serverErrorHandler(Throwable exception) {
        log.debug("Ошибка сервера: ", exception);
        return new ErrorResponse("Ошибка сервера", exception.getMessage());
    }
}
