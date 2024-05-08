package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.request.model.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User owner;

    private String description;

    private LocalDateTime created;

    @OneToMany
    @JoinTable(
            name = "REQUESTS_ITEMS",
            joinColumns = @JoinColumn(name = "id_request", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_item", referencedColumnName = "id")
    )
    private List<Item> items;

    public ItemRequest(ItemRequestCreatedDto itemDto, User owner) {
        this.owner = owner;
        this.description = itemDto.getDescription();
        this.created = LocalDateTime.now();
        ;
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        this.items.add(item);
    }
}
