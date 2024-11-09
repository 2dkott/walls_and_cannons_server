package com.testgame.wall_and_cannons_server.exceptions;

import com.testgame.wall_and_cannons_server.domain.PlayerUser;

public class NoMatchingResultException extends RuntimeException{
    public NoMatchingResultException(PlayerUser playerUser){
        super("There is no matching players for " + playerUser);
    }
}
