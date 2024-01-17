package org.taskmanager.taskmanager.exception;

public class InvalidDeadlineException extends RuntimeException {
    public InvalidDeadlineException(String message) {
        super(message);
    }
}