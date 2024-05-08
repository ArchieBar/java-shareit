package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;

    public CommentDto(String authorName, CommentCreateDto commentDto) {
        this.text = commentDto.getText();
        this.authorName = authorName;
        this.created = LocalDateTime.now();
    }
}
