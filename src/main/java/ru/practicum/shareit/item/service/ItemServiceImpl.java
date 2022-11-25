package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.*;
import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDtoResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemDtoResponse> findAllByOwnerId(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);

        List<ItemDtoResponse> itemsDtoWithBookingList = items
                .stream()
                .map(item -> {
                    ItemDtoResponse itemDtoResponse = ItemMapper.toItemDtoResponse(item);

                    itemDtoResponse.setComments(commentRepository.findAllByItemId(item.getId())
                            .stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(toList()));

                    Booking lastBooking = bookingRepository.findLastBooking(LocalDateTime.now(), userId, item.getId());
                    Booking nextBooking = bookingRepository.findNextBooking(LocalDateTime.now(), userId, item.getId());
                    itemDtoResponse.setLastBooking(lastBooking == null ? null : new ItemDtoResponse.ItemBooking(
                            lastBooking.getId(),
                            lastBooking.getBooker().getId()));

                    itemDtoResponse.setNextBooking(nextBooking == null? null : new ItemDtoResponse.ItemBooking(
                            nextBooking.getId(),
                            nextBooking.getBooker().getId()));

            return itemDtoResponse;
        })
        .collect(toList());

        log.info("ItemService: findAllByOwnerId implementation. User ID {}.", userId);
        return itemsDtoWithBookingList;
    }

    @Override
    public List<ItemDtoResponse> findAllByText(String text) {
        log.info("ItemService: findAllByText implementation. Text: {}.", text);
        return itemRepository.search(text)
                .stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDtoResponse)
                .collect(toList());
    }

    @Override
    public ItemDtoResponse findById(Long userId, Long itemId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User ID %s doesn't exist.", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item ID %s doesn't exist.", itemId)));
        ItemDtoResponse itemDtoResponse = toItemDtoResponse(item);

        itemDtoResponse.setComments(commentRepository.findAllByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(toList()));

        Booking lastBooking = bookingRepository.findLastBooking(LocalDateTime.now(), userId, itemId);
        Booking nextBooking = bookingRepository.findNextBooking(LocalDateTime.now(), userId, itemId);

        itemDtoResponse.setLastBooking(lastBooking == null ? null : new ItemDtoResponse.ItemBooking(
                lastBooking.getId(),
                lastBooking.getBooker().getId()));

        itemDtoResponse.setNextBooking(nextBooking == null ? null : new ItemDtoResponse.ItemBooking(
                nextBooking.getId(),
                nextBooking.getBooker().getId()));

        log.info("ItemService: findById implementation. User ID {}, item ID {}.", userId, itemId);
        return itemDtoResponse;
    }

    @Override
    public ItemDtoResponse save(Long userId, ItemDtoRequest itemDtoRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User ID %s doesn't exist.", userId)));
        Item item = ItemMapper.toItem(itemDtoRequest);
        item.setOwner(user);
        Item itemSave = itemRepository.save(item);
        log.info("ItemService: save implementation. User ID {}, item ID {}.", userId, itemSave.getId());
        return ItemMapper.toItemDtoResponse(itemSave);
    }

    @Override
    public ItemDtoResponse update(Long userId, Long itemId, ItemDtoRequest itemDtoRequest) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item ID %s doesn't exist.", itemId)));

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException(String.format("User ID %s doesn't own item ID %s.", userId, itemId));
        }

        if (itemDtoRequest.getName() != null && !itemDtoRequest.getName().isBlank()) {
            item.setName(itemDtoRequest.getName());
        }
        if (itemDtoRequest.getDescription() != null && !itemDtoRequest.getDescription().isBlank()) {
            item.setDescription(itemDtoRequest.getDescription());
        }
        if (itemDtoRequest.getAvailable() != null) {
            item.setAvailable(itemDtoRequest.getAvailable());
        }
        log.info("ItemService: update implementation. User ID {}, itemId {}.", userId, itemId);
        return ItemMapper.toItemDtoResponse(itemRepository.save(item));
    }

    @Override
    public void delete(Long userId, Long itemId) {
        findById(userId, itemId);
        log.info("ItemService: delete implementation. Item ID {}.", itemId);
        itemRepository.deleteById(itemId);
    }
}