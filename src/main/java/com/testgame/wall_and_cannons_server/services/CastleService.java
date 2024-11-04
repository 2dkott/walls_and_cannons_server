package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.domain.Castle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.persistance.CastleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CastleService {

    @Autowired
    private CastleRepository castleRepository;

    public List<Castle> getAllCastlesByUserId(PlayerUser playerUser) {
        return castleRepository.findByPlayerUser(playerUser);
    }

    public Optional<Castle> getCurrentCastleByUser(PlayerUser playerUser) {
        return castleRepository.findCastleByPlayerUserAndAndCurrentIsTrue(playerUser);
    }
}
