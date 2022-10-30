package ru.practicum.shareit.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String clsName, Long id) {
        super(String.format("%s с ID = %d не существует", clsName, id));
    }
}
