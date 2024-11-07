package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class ActiveUserProvider {

    List<PlayerUser> activePlayers = new ArrayList<>();

    public List<PlayerUser> getActivePlayers() {
        return activePlayers;
    }

    public boolean isUserListEmpty() {
        return activePlayers.isEmpty();
    }

    public void addUser(PlayerUser user) {
        activePlayers.add(user);
    }

}


