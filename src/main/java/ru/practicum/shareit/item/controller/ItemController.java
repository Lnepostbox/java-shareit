package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;

    @GetMapping
    public List<ItemDtoResponse> findAllByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemController: findAllByOwnerId implementation. User ID {}.", userId);
        return itemService.findAllByOwnerId(userId);
    }

    @GetMapping(value = "/search")
    public List<ItemDtoResponse> findAllByText(@RequestParam(name = "text") String text) {
        if (text.isBlank()) {
            return List.of();
        }
        log.info("ItemController: findAllByText implementation. Text: {}.", text);
        return itemService.findAllByText(text);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDtoResponse findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info("ItemController: findById implementation. User ID {}, item ID {}.", userId, itemId);
        return itemService.findById(userId, itemId);
    }

    @PostMapping
    public ItemDtoResponse save(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated(Create.class) @RequestBody ItemDtoRequest itemDtoRequest) {
        log.info("ItemController: save implementation. User ID {}.", userId);
        return itemService.save(userId, itemDtoRequest);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto saveComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody CommentDto commentDto) {
        log.info("ItemController: saveComment implementation. User ID {}, itemId {}.", userId, itemId);
        return commentService.save(userId, itemId, commentDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDtoResponse update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @Validated(Update.class) @RequestBody ItemDtoRequest itemDtoRequest) {
        log.info("ItemController: update implementation. User ID {}, itemId {}.", userId, itemId);
        return itemService.update(userId, itemId, itemDtoRequest);
    }

    @DeleteMapping(value = "/{itemId}")
    public void delete(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info("ItemController: delete implementation. User ID {}, itemId {}.", userId, itemId);
        itemService.delete(userId, itemId);
    }
}