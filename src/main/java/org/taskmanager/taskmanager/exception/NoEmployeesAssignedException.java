package org.taskmanager.taskmanager.exception;

public class NoEmployeesAssignedException extends RuntimeException {
    public NoEmployeesAssignedException(String message) {
        super(message);
    }
}