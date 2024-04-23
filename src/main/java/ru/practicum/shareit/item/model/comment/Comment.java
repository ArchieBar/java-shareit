package ru.practicum.shareit.item.model.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;

    public Comment(String authorName, CommentDto commentDto) {
        this.text = commentDto.getText();
        this.authorName = authorName;
        this.created = LocalDateTime.now();
    }
}
