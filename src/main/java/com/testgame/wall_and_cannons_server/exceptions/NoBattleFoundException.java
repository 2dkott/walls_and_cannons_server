package com.testgame.wall_and_cannons_server.exceptions;

public class NoBattleFoundException extends RuntimeException{
    public NoBattleFoundException(long battleId){
        super("No battle found with id " + battleId);
    }
}
