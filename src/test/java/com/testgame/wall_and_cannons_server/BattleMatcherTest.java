package com.testgame.wall_and_cannons_server;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.PlayerParty;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.persistance.BattleRepository;
import com.testgame.wall_and_cannons_server.services.ActiveUserProvider;
import com.testgame.wall_and_cannons_server.domain.BattleMatcher;
import com.testgame.wall_and_cannons_server.services.BattleService;
import org.junit.jupiter.api.BeforeAll;
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

    private static Long idIncrement = 0L;

    private static final PlayerUser userA = createPlayerUser();
    private static final PlayerUser userB = createPlayerUser();
    private static final PlayerUser userC = createPlayerUser();
    private static final PlayerUser userD = createPlayerUser();

    private static final PlayerParty playerPartyA = createPlayerParty(userA, true);
    private static final PlayerParty playerPartyB = createPlayerParty(userB, false);
    private static final PlayerParty playerPartyC = createPlayerParty(userC, false);
    private static final PlayerParty playerPartyD = createPlayerParty(userD, false);

    @BeforeEach
    public void init() {
        battleMatcher = new BattleMatcher(() -> activeUserProvider.getActivePlayers(), () -> battleService.findAllBattles());
    }

    private static PlayerParty createPlayerParty(PlayerUser playerUser, boolean isConfirmed) {
        PlayerParty playerParty = new PlayerParty();
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

    public static Stream<Arguments> createBattlesForUser() {
        Battle battleBC = new Battle();
        battleBC.setPlayerParties(List.of(playerPartyB, playerPartyC));
        battleBC.setFinished(false);
        battleBC.setWinner(null);
        battleBC.setId(idIncrement++);

        Battle battleAC = new Battle();
        battleAC.setPlayerParties(List.of(playerPartyA, playerPartyC));
        battleAC.setFinished(false);
        battleAC.setWinner(null);
        battleAC.setId(idIncrement++);

        return Stream.of(
                Arguments.of("There is no battle with player", userA, List.of(battleBC), List.of()),
                Arguments.of("There is one battle with player", userA, List.of(battleAC, battleBC), List.of(battleAC)));
    }

    public static Stream<Arguments> createActiveBattlesForUser() {
        Battle battleBC = new Battle();
        battleBC.setPlayerParties(List.of(playerPartyB, playerPartyC));
        battleBC.setFinished(false);
        battleBC.setWinner(null);
        battleBC.setId(idIncrement++);

        Battle battleAC = new Battle();
        battleAC.setPlayerParties(List.of(playerPartyA, playerPartyC));
        battleAC.setFinished(false);
        battleAC.setWinner(null);
        battleAC.setId(idIncrement++);

        Battle finishedBattleAC = new Battle();
        finishedBattleAC.setPlayerParties(List.of(playerPartyA, playerPartyC));
        finishedBattleAC.setFinished(true);
        finishedBattleAC.setWinner(null);
        finishedBattleAC.setId(idIncrement++);

        return Stream.of(
                Arguments.of("There is no active battle with player", userA, List.of(battleBC, finishedBattleAC), List.of()),
                Arguments.of("There is one battle with player", userA, List.of(battleAC, battleBC, finishedBattleAC), List.of(battleAC)));
    }

    public static Stream<Arguments> prepareMatch() {
        Battle battleBC = new Battle();
        battleBC.setPlayerParties(List.of(playerPartyB, playerPartyC));
        battleBC.setFinished(false);
        battleBC.setWinner(null);
        battleBC.setId(idIncrement++);

        Battle battleAC = new Battle();
        battleAC.setPlayerParties(List.of(playerPartyA, playerPartyC));
        battleAC.setFinished(false);
        battleAC.setWinner(null);
        battleAC.setId(idIncrement++);

        Battle battleBD = new Battle();
        battleBD.setPlayerParties(List.of(playerPartyB, playerPartyD));
        battleBD.setFinished(false);
        battleBD.setWinner(null);
        battleBD.setId(idIncrement++);

        Battle finishedBattleAC = new Battle();
        finishedBattleAC.setPlayerParties(List.of(playerPartyA, playerPartyC));
        finishedBattleAC.setFinished(true);
        finishedBattleAC.setWinner(null);
        finishedBattleAC.setId(idIncrement++);

        Battle expectedBattleAC = new Battle();
        expectedBattleAC.setPlayerParties(List.of(playerPartyA, playerPartyC));
        expectedBattleAC.setFinished(false);
        expectedBattleAC.setWinner(null);

        return Stream.of(
                Arguments.of("There is no available players", userA, List.of(userA, userB, userC), List.of(battleAC, battleBC, finishedBattleAC), Optional.empty()),
                Arguments.of("There is available player", userA, List.of(userA, userB, userC), List.of(finishedBattleAC, battleBD), Optional.of(expectedBattleAC)));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createBattles")
    public void testGetRandomPlayerWithoutBattles(String name, PlayerUser playerUser, List<PlayerUser> activeUsers, List<Battle> allBattles, Optional<PlayerUser> expectedPlayer) {
        Mockito.when(battleRepository.findAll()).thenReturn(allBattles);
        Mockito.when(activeUserProvider.getActivePlayers()).thenReturn(activeUsers);
        Optional<PlayerUser> actualUser = battleMatcher.findRandomPlayerWithoutBattleExcluding(playerUser);
        assert actualUser.equals(expectedPlayer);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createBattlesForUser")
    public void testGetBattlesByPlayer(String name, PlayerUser playerUser, List<Battle> allBattles, List<Battle> expectedList) {
        List<Battle> actualList = battleMatcher.getBattlesByPlayer(allBattles, playerUser);
        assert actualList.equals(expectedList);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createActiveBattlesForUser")
    public void testGetActiveBattlesByPlayer(String name, PlayerUser playerUser, List<Battle> allBattles, List<Battle> expectedList) {
        List<Battle> actualList = battleMatcher.getActiveBattlesByPlayer(allBattles, playerUser);
        assert actualList.equals(expectedList);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("prepareMatch")
    public void testMatch(String name, PlayerUser playerUser, List<PlayerUser> activeUsers, List<Battle> allBattles, Optional<Battle> expectedBattle) {
        Mockito.when(battleRepository.findAll()).thenReturn(allBattles);
        Mockito.when(activeUserProvider.getActivePlayers()).thenReturn(activeUsers);
        Optional<Battle> actualBattle = battleMatcher.match(playerUser);
        assert actualBattle.equals(expectedBattle);
    }

}
