package com.testgame.wall_and_cannons_server.controllers;

import com.testgame.wall_and_cannons_server.domain.Castle;
import com.testgame.wall_and_cannons_server.services.CastleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/castle")
public class CastleController {

    @Autowired
    private CastleService castleService;

    @GetMapping("/{user_id}")
    public List<CastleRequestEntity> getAllCastles(@PathVariable("user_id") Long userId) {
        return castleService.getAllCastlesByUserId(userId).stream().map(CastleRequestEntity::new).toList();
    }
}

