package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ActiveUserProvider {

    List<PlayerUser> activePlayers = new ArrayList<>();

    public List<PlayerUser> getActivePlayers() {
        return activePlayers;
    }

    public boolean isUserListEmpty() {
        return activePlayers.isEmpty();
    }

    public void putUser(PlayerUser user) {
        log.info("Putting {} to active list", user);
        if (!activePlayers.contains(user)) {
            activePlayers.add(user);
        }
    }

    public boolean isUserInList(PlayerUser user) {
        log.info("Is {} in active list", user);
        return activePlayers.contains(user);
    }

}


