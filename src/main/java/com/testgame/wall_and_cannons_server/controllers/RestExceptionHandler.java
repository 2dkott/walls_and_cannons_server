package com.testgame.wall_and_cannons_server.controllers;

import com.testgame.wall_and_cannons_server.services.PlayerHasNoCurrentCastle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({PlayerHasNoCurrentCastle.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        ApiError apiError = new ApiError("Entity Not Found Exception", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
