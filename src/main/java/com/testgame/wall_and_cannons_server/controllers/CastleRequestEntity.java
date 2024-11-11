package com.testgame.wall_and_cannons_server.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.testgame.wall_and_cannons_server.domain.Castle;

public class CastleRequestEntity {

    public CastleRequestEntity(Castle castle) {
        this.id = castle.getId();
        this.userId = castle.getPlayerUser().getId();
    }

    @JsonProperty("id")
    public long id;

    @JsonProperty("user_id")
    public long userId;
}
