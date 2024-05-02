package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.status.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
public class BookingRepositoryQueryTest {
    private final Pageable pageable = PageRequest.of(0, 10);
    @Autowired
    private BookingRepository repository;
    @Autowired
    private ItemRepositoryJpa itemRepository;
    @Autowired
    private UserRepositoryJpa userRepository;
    private User booker;
    private Item item;
    private Booking booking;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setName("user");
        user.setEmail("user@email.email");
        user = userRepository.save(user);

        booker = new User();
        booker.setName("booker");
        booker.setEmail("booker@email.email");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setComments(new ArrayList<>());
        item = itemRepository.save(item);

        booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);
        booking.setStartTime(LocalDateTime.now().minusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(2));
        booking = repository.save(booking);
    }

    @Test
    public void findAllCurrentBooking() {
        Booking result = repository.findAllCurrentBooking(booker.getId(), LocalDateTime.now(), pageable)
                .getContent().get(0);

        assertThat(result, equalTo(booking));
    }

    @Test
    public void findAllCurrentBookingByItemOwnerId() {
        Booking result = repository.findAllCurrentBookingByItemOwnerId
                (item.getOwner().getId(), LocalDateTime.now(), pageable).getContent().get(0);

        assertThat(result, equalTo(booking));
    }
}
