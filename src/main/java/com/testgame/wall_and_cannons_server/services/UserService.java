package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;



}
