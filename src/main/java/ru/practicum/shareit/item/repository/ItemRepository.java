package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> findAllItems(Long userId);

    Optional<Item> findItemById(Long itemId);

    List<Item> findItemsByText(String text);

    Item createItem(Long userId, Item item);

    Item updateItem(Long userId, Long itemId, Item item);

    void deleteItemById(Long itemId);

    boolean itemExists(Long itemId);
}
