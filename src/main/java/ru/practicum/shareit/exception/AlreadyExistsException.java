package ru.practicum.shareit.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(String clsName, Long id) {
        super(String.format("%s с ID = %d не существует", clsName, id));
    }

    public AlreadyExistsException(String clsName, String textIdentifier) {
        super(String.format("%s с ID = %s не существует", clsName, textIdentifier));
    }
}
