package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.item.Item;

@Repository
public interface ItemRepositoryJpa extends JpaRepository<Item, Long> {

    Page<Item> findAllItemsByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT i FROM Item i " +
            "WHERE (LOWER(i.name) LIKE CONCAT('%', LOWER(:textSearch), '%') " +
            "OR LOWER(i.description) LIKE CONCAT('%', LOWER(:textSearch), '%')) " +
            "AND i.available = true")
    Page<Item> findAllItemsByText(String textSearch, Pageable pageable);
}
