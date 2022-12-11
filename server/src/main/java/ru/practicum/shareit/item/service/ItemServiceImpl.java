package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.*;
import static java.util.stream.Collectors.groupingBy;
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
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoResponse> findAllByOwnerId(Long ownerId, int from, int size) {
        List<Item> items = itemRepository.findAllByOwnerId(ownerId, PageRequest.of(from, size));
        Map<Item, List<Comment>> commentsAll = commentRepository.findAllByItemIn(
                items,
                Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));
        Map<Item, List<Booking>> bookingsALL = bookingRepository.findAllByItemInAndStatus(
                items,
                Status.APPROVED,
                Sort.by(Sort.Direction.DESC, "start"))
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        List<ItemDtoResponse> itemsDtoWithBookingList = items
                .stream()
                .map(item -> {
                    ItemDtoResponse itemDtoResponse = ItemMapper.toItemDtoResponse(item);
                    List<Comment> comments = commentsAll.getOrDefault(item, Collections.emptyList());
                    List<Booking> bookings = bookingsALL.getOrDefault(item, Collections.emptyList());
                    LocalDateTime now = LocalDateTime.now();

                    Booking lastBooking = bookings.stream()
                            .filter(b -> ((b.getEnd().isEqual(now) || b.getEnd().isBefore(now))
                                    || (b.getStart().isEqual(now) || b.getStart().isBefore(now))))
                            .findFirst()
                            .orElse(null);

                    Booking nextBooking = bookings.stream()
                            .filter(b -> b.getStart().isAfter(now))
                            .reduce((first, second) -> second)
                            .orElse(null);

                    itemDtoResponse.setComments(comments
                            .stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(toList()));

                    itemDtoResponse.setLastBooking(lastBooking == null ? null : new ItemDtoResponse.ItemBooking(
                            lastBooking.getId(),
                            lastBooking.getBooker().getId()));

                    itemDtoResponse.setNextBooking(nextBooking == null ? null : new ItemDtoResponse.ItemBooking(
                            nextBooking.getId(),
                            nextBooking.getBooker().getId()));

                    return itemDtoResponse;
                })
                .collect(toList());

        log.info("ItemService: findAllByOwnerId implementation. User ID {}.", ownerId);
        return itemsDtoWithBookingList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoResponse> findAllByText(String text, int from, int size) {
        log.info("ItemService: findAllByText implementation. Text: {}.", text);
        return itemRepository.search(text, PageRequest.of(from, size))
                .stream()
                .map(ItemMapper::toItemDtoResponse)
                .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoResponse> findAllByRequestId(Long requestId) {
        List<Item> items = itemRepository.findAllByRequestId(requestId);
        return items.stream()
                .map(ItemMapper::toItemDtoResponse)
                .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public ItemDtoResponse save(Long userId, ItemDtoRequest itemDtoRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User ID %s doesn't exist.", userId)));
        ItemRequest itemRequest = itemDtoRequest.getRequestId() == null ? null : itemRequestRepository
                .findById(itemDtoRequest.getRequestId())
                .orElseThrow(() -> new NotFoundException("ItemRequest not found."));
        Item item = ItemMapper.toItem(itemDtoRequest);
        item.setOwner(user);
        item.setRequest(itemRequest);
        Item itemSave = itemRepository.save(item);
        log.info("ItemService: save implementation. User ID {}, item ID {}.", userId, itemSave.getId());
        return ItemMapper.toItemDtoResponse(itemSave);
    }

    @Override
    @Transactional
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
        return ItemMapper.toItemDtoResponse(item);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long itemId) {
        findById(userId, itemId);
        log.info("ItemService: delete implementation. Item ID {}.", itemId);
        itemRepository.deleteById(itemId);
    }

}