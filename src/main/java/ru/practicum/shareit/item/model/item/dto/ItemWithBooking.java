package ru.practicum.shareit.item.model.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.dto.BookingFromItemDto;
import ru.practicum.shareit.item.model.comment.Comment;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithBooking {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<Comment> comments;
    private BookingFromItemDto lastBooking;
    private BookingFromItemDto nextBooking;
}
