package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @GetMapping("/all")
    public List<ItemRequestDto> findAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        log.info("ItemRequestServerController: findAll implementation. User ID {}.", userId);
        return itemRequestService.findAll(userId, from, size);
    }

    @GetMapping
    public List<ItemRequestDto> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemRequestServerController: findAllByUserId implementation. User ID {}.", userId);
        return itemRequestService.findAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId) {
        log.info("ItemRequestServerController: findById implementation. User ID {}, request ID {}.", userId, requestId);
        return itemRequestService.findById(userId, requestId);
    }

    @PostMapping
    public ItemRequestDto save(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody ItemRequestDto itemRequestDto) {
        log.info("ItemRequestServerController: save implementation. User ID {}.", userId);
        return itemRequestService.save(userId, itemRequestDto);
    }
}
