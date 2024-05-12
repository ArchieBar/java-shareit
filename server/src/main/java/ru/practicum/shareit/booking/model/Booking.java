package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.status.Status;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User booker;

    @ManyToOne(fetch = FetchType.EAGER)
    private Item item;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public Booking(User booker, Item item, LocalDateTime start, LocalDateTime end) {
        this.booker = booker;
        this.item = item;
        this.status = Status.WAITING;
        this.startTime = start;
        this.endTime = end;
    }
}
