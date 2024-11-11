package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.domain.PlayerParty;
import com.testgame.wall_and_cannons_server.persistance.PlayerPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayerPartyService {

    @Autowired
    PlayerPartyRepository repository;

    public PlayerParty save(PlayerParty party) {
        return repository.save(party);
    }
}
