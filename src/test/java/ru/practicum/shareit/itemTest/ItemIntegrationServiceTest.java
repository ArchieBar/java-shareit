package ru.practicum.shareit.itemTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name = test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class ItemIntegrationServiceTest {
    private final UserRepositoryJpa userRepository;
    private final ItemServiceJpa service;
    private final EntityManager em;

    @Test
    public void createItem() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@email.email");
        user = userRepository.save(user);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("nameItem");
        itemDto.setDescription("descriptionItem");
        itemDto.setAvailable(true);
        itemDto = service.createItem(itemDto, user.getId());

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class);
        Item item = query.setParameter("id", itemDto.getId()).getSingleResult();

        assertThat(item.getId(), equalTo(itemDto.getId()));
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
    }
}
