package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.BattleRoundCell;
import com.testgame.wall_and_cannons_server.domain.Castle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.persistance.BattleCellRepository;
import com.testgame.wall_and_cannons_server.persistance.BattleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleService {

    @Autowired
    private BattleRepository battleRepository;

    @Autowired
    private BattleCellRepository battleCellRepostitory;

    @Autowired
    private CastleService castleService;

    public Battle createBattle(PlayerUser playerUserA, PlayerUser playerUserB) {
        Battle battle = new Battle();


        battle.setPlayerUserA(castleA.getPlayerUser());
        battle.setPlayerUserB(castleB.getPlayerUser());
        battleRepository.save(battle);

        List<BattleRoundCell> battleRoundCells = castleA.getWall().getCellList().stream()
                .map(cell -> new BattleRoundCell(battle, cell)).toList();
        battleCellRepostitory.saveAll(battleRoundCells);



        return battle;
    }
}
