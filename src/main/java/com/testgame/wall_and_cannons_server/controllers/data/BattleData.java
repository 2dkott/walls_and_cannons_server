package com.testgame.wall_and_cannons_server.controllers.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BattleData {

    public BattleData(Battle battle) {
        this.id = battle.getId();
        this.isFinished = battle.isFinished();
        this.winnerId = battle.getWinner().getId();
        this.partyIds = battle.getPlayerParties().stream().map(playerParty -> playerParty.getPlayerUser().getId()).toList();
    }

    @JsonProperty("battle_id")
    private Long id;

    @JsonProperty("battle_party_ids")
    private List<Long> partyIds = new ArrayList<>();

    @JsonProperty("is_finished")
    public boolean isFinished;

    @JsonProperty("winner_id")
    private Long winnerId;
}
