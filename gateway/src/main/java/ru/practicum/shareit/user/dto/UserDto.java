package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(groups = Marker.OnCreate.class, message = "Имя пользователя не может быть пустым.")
    private String name;

    @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class},
            message = "Email пользователя должен быть формата: email@email.email.")
    @NotNull(groups = Marker.OnCreate.class, message = "Email пользователя не может быть пустым.")
    private String email;
}
