package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;

    @GetMapping
    public List<ItemDtoResponse> findAllByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("ItemServerController: findAllByOwnerId implementation. User ID {}.", ownerId);
        return itemService.findAllByOwnerId(ownerId, from, size);
    }

    @GetMapping(value = "/search")
    public List<ItemDtoResponse> findAllByText(
            @RequestParam(name = "text") String text,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("ItemServerController: findAllByText implementation. Text: {}.", text);
        return itemService.findAllByText(text, from, size);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDtoResponse findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info("ItemServerController: findById implementation. User ID {}, item ID {}.", userId, itemId);
        return itemService.findById(userId, itemId);
    }

    @PostMapping
    public ItemDtoResponse save(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody ItemDtoRequest itemDtoRequest) {
        log.info("ItemServerController: save implementation. User ID {}.", userId);
        return itemService.save(userId, itemDtoRequest);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto saveComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody CommentDto commentDto) {
        log.info("ItemServerController: saveComment implementation. User ID {}, itemId {}.", userId, itemId);
        return commentService.save(userId, itemId, commentDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDtoResponse update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemDtoRequest itemDtoRequest) {
        log.info("ItemServerController: update implementation. User ID {}, itemId {}.", userId, itemId);
        return itemService.update(userId, itemId, itemDtoRequest);
    }

    @DeleteMapping(value = "/{itemId}")
    public void delete(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info("ItemServerController: delete implementation. User ID {}, itemId {}.", userId, itemId);
        itemService.delete(userId, itemId);
    }
}