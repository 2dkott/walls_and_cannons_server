package com.testgame.wall_and_cannons_server.domain;

import lombok.Getter;

@Getter
public class MatchingResult {

    private final PlayerUser initPlayer;
    private final PlayerUser matchedPlayer;
    private final Battle battle;

    public MatchingResult(PlayerUser initPlayer, PlayerUser matchedPlayer, Battle battle) {
        this.initPlayer = initPlayer;
        this.matchedPlayer = matchedPlayer;
        this.battle = battle;
    }
}
