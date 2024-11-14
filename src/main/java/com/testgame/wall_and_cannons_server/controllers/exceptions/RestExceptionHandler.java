package com.testgame.wall_and_cannons_server.controllers.exceptions;

import com.testgame.wall_and_cannons_server.exceptions.NoBattleCreatedException;
import com.testgame.wall_and_cannons_server.exceptions.NoBattleFoundException;
import com.testgame.wall_and_cannons_server.exceptions.NoMatchingResultException;
import com.testgame.wall_and_cannons_server.exceptions.NoRoundsForBattleException;
import com.testgame.wall_and_cannons_server.exceptions.NoUserFoundException;
import com.testgame.wall_and_cannons_server.services.PlayerHasNoCurrentCastle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({PlayerHasNoCurrentCastle.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        ApiError apiError = new ApiError("Entity Not Found Exception", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NoUserFoundException.class})
    public ResponseEntity<Object> handleNoUserFoundException(Exception ex) {
        return notFoundException(ex);
    }

    @ExceptionHandler({NoMatchingResultException.class})
    public ResponseEntity<Object> handleNoMatchingResultException(Exception ex) {
        return notFoundException(ex);
    }

    @ExceptionHandler({NoBattleFoundException.class})
    public ResponseEntity<Object> handleNoBattleFoundException(Exception ex) {
        return notFoundException(ex);
    }

    @ExceptionHandler({NoRoundsForBattleException.class})
    public ResponseEntity<Object> handleNoRoundsForBattleException(Exception ex) {
        return notFoundException(ex);
    }

    @ExceptionHandler({NoBattleCreatedException.class})
    public ResponseEntity<Object> handleNoBattleCreatedException(Exception ex) {
        return notCreatedException(ex);
    }

    private ResponseEntity<Object> notFoundException(Exception ex) {
        return badRequestException(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> notCreatedException(Exception ex) {
        return badRequestException(ex, HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<Object> badRequestException(Exception ex, HttpStatus status) {
        ApiError apiError = new ApiError(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(apiError, status);
    }
}
