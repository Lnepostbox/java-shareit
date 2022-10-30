package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import java.util.List;

public interface ItemService {

    List<Item> findAllItems(Long userId);

    Item findItemById(Long userId, Long itemId);

    List<Item> findItemsByText(String text);

    Item createItem(Long userId, Item item);

    Item updateItem(Long userId, Long itemId, Item item);

    void deleteItemById(Long userId, Long itemId);
}
