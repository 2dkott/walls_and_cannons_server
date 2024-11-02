package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.controllers.UserRequestEntity;
import com.testgame.wall_and_cannons_server.domain.Castle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.domain.Wall;
import com.testgame.wall_and_cannons_server.persistance.CastleRepository;
import com.testgame.wall_and_cannons_server.persistance.UserRepository;
import com.testgame.wall_and_cannons_server.persistance.WallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


    public UserRequestEntity createNewPlayer(UserRequestEntity newUser) {
        PlayerUser user = userRepository.save(newUser.mapToPlayerUser());

        Wall wall = new Wall();
        wall.setWallColumns(5);
        wall.setWallRows(5);
        wallRepository.save(wall);
        cellService.buildCells(wall);

        Castle castle = new Castle();
        castle.setPlayerUser(user);
        castle.setWall(wall);
        castleRepository.save(castle);

        return new UserRequestEntity(user);
    }
}
