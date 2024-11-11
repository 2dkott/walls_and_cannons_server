package com.testgame.wall_and_cannons_server.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class MatchingResult {

    private final PlayerUser initPlayer;
    private final List<PlayerUser> matchedPlayers;
    private final Battle battle;

    public MatchingResult(PlayerUser initPlayer, List<PlayerUser> matchedPlayer, Battle battle) {
        this.initPlayer = initPlayer;
        this.matchedPlayers = matchedPlayer;
        this.battle = battle;
    }
}
