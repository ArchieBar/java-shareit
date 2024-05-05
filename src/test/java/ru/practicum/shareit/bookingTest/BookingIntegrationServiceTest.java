package ru.practicum.shareit.bookingTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name = test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class BookingIntegrationServiceTest {
    private final BookingService bookingService;
    private final UserRepositoryJpa userRepository;
    private final ItemRepositoryJpa itemRepository;
    private final EntityManager em;

    @Test
    public void testCreateBooking() {
        User user = new User();
        user.setName("name");
        user.setEmail("mail@mail.mail");
        user = userRepository.save(user);

        User ownerItem = new User();
        ownerItem.setName("ownerItem");
        ownerItem.setEmail("ownerItem@mail.mail");
        ownerItem = userRepository.save(ownerItem);

        Item item = new Item();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(ownerItem);
        item.setComments(new ArrayList<>());
        item = itemRepository.save(item);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().minusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusHours(2));
        BookingResponseDto serviceResult = bookingService.createBooking(user.getId(), bookingDto);

        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking queryResult = query.setParameter("id", serviceResult.getId()).getSingleResult();

        assertThat(queryResult.getId(), equalTo(serviceResult.getId()));
        assertThat(queryResult.getItem().getId(), equalTo(serviceResult.getItem().getId()));
        assertThat(queryResult.getBooker().getEmail(), equalTo(serviceResult.getBooker().getEmail()));
    }
}
