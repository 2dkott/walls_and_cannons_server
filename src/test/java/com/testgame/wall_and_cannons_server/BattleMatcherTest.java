package com.testgame.wall_and_cannons_server;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.PlayerParty;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.persistance.BattleRepository;
import com.testgame.wall_and_cannons_server.services.ActiveUserProvider;
import com.testgame.wall_and_cannons_server.domain.BattleMatcher;
import com.testgame.wall_and_cannons_server.services.BattleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class BattleMatcherTest {

    @Mock
    BattleRepository battleRepository;

    @InjectMocks
    BattleService battleService;

    @Mock
    ActiveUserProvider activeUserProvider;


    BattleMatcher battleMatcher;

    @BeforeEach
    public void init() {
        battleMatcher = new BattleMatcher(battleService, activeUserProvider);
    }

    private static Long idIncrement = 0L;


    private static PlayerParty createPlayerParty(PlayerUser playerUser, boolean isConfirmed) {
        PlayerParty playerParty = new PlayerParty();
        playerParty.setId(idIncrement++);
        playerParty.setConfirmed(isConfirmed);
        playerParty.setPlayerUser(playerUser);
        return playerParty;
    }

    private static PlayerUser createPlayerUser() {
        PlayerUser playerUser = new PlayerUser();
        playerUser.setId(idIncrement++);
        playerUser.setLoginName("TestLoginName" + idIncrement);
        playerUser.setPassword("TestPassword" + idIncrement);
        playerUser.setGameName("TestGameName" + idIncrement);
        return playerUser;
    }

    private static Stream<Arguments> createBattles() {

        PlayerUser userA = createPlayerUser();
        PlayerUser userB = createPlayerUser();
        PlayerUser userC = createPlayerUser();
        PlayerUser userD = createPlayerUser();

        PlayerParty playerPartyB = createPlayerParty(userB, true);
        PlayerParty playerPartyC = createPlayerParty(userC, false);
        PlayerParty playerPartyD = createPlayerParty(userD, true);

        Battle battleBC = new Battle();
        battleBC.setPlayerParties(List.of(playerPartyB, playerPartyC));
        battleBC.setFinished(false);
        battleBC.setWinner(null);
        battleBC.setId(idIncrement++);

        Battle battleBD = new Battle();
        battleBD.setPlayerParties(List.of(playerPartyB, playerPartyD));
        battleBD.setFinished(true);
        battleBD.setWinner(null);
        battleBD.setId(idIncrement++);

        return Stream.of(
                Arguments.of("There is no active battle with player", userA, List.of(userA, userB, userC, userD), List.of(battleBC), Optional.of(userD)),
                Arguments.of("There is one finished battle with player", userA, List.of(userA, userB, userC, userD), List.of(battleBC, battleBD), Optional.of(userD)),
                Arguments.of("There is one finished battle with player", userA, List.of(userA, userB, userC), List.of(battleBC), Optional.empty()));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createBattles")
    public void testFindAllCurrentBattlesByPlayer(String name, PlayerUser playerUser, List<PlayerUser> activeUsers, List<Battle> activeBattles, Optional<PlayerUser> expectedPlayer) {
        Mockito.when(battleRepository.findAll()).thenReturn(activeBattles);
        Mockito.when(activeUserProvider.getActivePlayers()).thenReturn(activeUsers);
        Optional<PlayerUser> actualUser = battleMatcher.findRandomPlayerWithoutBattleExcluding(playerUser);
        assert actualUser.equals(expectedPlayer);

    }
}
