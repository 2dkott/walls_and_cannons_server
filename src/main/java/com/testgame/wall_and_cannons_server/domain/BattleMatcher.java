package com.testgame.wall_and_cannons_server.domain;

import com.testgame.wall_and_cannons_server.exceptions.FewSamePlayersInBattleException;
import com.testgame.wall_and_cannons_server.exceptions.NoPlayerInBattleException;
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

        Optional<Battle> battleToJoin = checkMatch(playerUser);

        if (battleToJoin.isPresent()) {
            return Optional.of(new MatchingResult(playerUser, BattleHandler.getPlayersExcluding(playerUser, battleToJoin.get()), battleToJoin.get()));
        }

        Optional<PlayerUser> enemyPlayer = BattleHandler.findRandomPlayerWithoutBattleExcluding(playerUserProvider.get(),
                battleListProvider.get(), playerUser);

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

            return Optional.of(new MatchingResult(playerUser, BattleHandler.getPlayersExcluding(playerUser, battle), battle));
        }
        log.info("There is no available active players fo initial {}}", playerUser);
        return Optional.empty();
    }

    public Optional<Battle> checkMatch(PlayerUser playerUser) {
        return BattleHandler.getActiveBattlesByPlayer(battleListProvider.get(), playerUser).stream()
                .filter(battle -> isGoodToJoin(battle, playerUser)).findFirst();
    }

    public boolean isGoodToJoin(Battle battle, PlayerUser playerUser) {
        List<PlayerParty> parties = battle.getPlayerParties().stream().filter(party -> party.getPlayerUser().equals(playerUser)).toList();

        if (parties.isEmpty()) throw new NoPlayerInBattleException(battle, playerUser);
        if (parties.size() > 1) throw new FewSamePlayersInBattleException(battle, playerUser);

        return !parties.get(0).isConfirmed();
    }


}