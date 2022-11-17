package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;

public interface CommentService {

    CommentDto save(CommentDto commentDto, Long itemId, Long userId);
}
