package com.testgame.wall_and_cannons_server.exceptions;

public class RoundNumberIncorrectException extends RuntimeException {
    public RoundNumberIncorrectException(int roundNumber) {
        super("Round number " + roundNumber + " is incorrect");
    }
}
