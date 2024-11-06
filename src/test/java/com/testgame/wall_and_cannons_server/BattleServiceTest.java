package com.testgame.wall_and_cannons_server;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.persistance.BattleRepository;
import com.testgame.wall_and_cannons_server.domain.PlayerParty;
import com.testgame.wall_and_cannons_server.services.BattleService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class BattleServiceTest {
    @Mock
    BattleRepository battleRepository;

    @InjectMocks
    BattleService battleService;

    PlayerUser playerUserA = new PlayerUser();
    PlayerUser playerUserB = new PlayerUser();

    private static Long idIncrement = 0L;

    public BattleServiceTest() {
        playerUserA.setId(1L);
        playerUserA.setLoginName("TestLoginName");
        playerUserA.setPassword("TestPassword");
        playerUserA.setGameName("TestGameName");

        playerUserB.setId(2L);
        playerUserB.setLoginName("TestLoginName");
        playerUserB.setPassword("TestPassword");
        playerUserB.setGameName("TestGameName");
    }

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
        PlayerParty playerPartyAC = createPlayerParty(userA, true);
        PlayerParty playerPartyAUNC = createPlayerParty(userA, false);
        PlayerParty playerPartyB = createPlayerParty(userB, false);
        PlayerParty playerPartyC = createPlayerParty(userC, false);


        Battle battle1UserA = new Battle();
        battle1UserA.setPlayerParties(List.of(playerPartyAC, playerPartyB, playerPartyC));
        battle1UserA.setFinished(true);
        battle1UserA.setWinner(null);
        battle1UserA.setId(idIncrement++);

        Battle battle2UserA = new Battle();
        battle2UserA.setPlayerParties(List.of(playerPartyAUNC, playerPartyB, playerPartyC));
        battle2UserA.setFinished(true);
        battle2UserA.setWinner(null);
        battle2UserA.setId(idIncrement++);

        Battle battle3UserA = new Battle();
        battle3UserA.setPlayerParties(List.of(playerPartyAC, playerPartyB, playerPartyC));
        battle3UserA.setFinished(false);
        battle3UserA.setWinner(null);
        battle3UserA.setId(idIncrement++);

        Battle battle4UserA = new Battle();
        battle4UserA.setPlayerParties(List.of(playerPartyAUNC, playerPartyB, playerPartyC));
        battle4UserA.setFinished(false);
        battle4UserA.setWinner(null);
        battle4UserA.setId(idIncrement++);

        Battle battleB = new Battle();
        battleB.setPlayerParties(List.of(playerPartyB, playerPartyC));
        battleB.setFinished(false);
        battleB.setWinner(null);
        battleB.setId(idIncrement++);

        return Stream.of(
                Arguments.of("There is one confirmed and finished battle with player",
                        userA,
                        true,
                        true,
                        List.of(battle1UserA, battleB),
                        List.of(battle1UserA)),
                Arguments.of("There is one not confirmed and finished battle with player",
                        userA,
                        false,
                        true,
                        List.of(battle2UserA, battle1UserA, battleB),
                        List.of(battle2UserA)),
                Arguments.of("There is confirmed and not finished battle with player",
                        userA,
                        true,
                        false,
                        List.of(battle3UserA, battle1UserA, battleB),
                        List.of(battle3UserA)),
                Arguments.of("There is not confirmed and not finished battle with player",
                        userA,
                        false,
                        false,
                        List.of(battle4UserA, battle1UserA, battleB),
                        List.of(battle4UserA)),
                Arguments.of("There are no battle with player", userA, true, true, List.of(battleB), List.of()));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createBattles")
    public void testFindAllCurrentBattlesByPlayer(String name, PlayerUser playerUser, boolean isConfirmed, boolean isFinished, List<Battle> allBattles, List<Battle> expectedBattles) {
        Mockito.when(battleRepository.findAll()).thenReturn(allBattles);
        assert expectedBattles.equals(battleService.getBattlesByPlayerUser(playerUser, isConfirmed, isFinished));
    }

}
