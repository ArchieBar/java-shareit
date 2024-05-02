package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.CommentDto;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.model.item.ItemMapper;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.item.service.ItemServiceJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @InjectMocks
    private ItemServiceJpa itemService;

    @Mock
    private ItemRepositoryJpa itemRepository;

    @Mock
    private UserRepositoryJpa userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Test
    public void getItemById() {
        Long itemId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        when(bookingRepository.findFirstBookingByItemIdAndStartTimeBeforeAndStatus(any(), any(), any(), any()))
                .thenReturn(Page.empty());

        when(bookingRepository.findFirstBookingByItemIdAndStartTimeAfterAndStatus(any(), any(), any(), any()))
                .thenReturn(Page.empty());

        ItemWithBookingDto itemWithBookingDto = itemService.getItemById(itemId, userId);

        assertEquals(1, itemWithBookingDto.getId());
    }

    @Test
    public void getAllItemsUserById() {
        Long userId = 1L;

        when(itemRepository.findAllItemsByOwnerId(eq(userId), any()))
                .thenReturn(new PageImpl<>(List.of(new Item())));

        when(bookingRepository.findFirstBookingByItemIdAndStartTimeBeforeAndStatus(any(), any(), any(), any()))
                .thenReturn(Page.empty());

        when(bookingRepository.findFirstBookingByItemIdAndStartTimeAfterAndStatus(any(), any(), any(), any()))
                .thenReturn(Page.empty());

        List<ItemWithBookingDto> itemWithBookingDtoList = itemService.getAllItemsUserById(userId, 0, 10);

        assertEquals(1, itemWithBookingDtoList.size());
    }

    //FIXME
    // Возможно стоит переписать тест, т.к. он ничего не проверяет
    @Test
    public void searchThingByText() {
        String text = "вод";

        Item item = new Item();
        item.setName("Водяной пистолет");

        when(itemRepository.findAllItemsByText(eq(text), any()))
                .thenReturn(new PageImpl<>(List.of(item)));

        List<ItemDto> itemDtoList = itemService.searchThingByText(text, 0, 10);

        assertEquals("Водяной пистолет", itemDtoList.get(0).getName());
    }

    @Test
    public void createItem() {
        Long itemId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("имя");

        Item item = ItemMapper.toNewItem(itemDto, user);
        item.setId(itemId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);

        ItemDto itemDtoReturn = itemService.createItem(itemDto, userId);

        assertEquals(1, itemDtoReturn.getId());
    }

    @Test
    public void updateItem() {
        Long itemId = 1L;
        Long userId = 1L;

        ItemDto itemDto = new ItemDto();
        itemDto.setName("DtoName");

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("DtoName");
        item.setOwner(user);

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));

        when(itemRepository.save(item))
                .thenReturn(item);

        ItemDto itemDtoReturn = itemService.updateItem(itemDto, itemId, userId);

        assertEquals("DtoName", itemDtoReturn.getName());
    }

    @Test
    public void createComment() {
        Item item = Mockito.mock(Item.class);
        User user = Mockito.mock(User.class);
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test Comment");
        Long itemId = 1L;
        Long userId = 1L;

        List<Booking> bookings = new ArrayList<>();
        bookings.add(Mockito.mock(Booking.class));

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findAllBookingByItemIdAndBookerIdAndEndTimeBefore(eq(itemId), eq(userId), any(LocalDateTime.class)))
                .thenReturn(bookings);
        when(commentRepository.save(any(Comment.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Comment result = itemService.createComment(commentDto, itemId, userId);

        assertEquals(commentDto.getText(), result.getText());

        verify(itemRepository).findById(itemId);
        verify(userRepository).findById(userId);
        verify(bookingRepository).findAllBookingByItemIdAndBookerIdAndEndTimeBefore(eq(itemId), eq(userId), any(LocalDateTime.class));
        verify(commentRepository).save(Mockito.any(Comment.class));
    }
}
