package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static List<ItemDto> toListItemDto(List<Item> listItem) {
        return listItem.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    /*
     * Ха, а как правильно из Dto класса сделать обычную сущность?
     * Передавать нужные параметры отдельно? Как у меня сделано в этом классе?
     *
     * Допустим так, а как корректно сделать из List<Dto> -> List<Object>?
     * Когда у тебя совпадают поля это легко, можно сделать стримами, а когда нет?
     * Как в этом случае?
     *
     * Пока не понимаю :(
     */
    public static Item toItem(ItemDto itemDto, Long idOwner) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                idOwner
        );
    }
}
