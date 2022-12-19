package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final ItemRequestRepository itemRequestRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> findAll(Long userId, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User ID %s doesn't exist.", userId)));

        PageRequest pageRequest = createPageRequest(from, size);

        List<ItemRequest> itemRequests = itemRequestRepository
                .findAllByRequestorIdNotLike(userId, pageRequest);
       log.info("ItemRequestService: findAll implementation. User ID {}.", userId);
        return getItemRequestDtoList(itemRequests);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> findAllByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User ID %s doesn't exist.", userId)));

        List<ItemRequest> itemRequests = itemRequestRepository
                .findAllByRequestorIdOrderByCreatedAsc(userId);
        log.info("ItemRequestService: findAllByUserId implementation. User ID {}.", userId);
        return getItemRequestDtoList(itemRequests);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequestDto findById(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User ID %s doesn't exist.", userId)));
        ItemRequest itemRequest = itemRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("ItemRequest ID %s doesn't exist.", requestId)));

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemRepository.findAllByRequestId(itemRequest.getId())
                .stream()
                .map(ItemMapper::toItemDtoResponse)
                .collect(toList()));
        log.info("ItemRequestService: findById implementation. User ID {}, request ID {}.", userId, requestId);
        return itemRequestDto;
    }

    @Transactional
    @Override
    public ItemRequestDto save(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User ID %s doesn't exist.", userId)));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(user);
        itemRequestRepository.save(itemRequest);
        log.info("ItemRequestService: save implementation. User ID {}, request ID {}.", userId, itemRequest.getId());
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    private List<ItemRequestDto> getItemRequestDtoList(List<ItemRequest> itemRequests) {

        Map<ItemRequest, List<Item>> itemsAll = itemRepository
                .findAllByRequestIn(itemRequests)
                .stream()
                .collect(groupingBy(Item::getRequest, toList()));

        return itemRequests
                .stream()
                .map(itemRequest -> {
            ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
            List<Item> itemList = itemsAll.getOrDefault(itemRequest, Collections.emptyList());
            itemRequestDto.setItems(itemList
                    .stream()
                    .map(ItemMapper::toItemDtoResponse)
                    .collect(toList()));
            return itemRequestDto;
                })
                .collect(toList());
    }

    private PageRequest createPageRequest(int from, int size) {
        return PageRequest.of(from / size, size);
    }
}
