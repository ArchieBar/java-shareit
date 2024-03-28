package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    //FIXME
    // Скорее всего этот класс должен содержать только следующие поля:
    // private String name;
    // private String description;
    // private Boolean available;

    /*
     * Пока очень плохо понимаю как нужно использовать Dto классы?
     * Предположу, что не так, как я делаю это сейчас
     */

    private Long id;
    private Long owner;
    private String name;
    private String description;
    private Boolean available;
}
