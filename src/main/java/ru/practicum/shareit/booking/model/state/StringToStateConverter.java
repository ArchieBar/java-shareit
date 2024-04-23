package ru.practicum.shareit.booking.model.state;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.booking.model.state.exception.InvalidArgumentStateException;

@Component
public class StringToStateConverter implements Converter<String, State> {
    @Override
    public State convert(String source) {
        try {
            return State.valueOf(source.toUpperCase());
        } catch (MethodArgumentTypeMismatchException | IllegalArgumentException e) {
            throw new InvalidArgumentStateException(source);
        }
    }
}
