package com.testgame.wall_and_cannons_server.controllers.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BattleRoundData {
    @JsonProperty("round_number")
    private int round;

    @JsonProperty("round_id")
    private long round_id;
}
