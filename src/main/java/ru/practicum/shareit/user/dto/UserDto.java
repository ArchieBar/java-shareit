package ru.practicum.shareit.user.dto;

import javax.validation.constraints.Email;

public class UserDto {
    private String name;
    @Email
    private String email;
}
