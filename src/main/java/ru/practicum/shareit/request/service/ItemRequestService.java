package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequest> findAllItemRequests();

    ItemRequest findItemRequestById(Long itemRequestId);

    ItemRequest createItemRequest(ItemRequest itemRequest);

    ItemRequest updateItemRequest(ItemRequest itemRequest);

    void deleteItemRequestById(Long itemRequestId);
}
