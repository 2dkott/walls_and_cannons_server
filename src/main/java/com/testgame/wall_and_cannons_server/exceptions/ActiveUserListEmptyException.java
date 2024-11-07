package com.testgame.wall_and_cannons_server.exceptions;

public class ActiveUserListEmptyException extends RuntimeException{

    public ActiveUserListEmptyException(){
        super("The active user list is empty");
    }
}
