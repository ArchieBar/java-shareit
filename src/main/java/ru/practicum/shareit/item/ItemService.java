package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepositoryInterface;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.text.MessageFormat;
import java.util.List;

@Service
public class ItemService {
    private final ItemRepositoryInterface itemRepository;
    private final UserRepositoryInterface userRepository;

    @Autowired
    public ItemService(ItemRepositoryInterface itemRepository, UserRepositoryInterface userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public ItemDto getItemById(Long idItem) {
        return itemRepository.getItemById(idItem);
    }

    public List<ItemDto> getAllItemsUserById(Long idUser) {
        verifyExistenceOfUser(idUser);
        return itemRepository.getAllItemsUserById(idUser);
    }

    public List<ItemDto> searchThingByText(String text) {
        return itemRepository.searchThingByText(text);
    }

    public ItemDto createItem(Item item, Long idOwner) {
        verifyExistenceOfUser(idOwner);
        return itemRepository.createItem(item, idOwner);
    }

    public ItemDto updateItem(Item item, Long idItem, Long idOwner) {
        verifyExistenceOfUser(idOwner);
        return itemRepository.updateItem(item, idItem, idOwner);
    }

    private void verifyExistenceOfUser(Long idUser) {
        if (!userRepository.findUserById(idUser)) {
            throw new UserNotFoundException(
                    MessageFormat.format("Пользователь с id: {0} не найден.", idUser));
        }
    }
}
