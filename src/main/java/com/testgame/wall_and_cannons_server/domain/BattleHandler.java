package com.testgame.wall_and_cannons_server.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
public class BattleHandler {

    public static List<Battle> getBattlesByPlayer(List<Battle> battleList, PlayerUser playerUser) {
        log.info("Searching all battles for {}", playerUser);
        return battleList.stream().filter(battle -> battle.isByPlayer(playerUser)).toList();
    }

    public static List<Battle> getActiveBattlesByPlayer(List<Battle> battleList, PlayerUser playerUser) {
        log.info("Searching only active battles for {}", playerUser);
        return getBattlesByPlayer(battleList, playerUser).stream().filter(battle -> !battle.isFinished()).toList();
    }

    public static Optional<PlayerUser> findRandomPlayerWithoutBattleExcluding(List<PlayerUser> playerUsers, List<Battle> battleList, PlayerUser currentPlayer) {
        log.info("Searching in active player list}");
        List<PlayerUser> noBattlePlayers = playerUsers.stream()
                .filter(player -> !player.equals(currentPlayer))
                .filter(player -> getActiveBattlesByPlayer(battleList, player).isEmpty()).toList();

        if (noBattlePlayers.isEmpty()) {
            log.info("There no unbusy active players}");
            return Optional.empty();
        }

        log.info("There are unbusy active players: {}", noBattlePlayers);

        Optional<PlayerUser> enemyPlayer = Optional.ofNullable(noBattlePlayers.get(new Random().nextInt(noBattlePlayers.size())));

        enemyPlayer.ifPresent(enemy -> log.info("Random {} was found", enemy));

        return enemyPlayer;
    }

    public static List<PlayerUser> getPlayersExcluding(PlayerUser playerUser, Battle battle) {
        return battle.getPlayerUsers().stream().filter(p -> !p.equals(playerUser)).toList();
    }
}
