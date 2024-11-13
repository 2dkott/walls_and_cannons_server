package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.BattleRound;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.persistance.BattleRepository;
import com.testgame.wall_and_cannons_server.persistance.BattleRoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BattleService {

    @Autowired
    private BattleRepository battleRepository;

    @Autowired
    private BattleRoundRepository battleRoundRepository;

    public List<Battle> getBattlesByPlayerUser(PlayerUser playerUser, boolean isConfirmed, boolean isFinished) {
        return getBattlesByPlayerUser(playerUser).stream()
                .filter(battle -> battle.isFinished == isFinished)
                .filter(battle -> battle.getPartyByPlayer(playerUser).get().isConfirmed() == isConfirmed)
                .toList();
    }

    public List<Battle> getActiveBattlesByPlayer(PlayerUser playerUser) {
        return getBattlesByPlayerUser(playerUser).stream()
                .filter(battle -> !battle.isFinished)
                .toList();
    }

    public List<Battle> getBattlesByPlayerUser(PlayerUser playerUser) {
        return battleRepository.findAll().stream()
                .filter(battle -> battle.isByPlayer(playerUser))
                .toList();
    }

    public List<Battle> findAllBattles() {
        return battleRepository.findAll();
    }

    public Optional<Battle> findBattleById(Long battleId) {
        return battleRepository.findById(battleId);
    }

    public Battle saveBattle(Battle battle) {
        return battleRepository.save(battle);
    }

    public Optional<BattleRound> findBattleRoundByBattleAndRoundNumber(Battle battle, int roundNumber) {
        return battleRoundRepository.findBattleRoundByBattleAndRoundNumber(battle, roundNumber);
    }

    public List<BattleRound> findBattleRoundsByBattle(Battle battle, int roundNumber) {
        return battleRoundRepository.findAllByBattle(battle);
    }
}
