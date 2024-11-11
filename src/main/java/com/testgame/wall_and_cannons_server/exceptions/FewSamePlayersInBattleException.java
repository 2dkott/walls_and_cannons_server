package com.testgame.wall_and_cannons_server.exceptions;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;

public class FewSamePlayersInBattleException extends RuntimeException{
    public FewSamePlayersInBattleException(Battle battle, PlayerUser playerUser){
        super(String.format("There are a few parties of %s in %s", playerUser, battle));
    }
}
