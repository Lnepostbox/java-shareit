package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService requestService;

    @GetMapping
    public List<ItemDto> findAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("ItemController: выполнено findAllItems - {}.", userId);
        return itemService.findAllItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.debug("ItemController: выполнено findItemById - {}.", itemId);
        return ItemMapper.toItemDto(itemService.findItemById(userId, itemId));
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByParams(@RequestParam String text) {
        log.debug("ItemController: выполнено findItemByParams - {}.", text);
        return itemService.findItemsByText(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.debug("ItemController: выполнено createItem - {}.", itemDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setRequest(itemDto.getRequest() != null ? requestService.findItemRequestById(itemDto.getRequest()) : null);
        item.setOwner(userService.findUserById(userId));
        return ItemMapper.toItemDto(itemService.createItem(userId, item));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto
    ) {
        log.debug("ItemController: выполнено updateItem - {}.", itemDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setRequest(itemDto.getRequest() != null ? requestService.findItemRequestById(itemDto.getRequest()) : null);
        item.setOwner(userService.findUserById(userId));
        return ItemMapper.toItemDto(itemService.updateItem(userId, itemId, item));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.debug("ItemController: выполнено deleteItem - {}.", itemId);
        itemService.deleteItemById(userId, itemId);
    }

}
