package com.testgame.wall_and_cannons_server.controllers;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.exceptions.NoUserFoundException;
import com.testgame.wall_and_cannons_server.services.ActiveUserProvider;
import com.testgame.wall_and_cannons_server.domain.BattleMatcher;
import com.testgame.wall_and_cannons_server.services.BattleService;
import com.testgame.wall_and_cannons_server.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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

    @PostConstruct
    public void init() {
        this.battleMatcher = new BattleMatcher(() -> activeUserProvider.getActivePlayers(), () -> battleService.findAllBattles());
    }

    @GetMapping("/find/{userId}")
    public void findBattle(@PathVariable long userId) {
        PlayerUser playerUser = userService.getUserById(userId).orElseThrow(() -> new NoUserFoundException(userId));

        Optional<Battle> battle = battleMatcher.match(playerUser);



        //if(battleService.getUnConfirmedBattlesByPlayerUser(playerUser).isEmpty());

    }

}
