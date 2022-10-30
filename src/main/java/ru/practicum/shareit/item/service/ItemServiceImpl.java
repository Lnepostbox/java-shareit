package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRep;

    @Override
    public List<Item> findAllItems(Long userId) {
        log.debug("ItemService: выполнено findAllItems.");
        return itemRep.findAllItems(userId);
    }

    @Override
    public Item findItemById(Long userId, Long itemId) {
        if (!itemRep.itemExists(itemId)) {
            throw new NotFoundException(Item.class.toString(), itemId);
        }
        Item item = itemRep.findItemById(itemId).orElseThrow(
                () -> new NotFoundException(Item.class.toString(), itemId)
        );
        log.debug("ItemService: выполнено findItemById - {}.", item);
        return item;
    }

    @Override
    public List<Item> findItemsByText(String text) {
        if (text != null && !text.isBlank()) {
            List<Item> searchedItems = itemRep.findItemsByText(text.toLowerCase());
            log.debug("ItemService: выполнено findItemsByText - {}.", searchedItems);
            return searchedItems;
        } else {
            log.debug("ItemService: выполено findItemsByText - текст не обнаружен.");
            return new ArrayList<>();
        }
    }

    @Override
    public Item createItem(Long userId, Item item) {
        if (item.getId() != null && itemRep.itemExists(item.getId())) {
            throw new AlreadyExistsException(Item.class.toString(), item.getId());
        }
        item = itemRep.createItem(userId, item);
        log.debug("ItemService: выполнено createItem - {}.", item);
        return item;
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item item) {
        if (!itemRep.itemExists(itemId)) {
            throw new NotFoundException(Item.class.toString(), itemId);
        }
        log.debug("ItemService: выполнено updateItem - {}.", item);
        return itemRep.updateItem(userId, itemId, item);
    }

    @Override
    public void deleteItemById(Long userId, Long itemId) {
        if (!itemRep.itemExists(itemId)) {
            throw new NotFoundException(Item.class.toString(), itemId);
        }
        itemRep.deleteItemById(itemId);
        log.debug("ItemService: выполнено deleteItemById - ID {}.", itemId);
    }
}
