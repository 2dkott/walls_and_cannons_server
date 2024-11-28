package com.testgame.wall_and_cannons_server.domain;

import com.testgame.wall_and_cannons_server.exceptions.NoRoundsForBattleException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BattleProcessor {

  private final PlayerUser playerUser;
  private final Battle battle;
  private final Supplier<List<BattleRound>> allBattleRoundsSupplier;
  private final Consumer<BattleRound> battleRoundSaver;
  private final Consumer<PlayerParty> playerPartySaver;

  public BattleProcessor(
      @NonNull PlayerUser playerUser,
      @NonNull Battle battle,
      Supplier<List<BattleRound>> allBattleRoundsSupplier,
      Consumer<BattleRound> battleRoundSaver,
      Consumer<PlayerParty> playerPartySaver) {
    this.playerUser = playerUser;
    this.battle = battle;
    this.allBattleRoundsSupplier = allBattleRoundsSupplier;
    this.battleRoundSaver = battleRoundSaver;
    this.playerPartySaver = playerPartySaver;
  }

  public Optional<BattleRound> returnCurrentRound(int roundNumber) {

    log.info("Check if requested round is still currently active");
    BattleRound latestBattleRound = getLatestRound();

    if (latestBattleRound.getRoundNumber() == roundNumber && latestBattleRound.isActive()) {
      log.info("The latest round number and requested round number are same: {}", roundNumber);
      if (isStillInFineTimeout(latestBattleRound)) {
        log.info("The latest round is requested round {}", latestBattleRound);
        return Optional.of(latestBattleRound);
      }
    }
    return Optional.empty();
  }

  public Optional<BattleRound> confirmAndGetCurrentRound(int roundNumber) {

    BattleRound latestBattleRound = getLatestRound();

    if (latestBattleRound.getRoundNumber() == roundNumber && !latestBattleRound.isActive()) {
      log.info("Checking that Player Parties confirmed battle round {}", latestBattleRound);
      return confirmAndGet(latestBattleRound);
    }
    return Optional.empty();
  }

  public Optional<BattleRound> chaseLatestAndConfirmAndGet(int roundNumber) {
    log.info("Check if requested round passed");
    BattleRound latestBattleRound = getLatestRound();
    if ((latestBattleRound.getRoundNumber() - roundNumber) == 1) {
      log.info("Requested round was passed");
      if (isStillInFineTimeout(latestBattleRound)) {
        return confirmAndGet(latestBattleRound);
      }
    }
    return Optional.empty();
  }

  public Optional<BattleRound> confirmAndGet(BattleRound battleRound) {
    Optional<PlayerParty> playerParty =
        getNotConfirmedPartiesFromRound(battleRound).stream()
            .filter(party -> party.getPlayerUser().equals(playerUser))
            .findFirst();

    if (playerParty.isPresent()) {
      log.info("Requested player is in list of parties did not confirmed round");
      playerParty.get().setConfirmed(true);
      playerPartySaver.accept(playerParty.get());
    }

    if (getNotConfirmedPartiesFromRound(battleRound).isEmpty()) {
      log.info("List is empty, saving latest round as active and return it");
      battleRound.setActive(true);
      battleRoundSaver.accept(battleRound);
      return Optional.of(battleRound);
    }
    return Optional.empty();
  }

  public boolean isStillInFineTimeout(BattleRound currentBattleRound) {
    log.info("Check if requested round still in allowed timeout");
    Duration timePassAfterRoundStarts =
        Duration.between(currentBattleRound.getRoundStartTime(), LocalDateTime.now());
    log.info(
        "Duration between latest round start and requested round call time {}",
        timePassAfterRoundStarts.getSeconds());
    log.info("Allowed round time out {}", currentBattleRound.getRoundDuration());
    long delta = currentBattleRound.getRoundDuration() - timePassAfterRoundStarts.getSeconds();
    if (delta >= 0) {
      log.info("Requested round is still in allowed timeout");
      return true;
    }
    log.info("Requested round is not in allowed timeout");
    return false;
  }

  public List<PlayerParty> getNotConfirmedPartiesFromRound(BattleRound round) {
    log.info("Searching parties which are not confirmed round {} yet", round);
    List<PlayerParty> parties = round.getPlayerParties().stream()
        .filter(playerParty -> !playerParty.isConfirmed())
        .toList();

    log.info("Found {} parties", parties);
    return parties;
  }

  public BattleRound getLatestRound() {
    log.info("Getting latest round of battle {}", battle);
    BattleRound latestBattleRound = getRoundsByBattle().stream()
        .max(Comparator.comparing(BattleRound::getRoundNumber))
        .orElseThrow(() -> new NoRoundsForBattleException(battle));
    log.info("Latest battle round {}", latestBattleRound);
    return latestBattleRound;
  }

  public List<BattleRound> getRoundsByBattle() {
    log.info("Retrieving all rounds of battle {}", battle);
    List<BattleRound> battleRoundList = allBattleRoundsSupplier.get().stream()
        .filter(battleRound -> battleRound.getBattle().equals(battle))
        .toList();
    log.info("Thera are rounds {} of {}", battleRoundList, battle);
    return battleRoundList;
  }
}
