package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> findAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("ItemController: выполнено findAllItems - {}.", userId);
        return ItemMapper.toItemDtoList(itemService.findAllItems(userId));
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.debug("ItemController: выполнено findItemById - {}.", itemId);
        return ItemMapper.toItemDto(itemService.findItemById(userId, itemId));
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByParams(@RequestParam String text) {
        if (text.isBlank()) {
            log.debug("ItemController: выполено findItemByParams - текст не обнаружен.");
            return new ArrayList<>();
        } else {
            log.debug("ItemController: выполнено findItemByParams - {}.", text);
            return ItemMapper.toItemDtoList(itemService.findItemsByText(text));
        }
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.debug("ItemController: выполнено createItem - {}.", itemDto);
        Item item = ItemMapper.toItem(itemDto);
        Long requestId = itemDto.getRequest();
        return ItemMapper.toItemDto(itemService.createItem(userId, item, requestId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto
    ) {
        log.debug("ItemController: выполнено updateItem - {}.", itemDto);
        Item item = ItemMapper.toItem(itemDto);
        Long requestId = itemDto.getRequest();
        return ItemMapper.toItemDto(itemService.updateItem(userId, itemId, item, requestId));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.debug("ItemController: выполнено deleteItem - {}.", itemId);
        itemService.deleteItemById(userId, itemId);
    }

}
