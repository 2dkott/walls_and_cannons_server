package com.testgame.wall_and_cannons_server.services;

public class PlayerHasNoCurrentCastle extends RuntimeException {
    public PlayerHasNoCurrentCastle(Long playerUserId) {
        super("Player " + playerUserId + " has no current castle");
    }
}
