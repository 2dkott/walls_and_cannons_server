package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.persistance.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<PlayerUser> getUserById(long userId) {
        log.info("Searching Player by userId: {}", userId);
        return userRepository.findById(userId);
    }
}
