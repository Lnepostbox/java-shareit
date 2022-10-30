package ru.practicum.shareit.request.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemRequestRepositoryInMemory implements ItemRequestRepository {
    private final HashMap<Long, ItemRequest> itemRequests = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public List<ItemRequest> findAllItemRequests() {
        return new ArrayList<>(itemRequests.values());
    }

    @Override
    public Optional<ItemRequest> findItemRequestById(Long itemRequestId) {
        return Optional.of(itemRequests.get(itemRequestId));
    }

    @Override
    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        if (itemRequest.getId() == null) {
            itemRequest.setId(nextId++);
        }
        itemRequests.put(itemRequest.getId(), itemRequest);
        return itemRequest;
    }

    @Override
    public ItemRequest updateItemRequest(ItemRequest itemRequest) {
        itemRequests.replace(itemRequest.getId(), itemRequest);
        return itemRequest;
    }

    @Override
    public void deleteItemRequestById(Long itemRequestId) {
        itemRequests.remove(itemRequestId);
    }

    @Override
    public boolean itemRequestExists(Long itemRequestId) {
        return itemRequests.containsKey(itemRequestId);
    }
}
