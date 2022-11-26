package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import java.util.List;

public interface ItemService {

    List<ItemDtoResponse> findAllByOwnerId(Long userId);

    List<ItemDtoResponse> findAllByText(String text);

    ItemDtoResponse findById(Long userId, Long itemId);

    ItemDtoResponse save(Long userId, ItemDtoRequest itemDtoRequest);

    ItemDtoResponse update(Long userId, Long itemId, ItemDtoRequest itemDtoRequest);

    void delete(Long userId, Long itemId);
}
