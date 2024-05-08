package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.status.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllBookingByBookerId(Long userId, Pageable pageable);

    Page<Booking> findAllBookingByItemOwnerId(Long ownerId, Pageable pageable);

    Page<Booking> findAllBookingByBookerIdAndStatus(Long userId, Status status, Pageable pageable);

    Page<Booking> findAllBookingByItemOwnerIdAndStatus(Long userId, Status status, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :userId " +
            "AND b.startTime <= :time")
    Page<Booking> findAllBookingByBookerIdAndStartTimeAfter(Long userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findAllBookingByItemOwnerIdAndStartTimeAfter(Long userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findAllBookingByBookerIdAndEndTimeBeforeAndStatus(Long userId, LocalDateTime time,
                                                                    Status status, Pageable pageable);

    Page<Booking> findAllBookingByItemOwnerIdAndEndTimeBeforeAndStatus(Long userId, LocalDateTime time, Status status, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.startTime <= :time " +
            "AND b.status = :status " +
            "ORDER BY b.endTime DESC")
    Page<Booking> findFirstBookingByItemIdAndStartTimeBeforeAndStatus(Long itemId,
                                                                      LocalDateTime time,
                                                                      Pageable pageable,
                                                                      Status status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.startTime > :time " +
            "AND b.status = :status " +
            "ORDER BY b.startTime")
    Page<Booking> findFirstBookingByItemIdAndStartTimeAfterAndStatus(Long itemId, LocalDateTime time, Pageable pageable, Status status);

    List<Booking> findAllBookingByItemIdAndBookerIdAndEndTimeBefore(Long itemId, Long bookerId, LocalDateTime time);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :userId " +
            "AND :now BETWEEN b.startTime AND b.endTime")
    Page<Booking> findAllCurrentBooking(Long userId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :userId " +
            "AND :now BETWEEN b.startTime AND b.endTime")
    Page<Booking> findAllCurrentBookingByItemOwnerId(Long userId, LocalDateTime now, Pageable pageable);
}
