package com.testgame.wall_and_cannons_server.exceptions;

public class RoundNotEndedException extends RuntimeException {
    public RoundNotEndedException(int roundName) {
        super("Round " + roundName + " has not ended");
    }
}
