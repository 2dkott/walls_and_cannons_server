package com.testgame.wall_and_cannons_server.exceptions;

import com.testgame.wall_and_cannons_server.domain.BattleRound;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;

public class NoPlayerInRoundException extends RuntimeException{
    public NoPlayerInRoundException(BattleRound battleRound, PlayerUser playerUser){
        super(String.format("%s is not in %s", playerUser, battleRound));
    }
}
