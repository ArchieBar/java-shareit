package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.model.ErrorResponse;
import ru.practicum.shareit.item.exception.ItemDuplicateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnershipException;
import ru.practicum.shareit.user.exception.UserDuplicateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({ItemNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse notFoundObjectHandler(RuntimeException exception) {
        log.info("Объект не найден: {}", exception.getMessage());
        return new ErrorResponse("Объект не найден", exception.getMessage());
    }

    @ExceptionHandler({ItemDuplicateException.class, UserDuplicateException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    private ErrorResponse illegalArgumentHandler(RuntimeException exception) {
        log.info("Недопустимый аргумент объекта: {}", exception.getMessage());
        return new ErrorResponse("Недопустимый аргумент объекта", exception.getMessage());
    }

    @ExceptionHandler({ItemOwnershipException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private ErrorResponse objectAccessDeniedHandler(RuntimeException exception) {
        log.info("Отказ в доступе к объекту: {}", exception.getMessage());
        return new ErrorResponse("Отказ в доступе к объекту", exception.getMessage());
    }

    //TODO
    // Изменить обработку ошибок по примеру из этой статьи:
    // https://struchkov.dev/blog/ru/spring-boot-validation/#%D0%B3%D1%80%D1%83%D0%BF%D0%BF%D1%8B-%D0%B2%D0%B0%D0%BB%D0%B8%D0%B4%D0%B0%D1%86%D0%B8%D0%B9
    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
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
