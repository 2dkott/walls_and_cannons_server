package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.exceptions.ActiveUserListEmptyException;
import com.testgame.wall_and_cannons_server.domain.PlayerParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class BattleMatcher {

    ActiveUserProvider activeUserProvider;
    BattleService battleService;

    @Autowired
    public BattleMatcher(BattleService battleService, ActiveUserProvider activeUserProvider) {
        this.battleService = battleService;
        this.activeUserProvider = activeUserProvider;
    }

    public Optional<Battle> match(PlayerUser playerUser) throws ActiveUserListEmptyException {

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
        List<PlayerUser> noBattlePlayers = activeUserProvider.getActivePlayers().stream()
                .filter(player -> !player.equals(currentPlayer))
                .filter(playerUser -> battleService.getActiveBattlesByPlayer(playerUser).isEmpty())
                .toList();

        if (noBattlePlayers.isEmpty()) return Optional.empty();
        return Optional.ofNullable(noBattlePlayers.get(new Random().nextInt(noBattlePlayers.size())));
    }
}