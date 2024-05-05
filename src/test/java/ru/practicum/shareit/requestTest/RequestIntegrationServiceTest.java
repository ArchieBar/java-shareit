package ru.practicum.shareit.requestTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
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
public class RequestIntegrationServiceTest {
    private final UserRepositoryJpa userRepository;
    private final ItemRequestServiceImpl requestService;
    private final EntityManager em;

    @Test
    public void createRequest() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@email.email");
        user = userRepository.save(user);

        ItemRequestCreatedDto requestDto = new ItemRequestCreatedDto();
        requestDto.setDescription("description");
        ItemRequestWithItemsDto serviceResult = requestService.createRequest(requestDto, user.getId());

        TypedQuery<ItemRequest> query =
                em.createQuery("SELECT i FROM ItemRequest i WHERE i.id = :id", ItemRequest.class);
        ItemRequest queryResult = query.setParameter("id", serviceResult.getId()).getSingleResult();

        assertThat(queryResult.getId(), equalTo(serviceResult.getId()));
        assertThat(queryResult.getCreated(), equalTo(serviceResult.getCreated()));
    }
}
