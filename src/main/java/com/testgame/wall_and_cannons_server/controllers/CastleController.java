package com.testgame.wall_and_cannons_server.controllers;

import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.exceptions.NoUserFoundException;
import com.testgame.wall_and_cannons_server.services.CastleService;
import com.testgame.wall_and_cannons_server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/castle")
public class CastleController {

    @Autowired
    private CastleService castleService;

    @Autowired
    UserService userService;

    @GetMapping("/{user_id}")
    public List<CastleRequestEntity> getAllCastles(@PathVariable("user_id") Long userId) {
        PlayerUser playerUser = userService.getUserById(userId).orElseThrow(() -> new NoUserFoundException(userId));
        return castleService.getAllCastlesByUser(playerUser).stream().map(CastleRequestEntity::new).toList();
    }
}

