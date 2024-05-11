package ru.practicum.shareit.item.model.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    @ManyToOne
    private ItemRequest request;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Comment> comments;

    public Item(String name, String description, Boolean available, User owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.comments = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
