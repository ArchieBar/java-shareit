package ru.practicum.shareit.user.dto;

import com.sun.xml.bind.v2.TODO;
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

    /*
     * В этой статье: https://www.baeldung.com/java-dto-pattern говорится о том
     * что нам нужно создать разные DTO классы под каждый нужный случай, возврат, создание и т.д.
     * Либо я чего-то не понял.
     *
     * Обновление: В конце статьи сказано: что не стоит создать разные классы, для разных ситуаций
     * лучше подумать о том, как использовать эти в разных условиях. В этом случае интерфейсы стоит оставить.
     * Надо найти ещё статьи по этому поводу.
     */

    //TODO
    // Надо переделать класс, пометки есть в теории
    private Long id;

    @NotBlank(groups = OnCreate.class, message = "Имя пользователя не может быть пустым.")
    private String name;

    @Email(groups = {OnCreate.class, OnUpdate.class},
            message = "Email пользователя должен быть формата: email@email.email.")
    @NotNull(groups = OnCreate.class, message = "Email пользователя не может быть пустым.")
    private String email;

    /*
     * Если создать разные классы DTO, к примеру для создания и обновления,
     * то использование интерфейсов будет не обязательно.
     * Достаточно будет правильно расставить аннотации в классах
     * и оставить только те поля, которые будут нужны в той или иной ситуации.
     */
    //FIXME
    // Вынести в отдельный интерфейс
    public interface OnCreate {
    }

    public interface OnUpdate {
    }
}
