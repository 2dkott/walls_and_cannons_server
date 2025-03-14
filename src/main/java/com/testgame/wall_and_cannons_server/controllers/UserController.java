package com.testgame.wall_and_cannons_server.controllers;

import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.persistance.UserRepository;
import com.testgame.wall_and_cannons_server.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserRepository userRepository;

    private final HttpHeaders headers = new HttpHeaders();

    public UserController() {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @PostMapping()
    public ResponseEntity<UserRequestEntity> addUser(@RequestBody UserRequestEntity userRequestEntity) {
        return new ResponseEntity<>(gameService.createNewPlayer(userRequestEntity), headers, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<UserRequestEntity>> getAllUser() {
        List<UserRequestEntity> users = userRepository.findAll().stream().map(user -> new UserRequestEntity(user)).toList();
        return new ResponseEntity<>(users, headers, HttpStatus.FOUND);
    }
}
