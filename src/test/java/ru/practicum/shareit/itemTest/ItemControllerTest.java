package ru.practicum.shareit.itemTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.CommentDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @MockBean(name = "itemServiceJpa")
    private ItemService service;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private ItemWithBookingDto itemWithBookingDto;

    private ItemDto itemDto;

    @BeforeEach
    public void setUp() {
        itemWithBookingDto = new ItemWithBookingDto(
                1L,
                "name",
                "description",
                true,
                null,
                new ArrayList<>(),
                null,
                null
        );

        itemDto = new ItemDto(
                1L,
                "name",
                "description",
                true,
                null,
                new ArrayList<>()
        );
    }

    @Test
    public void getItemId() throws Exception {
        when(service.getItemById(anyLong(), anyLong()))
                .thenReturn(itemWithBookingDto);

        mvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithBookingDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemWithBookingDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemWithBookingDto.getAvailable()), Boolean.class));
    }

    @Test
    public void getAllItemsUserById() throws Exception {
        when(service.getAllItemsUserById(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemWithBookingDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemWithBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemWithBookingDto.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(itemWithBookingDto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available", is(itemWithBookingDto.getAvailable()), Boolean.class));
    }

    @Test
    public void searchThingByText() throws Exception {
        when(service.searchThingByText(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemWithBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemWithBookingDto.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(itemWithBookingDto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available", is(itemWithBookingDto.getAvailable()), Boolean.class));
    }

    @Test
    public void createItem() throws Exception {
        when(service.createItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemWithBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithBookingDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemWithBookingDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemWithBookingDto.getAvailable()), Boolean.class));
    }

    @Test
    public void creTeComment() throws Exception {
        Comment comment = new Comment(
                1L,
                "text",
                "authorName",
                LocalDateTime.now()

        );

        CommentDto commentDto = new CommentDto(
                "text"
        );

        when(service.createComment(any(CommentDto.class), anyLong(), anyLong()))
                .thenReturn(comment);

        mvc.perform(post("/items/{itemId}/comment", 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText()), String.class));
    }

    @Test
    public void updateItem() throws Exception {
        when(service.updateItem(any(ItemDto.class), anyLong(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/{itemId}", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithBookingDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemWithBookingDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemWithBookingDto.getAvailable()), Boolean.class));

    }
}
