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

    public Optional<PlayerUser> findUser() {
        if (isUserListEmpty()) {
            return Optional.empty();
        }
        return Optional.of(UserSelector.getRandomUser(activePlayers));
    }

    public boolean isUserListEmpty() {
        return activePlayers.isEmpty();
    }

}

class UserSelector {

    public static PlayerUser getRandomUser(List<PlayerUser> playerUserList) {
        return playerUserList.get(new Random().nextInt(playerUserList.size()));
    }

}
