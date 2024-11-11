package com.testgame.wall_and_cannons_server.exceptions;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;

public class NoPlayerInBattleException extends RuntimeException{
    public NoPlayerInBattleException(Battle battle, PlayerUser playerUser){
        super(String.format("%s is not in %s", playerUser, battle));
    }
}
