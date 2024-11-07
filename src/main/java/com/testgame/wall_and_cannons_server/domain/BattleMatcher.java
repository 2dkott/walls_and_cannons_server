package com.testgame.wall_and_cannons_server.domain;

import com.testgame.wall_and_cannons_server.services.ActiveUserProvider;
import com.testgame.wall_and_cannons_server.services.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;


public class BattleMatcher {

    private final Supplier<List<PlayerUser>> playerUserProvider;
    private final Function<PlayerUser, List<Battle>> battleListProvider;

    public BattleMatcher(Supplier<List<PlayerUser>> playerUserProvider, Function<PlayerUser, List<Battle>> battleListProvider) {
        this.battleListProvider = battleListProvider;
        this.playerUserProvider = playerUserProvider;
    }

    public Optional<Battle> match(PlayerUser playerUser) {

        Optional<PlayerUser> enemyPlayer = findRandomPlayerWithoutBattleExcluding(playerUser);

        if (enemyPlayer.isPresent()) {
            Battle battle = new Battle();

            PlayerParty playerParty = new PlayerParty();
            playerParty.setPlayerUser(playerUser);
            playerParty.setConfirmed(true);

            PlayerParty enemyPlayerParty = new PlayerParty();
            enemyPlayerParty.setPlayerUser(enemyPlayer.get());
            enemyPlayerParty.setConfirmed(false);

            battle.getPlayerParties().add(playerParty);
            battle.getPlayerParties().add(enemyPlayerParty);

            return Optional.of(battle);
        }
        return Optional.empty();
    }



    public Optional<PlayerUser> findRandomPlayerWithoutBattleExcluding(PlayerUser currentPlayer) {
        List<PlayerUser> noBattlePlayers = playerUserProvider.get().stream()
                .filter(player -> !player.equals(currentPlayer))
                .filter(playerUser -> battleListProvider.apply(playerUser).isEmpty())
                .toList();

        if (noBattlePlayers.isEmpty()) return Optional.empty();
        return Optional.ofNullable(noBattlePlayers.get(new Random().nextInt(noBattlePlayers.size())));
    }
}