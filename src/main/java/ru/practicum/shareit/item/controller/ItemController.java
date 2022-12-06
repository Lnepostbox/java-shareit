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
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;

    @GetMapping
    public List<ItemDtoResponse> findAllByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Parameter from must not be negative") int from,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Parameter size must be positive") int size) {
        log.info("ItemController: findAllByOwnerId implementation. User ID {}.", ownerId);
        return itemService.findAllByOwnerId(ownerId, from, size);
    }

    @GetMapping(value = "/search")
    public List<ItemDtoResponse> findAllByText(
            @RequestParam(name = "text") String text,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Parameter from must not be negative") int from,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Parameter size must be positive") int size) {
        if (text.isBlank()) {
            return List.of();
        }
        log.info("ItemController: findAllByText implementation. Text: {}.", text);
        return itemService.findAllByText(text, from, size);
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