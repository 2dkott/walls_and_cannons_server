package com.testgame.wall_and_cannons_server.controllers;

import com.testgame.wall_and_cannons_server.controllers.data.BattleData;
import com.testgame.wall_and_cannons_server.domain.MatchingResult;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.exceptions.NoMatchingResultException;
import com.testgame.wall_and_cannons_server.exceptions.NoUserFoundException;
import com.testgame.wall_and_cannons_server.services.ActiveUserProvider;
import com.testgame.wall_and_cannons_server.domain.BattleMatcher;
import com.testgame.wall_and_cannons_server.services.BattleService;
import com.testgame.wall_and_cannons_server.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/battle")
public class BattleController {


    BattleMatcher battleMatcher;

    @Autowired
    UserService userService;

    @Autowired
    ActiveUserProvider activeUserProvider;

    @Autowired
    BattleService battleService;

    private final HttpHeaders headers = new HttpHeaders();

    @PostConstruct
    public void init() {
        this.battleMatcher = new BattleMatcher(() -> activeUserProvider.getActivePlayers(), () -> battleService.findAllBattles());
    }

    @GetMapping("/match/{userId}")
    public ResponseEntity<BattleData> findBattle(@PathVariable long userId) {

        PlayerUser playerUser = userService.getUserById(userId).orElseThrow(() -> new NoUserFoundException(userId));
        log.info("{} was found", playerUser);

        Optional<MatchingResult> matchingResult = battleMatcher.match(playerUser);

        BattleData battleData = matchingResult.map(matching -> new BattleData(matching.getBattle())).orElseThrow(() -> new NoMatchingResultException(playerUser));
        return new ResponseEntity<>(battleData, headers, HttpStatus.FOUND);
    }

}
