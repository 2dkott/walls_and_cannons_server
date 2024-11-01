package com.testgame.wall_and_cannons_server.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import lombok.Data;

@Data
public class UserRequestBody {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("game_name")
    private String gameName;

    @JsonProperty("login_name")
    private String loginName;

    public UserRequestBody() {}

    public UserRequestBody(PlayerUser user) {
        this.id = user.getId();
        this.gameName = user.getGameName();
        this.loginName = user.getLoginName();
    }

    public PlayerUser mapToPlayerUser() {
        PlayerUser playerUser = new PlayerUser();
        playerUser.setId(this.id);
        playerUser.setLoginName(this.loginName);
        playerUser.setGameName(this.gameName);
        return playerUser;
    }
}
