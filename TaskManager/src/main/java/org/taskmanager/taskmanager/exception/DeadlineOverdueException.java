package org.taskmanager.taskmanager.exception;

public class DeadlineOverdueException extends RuntimeException {
    public DeadlineOverdueException(String message) {
        super(message);
    }
}