package com.testgame.wall_and_cannons_server.controllers.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConfirmationData {

    @JsonProperty("type")
    private ConfirmationType confirmationType;

    @JsonProperty("object_id")
    private Long id;

    @JsonProperty("confirmed")
    boolean confirmed = false;

    @JsonProperty("battle_round")
    BattleRoundData battleRoundData;

}
