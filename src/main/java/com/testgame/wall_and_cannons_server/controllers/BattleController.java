package com.testgame.wall_and_cannons_server.controllers;

import com.testgame.wall_and_cannons_server.controllers.data.BattleData;
import com.testgame.wall_and_cannons_server.controllers.data.BattleRoundData;
import com.testgame.wall_and_cannons_server.controllers.data.ConfirmationData;
import com.testgame.wall_and_cannons_server.controllers.data.ConfirmationType;
import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.BattleMatcher;
import com.testgame.wall_and_cannons_server.domain.BattleRound;
import com.testgame.wall_and_cannons_server.domain.MatchingResult;
import com.testgame.wall_and_cannons_server.domain.PlayerParty;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.exceptions.NoBattleFoundException;
import com.testgame.wall_and_cannons_server.exceptions.NoMatchingResultException;
import com.testgame.wall_and_cannons_server.exceptions.NoUserFoundException;
import com.testgame.wall_and_cannons_server.services.ActiveUserProvider;
import com.testgame.wall_and_cannons_server.services.BattleService;
import com.testgame.wall_and_cannons_server.services.GameService;
import com.testgame.wall_and_cannons_server.services.PlayerPartyService;
import com.testgame.wall_and_cannons_server.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
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

    @Autowired
    PlayerPartyService playerPartyService;

    private final HttpHeaders headers = new HttpHeaders();
    @Autowired
    private GameService gameService;

    @PostConstruct
    public void init() {
        this.battleMatcher = new BattleMatcher(() -> activeUserProvider.getActivePlayers(), () -> battleService.findAllBattles());
    }

    @GetMapping("/get-round")
    public void getRound(@RequestParam(name = "round") int roundNumber, @RequestParam(name = "playerId") long playerId, @RequestParam(name = "battleId") long battleId) {
        Battle battle = battleService.findBattleById(battleId).orElseThrow(() -> new NoBattleFoundException(battleId));
        List<BattleRound> battleRoundList = battleService.findBattleRoundsByBattle(battle);
        if (battleRoundList.isEmpty())
        Optional<BattleRound> battleRound = battleService.findBattleRoundByBattleAndRoundNumber(battle, roundNumber);
    }

    @GetMapping("/confirm/{battleId}")
    public ResponseEntity<ConfirmationData> getBattleConfirmation(@PathVariable long battleId) {
        Battle battle = battleService.findBattleById(battleId).orElseThrow(() -> new NoBattleFoundException(battleId));

        boolean isBattleConfirmed = battle.getPlayerParties().stream().anyMatch(playerParty -> !playerParty.isConfirmed());

        if (isBattleConfirmed) {
            BattleRoundData
        }
        ConfirmationData confirmationData = new ConfirmationData();
        confirmationData.setConfirmationType(ConfirmationType.BATTLE);
        confirmationData.setId(battleId);
        if () {
            confirmationData.setConfirmed(true);

        }


        return new ResponseEntity<>(confirmationData, headers, HttpStatus.OK);
    }

    @PostMapping("/match-user/{userId}")
    public ResponseEntity<BattleData> findBattle(@PathVariable long userId) {
        PlayerUser playerUser = userService.getUserById(userId).orElseThrow(() -> new NoUserFoundException(userId));
        log.info("{} was found", playerUser);

        activeUserProvider.putUser(playerUser);

        Optional<MatchingResult> matchingResult = battleMatcher.match(playerUser);

        Battle battle = matchingResult.orElseThrow(() -> new NoMatchingResultException(playerUser)).getBattle();

        if (Objects.isNull(battle.getId())) battle = gameService.createBattle(battle);
        else {
            Optional<PlayerParty> playerParty = battle.getPartyByPlayer(playerUser);
            playerParty.ifPresent(party -> {
                party.setConfirmed(true);
                playerPartyService.save(party);
            });
        }

        BattleData battleData = new BattleData(battle);
        return new ResponseEntity<>(battleData, headers, HttpStatus.FOUND);
    }

}
