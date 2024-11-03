package com.testgame.wall_and_cannons_server.persistance;

import com.testgame.wall_and_cannons_server.domain.BattleRoundCell;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleCellRepository extends JpaRepository<BattleRoundCell, Long> {


}
