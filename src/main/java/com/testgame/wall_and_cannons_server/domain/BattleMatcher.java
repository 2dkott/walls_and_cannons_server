package com.testgame.wall_and_cannons_server.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

@Slf4j
public class BattleMatcher {

    private final Supplier<List<PlayerUser>> playerUserProvider;
    private final Supplier<List<Battle>> battleListProvider;

    public BattleMatcher(Supplier<List<PlayerUser>> playerUserProvider, Supplier<List<Battle>> battleListProvider) {
        this.battleListProvider = battleListProvider;
        this.playerUserProvider = playerUserProvider;
    }

    public Optional<MatchingResult> match(PlayerUser playerUser) {
        log.info("Matching {} with another active player", playerUser);
        Optional<PlayerUser> enemyPlayer = findRandomPlayerWithoutBattleExcluding(playerUser);

        if (enemyPlayer.isPresent()) {
            log.info("{} player matched with initial {}}", enemyPlayer.get(), playerUser);

            Battle battle = new Battle();

            PlayerParty playerParty = new PlayerParty();
            playerParty.setPlayerUser(playerUser);
            playerParty.setConfirmed(true);

            PlayerParty enemyPlayerParty = new PlayerParty();
            enemyPlayerParty.setPlayerUser(enemyPlayer.get());
            enemyPlayerParty.setConfirmed(false);

            battle.setPlayerParties(List.of(playerParty, enemyPlayerParty));
            log.info("{} is created", battle);


            return Optional.of(new MatchingResult(playerUser, enemyPlayer.get(), battle));
        }
        log.info("There is no available active players fo initial {}}", playerUser);
        return Optional.empty();
    }

    public List<Battle> getBattlesByPlayer(List<Battle> battleList, PlayerUser playerUser) {
        log.info("Searching all battles for {}", playerUser);
        return battleList.stream().filter(battle -> battle.isByPlayer(playerUser)).toList();
    }

    public List<Battle> getActiveBattlesByPlayer(List<Battle> battleList, PlayerUser playerUser) {
        log.info("Searching only active battles for {}", playerUser);
        return getBattlesByPlayer(battleList, playerUser).stream().filter(battle -> !battle.isFinished()).toList();
    }

    public Optional<PlayerUser> findRandomPlayerWithoutBattleExcluding(PlayerUser currentPlayer) {
        log.info("Searching in active player list}");
        List<PlayerUser> noBattlePlayers = playerUserProvider.get().stream()
                .filter(player -> !player.equals(currentPlayer))
                .filter(player -> getActiveBattlesByPlayer(battleListProvider.get(), player).isEmpty())
                .toList();

        if (noBattlePlayers.isEmpty()) {
            log.info("There no unbusy active players}");
            return Optional.empty();
        }
        log.info("There are unbusy active players: {}", noBattlePlayers);

        Optional<PlayerUser> enemyPlayer = Optional.ofNullable(noBattlePlayers.get(new Random().nextInt(noBattlePlayers.size())));

        enemyPlayer.ifPresent(enemy -> log.info("Random {} was found", enemy));

        return enemyPlayer;
    }
}