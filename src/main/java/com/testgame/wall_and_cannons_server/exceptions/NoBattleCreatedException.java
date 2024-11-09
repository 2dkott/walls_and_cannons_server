package com.testgame.wall_and_cannons_server.exceptions;

import com.testgame.wall_and_cannons_server.domain.PlayerUser;

public class NoBattleCreatedException extends RuntimeException{
    public NoBattleCreatedException(PlayerUser playerA, PlayerUser playerB){
        super("No battle created for " + playerA + " and " + playerB);
    }
}
