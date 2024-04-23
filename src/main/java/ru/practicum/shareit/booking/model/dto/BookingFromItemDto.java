package ru.practicum.shareit.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.status.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingFromItemDto {
    private Long id;
    private Long bookerId;
    private Status status;
    private LocalDateTime start;
    private LocalDateTime end;
}
