package com.testgame.wall_and_cannons_server.exceptions;

public class NoUserFoundException extends RuntimeException{
    public NoUserFoundException(long userId){
        super("No user found with id " + userId);
    }
}
