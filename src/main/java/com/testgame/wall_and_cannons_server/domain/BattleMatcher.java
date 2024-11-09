package com.testgame.wall_and_cannons_server.domain;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;


public class BattleMatcher {

    private final Supplier<List<PlayerUser>> playerUserProvider;
    private final Supplier<List<Battle>> battleListProvider;

    public BattleMatcher(Supplier<List<PlayerUser>> playerUserProvider, Supplier<List<Battle>> battleListProvider) {
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

            battle.setPlayerParties(List.of(playerParty, enemyPlayerParty));

            return Optional.of(battle);
        }
        return Optional.empty();
    }

    public List<Battle> getBattlesByPlayer(List<Battle> battleList, PlayerUser playerUser) {
        return battleList.stream().filter(battle -> battle.isByPlayer(playerUser)).toList();
    }

    public List<Battle> getActiveBattlesByPlayer(List<Battle> battleList, PlayerUser playerUser) {
        return getBattlesByPlayer(battleList, playerUser).stream().filter(battle -> !battle.isFinished()).toList();
    }

    public Optional<PlayerUser> findRandomPlayerWithoutBattleExcluding(PlayerUser currentPlayer) {
        List<PlayerUser> noBattlePlayers = playerUserProvider.get().stream()
                .filter(player -> !player.equals(currentPlayer))
                .filter(player -> getActiveBattlesByPlayer(battleListProvider.get(), player).isEmpty())
                .toList();

        if (noBattlePlayers.isEmpty()) return Optional.empty();
        return Optional.ofNullable(noBattlePlayers.get(new Random().nextInt(noBattlePlayers.size())));
    }
}