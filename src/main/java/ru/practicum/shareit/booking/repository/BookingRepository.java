package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.status.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllBookingByBookerId(Long userId);

    List<Booking> findAllBookingByItemOwnerId(Long ownerId);

    List<Booking> findAllBookingByBookerIdAndStatus(Long userId, Status status);

    List<Booking> findAllBookingByItemOwnerIdAndStatus(Long userId, Status status);

    List<Booking> findAllBookingByBookerIdAndStartTimeAfter(Long userId, LocalDateTime time);

    List<Booking> findAllBookingByItemOwnerIdAndStartTimeAfter(Long userId, LocalDateTime time);

    List<Booking> findAllBookingByBookerIdAndEndTimeBeforeAndStatus(Long userId, LocalDateTime time, Status status);

    List<Booking> findAllBookingByItemOwnerIdAndEndTimeBeforeAndStatus(Long userId, LocalDateTime time, Status status);

    Page<Booking> findFirstBookingByItemIdAndStartTimeBeforeAndStatus(Long itemId, LocalDateTime time, Pageable pageable, Status status);

    Page<Booking> findFirstBookingByItemIdAndStartTimeAfterAndStatus(Long itemId, LocalDateTime time, Pageable pageable, Status status);

    List<Booking> findAllBookingByItemIdAndBookerIdAndEndTimeBefore(Long itemId, Long bookerId, LocalDateTime time);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :userId " +
            "AND :now BETWEEN b.startTime AND b.endTime")
    List<Booking> findAllCurrentBooking(Long userId, LocalDateTime now);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :userId " +
            "AND :now BETWEEN b.startTime AND b.endTime")
    List<Booking> findAllCurrentBookingByItemOwnerId(Long userId, LocalDateTime now);
}
