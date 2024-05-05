package ru.practicum.shareit.userTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceJpa;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name = test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class UserIntegrationServiceTest {
    private final UserServiceJpa service;
    private final EntityManager em;

    @Test
    public void testCreateBooking() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("mail@mail.mail");
        userDto = service.createUser(userDto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail()).getSingleResult();

        assertThat(user.getId(), equalTo(userDto.getId()));
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    public void testDeleteUser() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("mail@mail.mail");
        userDto = service.createUser(userDto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail()).getSingleResult();

        assertThat(user.getId(), equalTo(userDto.getId()));
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));

        service.deleteUserById(userDto.getId());

        query = em.createQuery("SELECT u FROM User u where u.email = :email", User.class);
        List<User> userSec = query.setParameter("email", userDto.getEmail()).getResultList();

        assertEquals(userSec.size(), 0);
    }
}
