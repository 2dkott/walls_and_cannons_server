package com.testgame.wall_and_cannons_server.persistance;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.BattleRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BattleRoundRepository extends JpaRepository<BattleRound, Long> {
    Optional<BattleRound> findBattleRoundByBattleAndRoundNumber(Battle battle, int roundNumber);

    List<BattleRound> findAllByBattle(Battle battle);
}
