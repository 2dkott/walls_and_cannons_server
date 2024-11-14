package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.controllers.UserRequestEntity;
import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.Castle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.domain.Wall;
import com.testgame.wall_and_cannons_server.persistance.CastleRepository;
import com.testgame.wall_and_cannons_server.persistance.UserRepository;
import com.testgame.wall_and_cannons_server.persistance.WallRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GameService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CastleRepository castleRepository;

    @Autowired
    WallRepository wallRepository;

    @Autowired
    CellService cellService;

    @Autowired
    BattleService battleService;

    @Autowired
    PlayerPartyService playerPartyService;


    public UserRequestEntity createNewPlayer(UserRequestEntity newUser) {
        log.info("Creating new {} from request", newUser);
        PlayerUser user = userRepository.save(newUser.mapToPlayerUser());
        log.info("{} was saved in DataBase", user);

        Castle castle = new Castle();
        castle.setPlayerUser(user);
        castleRepository.save(castle);
        log.info("{} was saved in DataBase", castle);

        Wall wall = new Wall();
        wall.setWallColumns(5);
        wall.setWallRows(5);
        wall.setCastle(castle);
        wallRepository.save(wall);
        log.info("{} was saved in DataBase", wall);
        cellService.buildCells(wall);

        return new UserRequestEntity(user);
    }

    public Battle createBattle(Battle battle) {
        Battle finalBattle = battleService.saveBattle(battle);
        battle.getPlayerParties().forEach(playerParty -> {
            playerParty.setBattle(finalBattle);
            playerPartyService.save(playerParty);
        });
        return battle;
    }
}
