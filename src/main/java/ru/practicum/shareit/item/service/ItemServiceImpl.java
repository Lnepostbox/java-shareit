package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingItemDtoFromBooking;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDtoWithBooking;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemDtoWithBooking> findAllByOwnerId(Long userId) {
        List<ItemDtoWithBooking> itemsDtoWithBookingList = itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(ItemMapper::toItemDtoWithBooking)
                .collect(Collectors.toList());
        List<Comment> commentsAll = commentRepository.findAllByItemOwnerId(userId);

        for (ItemDtoWithBooking itemDtoWithBooking : itemsDtoWithBookingList) {

            List<Comment> comments = commentsAll
                    .stream()
                    .filter(comment -> Objects.equals(comment.getItem().getId(), itemDtoWithBooking.getId()))
                    .collect(Collectors.toList());

            if (!comments.isEmpty()) {
                itemDtoWithBooking.setComments(comments
                        .stream().map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList()));
            }
            saveLastAndNextBooking(itemDtoWithBooking);

        }
        itemsDtoWithBookingList.sort(Comparator.comparing(ItemDtoWithBooking::getId));
        log.info("Получены все вещи.");
        return itemsDtoWithBookingList;
    }

    @Override
    public ItemDtoWithBooking findById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(
                String.format("Вещь ID %s не существует.", itemId)));
        ItemDtoWithBooking itemDtoWithBooking = toItemDtoWithBooking(item);
        if (item.getOwner().getId().equals(userId)) {
            saveLastAndNextBooking(itemDtoWithBooking);
        }
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        if (!comments.isEmpty()) {
            itemDtoWithBooking.setComments(comments
                    .stream().map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList())
            );
        }
        log.info("Получеа вещь ID {}:{}", itemId, itemDtoWithBooking);
        return itemDtoWithBooking;
    }

    @Override
    public List<ItemDto> findAllByText(String text) {
        log.info("Получены вещи по тексту.");
        return itemRepository.search(text)
                .stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto save(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя ID %s не существует.", userId)));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        Item itemCreate = itemRepository.save(item);
        log.info("Добавлена вещь ID {}: {}", itemCreate.getId(), itemCreate);
        return ItemMapper.toItemDto(itemCreate);
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long userId, Long itemId) {
        Item item = ItemMapper.toItem(itemDto);
        final Item itemUpdate = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь ID %s не существует.", itemId)));
        if (!itemUpdate.getOwner().getId().equals(userId)) {
            log.error("Пользователь ID  {} не владеет вещью.", userId);
            throw new NotFoundException(String.format("Пользователь ID  %s не владеет вещью.", userId));
        }
        if (item.getAvailable() != null) {
            itemUpdate.setAvailable(item.getAvailable());
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            itemUpdate.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            itemUpdate.setDescription(item.getDescription());
        }
        itemRepository.save(itemUpdate);
        log.info("Обновлена вещь ID {}:{}", itemId, itemUpdate);
        return ItemMapper.toItemDto(itemUpdate);
    }


    @Override
    public void deleteById(Long id) {
        itemRepository.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("Пользователя ID %s не существует.", id)));
        log.info("Удалена вещь ID {}", id);
        itemRepository.deleteById(id);
    }

    private void saveLastAndNextBooking(ItemDtoWithBooking itemDtoWithBooking) {
        List<Booking> lastBookings = bookingRepository
                .findBookingsByItemIdAndEndIsBeforeOrderByEndDesc(itemDtoWithBooking.getId(),
                        LocalDateTime.now());
        if (!lastBookings.isEmpty()) {
            BookingItemDto lastBooking = toBookingItemDtoFromBooking(lastBookings.get(0));
            itemDtoWithBooking.setLastBooking(lastBooking);
        }
        List<Booking> nextBookings = bookingRepository
                .findBookingsByItemIdAndStartIsAfterOrderByStartDesc(itemDtoWithBooking.getId(),
                        LocalDateTime.now());
        if (!nextBookings.isEmpty()) {
            BookingItemDto nextBooking = toBookingItemDtoFromBooking(nextBookings.get(0));
            itemDtoWithBooking.setNextBooking(nextBooking);
        }
    }
}