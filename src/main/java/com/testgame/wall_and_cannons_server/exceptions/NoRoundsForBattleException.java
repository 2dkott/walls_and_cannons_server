package com.testgame.wall_and_cannons_server.exceptions;

import com.testgame.wall_and_cannons_server.domain.Battle;

public class NoRoundsForBattleException extends RuntimeException{
    public NoRoundsForBattleException(Battle battle){
        super("No rounds for a " + battle);
    }
}
