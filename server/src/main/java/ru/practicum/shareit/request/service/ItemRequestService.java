package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import java.util.List;

public interface ItemRequestService {

    List<ItemRequestDto> findAll(Long userId, int from, int size);

    List<ItemRequestDto> findAllByUserId(Long userId);

    ItemRequestDto findById(Long userId, Long requestId);

    ItemRequestDto save(Long userId, ItemRequestDto itemRequestDto);

}
