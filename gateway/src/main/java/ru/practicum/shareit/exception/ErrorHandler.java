package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.exception.InvalidArgumentStateException;
import ru.practicum.shareit.exception.model.ErrorResponse;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    //TODO
    // Изменить обработку ошибок по примеру из этой статьи:
    // https://struchkov.dev/blog/ru/spring-boot-validation/#%D0%BA%D0%BE%D0%BD%D0%BA%D1%80%D0%B5%D1%82%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D1%8F-%D0%BE%D1%88%D0%B8%D0%B1%D0%BE%D0%BA
    @ExceptionHandler({
            ValidationException.class,
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse objectValidationException(Exception exception) {
        log.info("Ошибка валидации: {}", exception.getMessage());
        return new ErrorResponse("Ошибка валидации", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentsStateException(InvalidArgumentStateException e) {
        log.info(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ErrorResponse serverErrorHandler(Throwable e) {
        log.debug("Ошибка сервера: ", e);
        return new ErrorResponse("Ошибка сервера", String.format("%s: %s", e.getClass(), e.getMessage()));
    }
}
