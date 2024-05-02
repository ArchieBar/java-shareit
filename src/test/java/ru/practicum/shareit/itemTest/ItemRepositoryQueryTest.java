package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
public class ItemRepositoryQueryTest {
    @Autowired
    private ItemRepositoryJpa repository;

    @Autowired
    private UserRepositoryJpa userRepository;

    @Test
    public void findAllItemsByText() {
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User();
        user.setName("name");
        user.setEmail("email@email.email");
        user = userRepository.save(user);

        Item item = new Item();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setComments(new ArrayList<>());
        item = repository.save(item);

        Item itemSec = new Item();
        itemSec.setName("eman");
        itemSec.setDescription("noitpircsed");
        itemSec.setAvailable(true);
        itemSec.setOwner(user);
        itemSec.setComments(new ArrayList<>());
        itemSec = repository.save(itemSec);

        Item result = repository.findAllItemsByText("name", pageable).getContent().get(0);
        Item resultSec = repository.findAllItemsByText("ema", pageable).getContent().get(0);

        assertThat(result, equalTo(item));
        assertThat(resultSec, equalTo(itemSec));
    }

}
