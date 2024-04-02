package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(groups = UserDto.onCreate.class, message = "Имя пользователя не может быть пустым.")
    private String name;

    @Email(groups = {UserDto.onCreate.class, UserDto.onUpdate.class},
            message = "Email пользователя должен быть формата: email@email.email.")
    @NotNull(groups = UserDto.onCreate.class, message = "Email пользователя не может быть пустым.")
    private String email;

    /*
     * Думаю, это лучше вынести в отдельный интерфейс, но где его лучше разместить?
     * В папке src?
     */
    public interface onCreate {}

    public interface onUpdate {}
}
