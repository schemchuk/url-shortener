package de.telran.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNameAlreadyTakenException.class)
    public ResponseEntity<String> handleUserNameAlreadyTakenException(UserNameAlreadyTakenException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleException.class)
    public ResponseEntity<String> handleRoleException(RoleException ex) {
        if (ex instanceof RoleException.RoleAlreadyExistsException) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } else if (ex instanceof RoleException.RoleNotFoundException) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



