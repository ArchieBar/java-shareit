package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepositoryJpa extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i WHERE i.owner.id = ?1")
    List<Item> findAllItemsByOwner(Long ownerId);

    @Query("SELECT i FROM Item i " +
            "WHERE (LOWER(i.name) LIKE CONCAT('%', LOWER(:textSearch), '%') " +
            "OR LOWER(i.description) LIKE CONCAT('%', LOWER(:textSearch), '%')) " +
            "AND i.available = true")
    List<Item> findAllItemsByText(String textSearch);
}
