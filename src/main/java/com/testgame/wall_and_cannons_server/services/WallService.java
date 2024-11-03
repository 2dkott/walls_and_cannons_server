package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.domain.Wall;
import com.testgame.wall_and_cannons_server.persistance.WallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WallService {

    @Autowired
    private WallRepository wallRepository;

}
