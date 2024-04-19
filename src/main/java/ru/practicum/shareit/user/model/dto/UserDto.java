package ru.practicum.shareit.user.model.dto;

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

    @NotBlank(groups = OnCreate.class, message = "Имя пользователя не может быть пустым.")
    private String name;

    @Email(groups = {OnCreate.class, OnUpdate.class},
            message = "Email пользователя должен быть формата: email@email.email.")
    @NotNull(groups = OnCreate.class, message = "Email пользователя не может быть пустым.")
    private String email;

    //FIXME
    // Вынести в отдельный интерфейс
    public interface OnCreate {
    }

    public interface OnUpdate {
    }
}
