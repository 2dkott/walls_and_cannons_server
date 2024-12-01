package com.testgame.wall_and_cannons_server.exceptions;

import com.testgame.wall_and_cannons_server.domain.BattleRound;

public class BattleRoundDurationException extends RuntimeException {

    public BattleRoundDurationException(BattleRound battleRound, String rootExceptionMessage){
        super(String.format("Something wrong with duration of %s:\n%s ", battleRound, rootExceptionMessage));
    }
}
