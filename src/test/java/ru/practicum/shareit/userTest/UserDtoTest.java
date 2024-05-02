package ru.practicum.shareit.userTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> json;

    /*
     * Не разобрался толком с этими тестами.
     * Как сделать так, чтобы учитывалась валидация?
     * И как учитывать группы валидаций?
     */
    @Test
    public void userDtoCreated() throws IOException {
        UserDto user = new UserDto(
                1L,
                "name",
                "email@email.email"
        );

        JsonContent<UserDto> result = json.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }
}
