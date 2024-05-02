package ru.practicum.shareit.requestTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.model.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class RequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private ItemRequestWithItemsDto requestResponse;

    @BeforeEach
    public void setUp() {
        requestResponse = new ItemRequestWithItemsDto(
                1L,
                "description",
                LocalDateTime.now(),
                new ArrayList<>()
        );
    }

    @Test
    public void getAllRequestsByUserId() throws Exception {
        when(itemRequestService.getAllRequestsByUserId(any(Long.class)))
                .thenReturn(List.of(requestResponse));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestResponse.getDescription()), String.class));
    }

    @Test
    public void getAllRequests() throws Exception {
        when(itemRequestService.getAllRequests(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(requestResponse));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestResponse.getDescription()), String.class));
    }

    @Test
    public void getRequestById() throws Exception {
        when(itemRequestService.getRequestById(any(Long.class), any(Long.class)))
                .thenReturn(requestResponse);

        mvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestResponse.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestResponse.getDescription()), String.class));
    }

    @Test
    public void createRequest() throws Exception {
        ItemRequestCreatedDto requestCreated = new ItemRequestCreatedDto();
        requestCreated.setDescription("description");

        when(itemRequestService.createRequest(any(ItemRequestCreatedDto.class), anyLong()))
                .thenReturn(requestResponse);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestCreated))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(requestResponse.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestResponse.getDescription()), String.class));
    }
}
