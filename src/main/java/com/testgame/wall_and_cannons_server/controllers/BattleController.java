package com.testgame.wall_and_cannons_server.controllers;

import com.testgame.wall_and_cannons_server.controllers.data.BattleData;
import com.testgame.wall_and_cannons_server.controllers.data.ConfirmationData;
import com.testgame.wall_and_cannons_server.controllers.data.ConfirmationType;
import com.testgame.wall_and_cannons_server.domain.*;
import com.testgame.wall_and_cannons_server.exceptions.*;
import com.testgame.wall_and_cannons_server.services.ActiveUserProvider;
import com.testgame.wall_and_cannons_server.services.BattleService;
import com.testgame.wall_and_cannons_server.services.GameService;
import com.testgame.wall_and_cannons_server.services.PlayerPartyService;
import com.testgame.wall_and_cannons_server.services.UserService;
import jakarta.annotation.PostConstruct;
import java.util.Objects;
import java.util.Optional;
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

@Slf4j
@RestController
@RequestMapping("/battle")
public class BattleController {

  BattleMatcher battleMatcher;

  @Autowired UserService userService;

  @Autowired ActiveUserProvider activeUserProvider;

  @Autowired BattleService battleService;

  @Autowired PlayerPartyService playerPartyService;

  @Autowired private GameService gameService;

  private final HttpHeaders headers = new HttpHeaders();

  @PostConstruct
  public void init() {
    this.battleMatcher =
        new BattleMatcher(
            () -> activeUserProvider.getActivePlayers(), () -> battleService.findAllBattles());
  }

  @GetMapping("/get-round")
  public void getRound(
      @RequestParam(name = "round") int roundNumber,
      @RequestParam(name = "playerId") long playerId,
      @RequestParam(name = "battleId") long battleId) {

    Battle battle =
        battleService
            .findBattleById(battleId)
            .orElseThrow(() -> new NoBattleFoundException(battleId));

    PlayerUser playerUser =
        userService.getUserById(playerId).orElseThrow(() -> new NoUserFoundException(playerId));

    BattleProcessor battleProcessor =
        new BattleProcessor(
            playerUser,
            battle,
            () -> battleService.findAllBattleRounds(),
            (battleRound) -> battleService.saveBattleRound(battleRound),
            (playerParty -> playerPartyService.save(playerParty)));

    BattleRound battleRoundToReturn =
        battleProcessor
            .returnCurrentRound(roundNumber)
            .or(() -> battleProcessor.confirmAndGetCurrentRound(roundNumber))
                .or(() -> battleProcessor.chaseLatestAndConfirmAndGet(roundNumber)).orElseThrow();
  }

  @GetMapping("/confirm/{battleId}")
  public ResponseEntity<ConfirmationData> getBattleConfirmation(@PathVariable long battleId) {
    Battle battle =
        battleService
            .findBattleById(battleId)
            .orElseThrow(() -> new NoBattleFoundException(battleId));

    boolean isBattleConfirmed =
        battle.getPlayerParties().stream().anyMatch(playerParty -> !playerParty.isConfirmed());
    if (isBattleConfirmed) {
      BattleRound battleRound = new BattleRound();
      battleRound.setBattle(battle);
      battleRound.setRoundNumber(0);
      battleRound.setActive(true);
      battleRound.setPlayerParties(
          battle.getPlayerParties().stream()
              .map(
                  playerParty -> {
                    PlayerParty party = new PlayerParty();
                    party.setConfirmed(false);
                    party.setBattleRound(battleRound);
                    party.setPlayerUser(playerParty.getPlayerUser());
                    return party;
                  })
              .toList());
    }

    ConfirmationData confirmationData = new ConfirmationData();
    confirmationData.setConfirmationType(ConfirmationType.BATTLE);
    confirmationData.setId(battleId);
    confirmationData.setConfirmed(isBattleConfirmed);

    return new ResponseEntity<>(confirmationData, headers, HttpStatus.OK);
  }

  @PostMapping("/match-user/{userId}")
  public ResponseEntity<BattleData> findBattle(@PathVariable long userId) {
    PlayerUser playerUser =
        userService.getUserById(userId).orElseThrow(() -> new NoUserFoundException(userId));
    log.info("{} was found", playerUser);

    activeUserProvider.putUser(playerUser);

    Optional<MatchingResult> matchingResult = battleMatcher.match(playerUser);

    Battle battle =
        matchingResult.orElseThrow(() -> new NoMatchingResultException(playerUser)).getBattle();

    if (Objects.isNull(battle.getId())) battle = gameService.createBattle(battle);
    else {
      Optional<PlayerParty> playerParty = battle.getPartyByPlayer(playerUser);
      playerParty.ifPresent(
          party -> {
            party.setConfirmed(true);
            playerPartyService.save(party);
          });
    }

    BattleData battleData = new BattleData(battle);
    return new ResponseEntity<>(battleData, headers, HttpStatus.FOUND);
  }
}
