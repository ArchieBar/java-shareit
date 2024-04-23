package ru.practicum.shareit.item.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.dto.BookingFromItemDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithBooking {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingFromItemDto lastBooking;
    private BookingFromItemDto nextBooking;
}
