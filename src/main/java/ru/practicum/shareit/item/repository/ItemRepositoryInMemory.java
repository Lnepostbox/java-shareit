package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OperationAccessException;
import ru.practicum.shareit.item.model.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryInMemory implements ItemRepository {
    private final List<Item> items = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public List<Item> findAllItems(Long userId) {
        return items.stream()
                .filter(item -> userId.equals(item.getOwner().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> findItemById(Long itemId) {
        return items.stream()
                .filter(item -> itemId.equals(item.getId()))
                .findFirst();
    }

    @Override
    public List<Item> findItemsByText(String text) {
        return items.stream()
                .filter(Item::getAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(text)
                                || item.getDescription().toLowerCase().contains(text)
                )
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(Long userId, Item item) {
        if (item.getId() == null) {
            item.setId(nextId++);
        }
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item item) {
        Item oldItem = findItemById(itemId).orElseThrow(
                () -> new NotFoundException(Item.class.toString(), itemId)
        );
        if (!userId.equals(oldItem.getOwner().getId())) {
            throw new OperationAccessException(userId);
        }
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        return oldItem;
    }

    @Override
    public void deleteItemById(Long itemId) {
        items.remove(findItemById(itemId).orElseThrow(
                () -> new NotFoundException(Item.class.toString(), itemId)
        ));
    }

    @Override
    public boolean itemExists(Long itemId) {
        return items.stream()
                .anyMatch(item -> itemId.equals(item.getId()));
    }
}
