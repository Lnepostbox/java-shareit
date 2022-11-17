package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import java.util.List;

public interface ItemService {

    List<ItemDtoWithBooking> findAllByUserId(Long userId);

    ItemDtoWithBooking findById(Long userId, Long itemId);

    List<ItemDto> findAllByText(String text);

    ItemDto save(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long userId, Long id);

    void deleteById(Long id);
}
