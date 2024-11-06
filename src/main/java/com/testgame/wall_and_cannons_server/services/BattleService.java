package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.persistance.BattleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleService {

    @Autowired
    private BattleRepository battleRepository;

    public List<Battle> getBattlesByPlayerUser(PlayerUser playerUser, boolean isConfirmed, boolean isFinished) {
        return battleRepository.findAll().stream()
                .filter(battle -> battle.isFinished == isFinished)
                .filter(battle -> battle.isByPlayer(playerUser))
                .filter(battle -> battle.getPartyByPlayer(playerUser).get().isConfirmed() == isConfirmed)
                .toList();
    }
}
