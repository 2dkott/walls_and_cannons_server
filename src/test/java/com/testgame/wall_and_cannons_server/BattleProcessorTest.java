package com.testgame.wall_and_cannons_server;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.BattleProcessor;
import com.testgame.wall_and_cannons_server.domain.BattleRound;
import com.testgame.wall_and_cannons_server.domain.PlayerParty;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.persistance.BattleRepository;
import com.testgame.wall_and_cannons_server.persistance.BattleRoundRepository;
import com.testgame.wall_and_cannons_server.persistance.PlayerPartyRepository;
import com.testgame.wall_and_cannons_server.services.BattleService;
import com.testgame.wall_and_cannons_server.services.PlayerPartyService;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class BattleProcessorTest {

    @Mock
    private BattleRepository battleRepository;

    @Mock
    private BattleRoundRepository battleRoundRepository;

    @Mock
    private BattleService battleService;

    @Mock
    PlayerPartyRepository playerPartyRepository;

    @Mock
    PlayerPartyService playerPartyService;
    @Mock
    private Supplier<List<BattleRound>> battleRoundSupplier;
    @Mock
    private Consumer<BattleRound> battleRoundSaver;
    @Mock
    private Consumer<PlayerParty> playerPartySaver;

    private static Long idIncrement = 0L;

    private static final PlayerUser userA = createPlayerUser();
    private static final PlayerUser userB = createPlayerUser();
    private static final PlayerUser userC = createPlayerUser();
    private static final PlayerUser userD = createPlayerUser();

    private static final PlayerParty playerPartyA = createPlayerParty(userA, true);
    private static final PlayerParty playerPartyB = createPlayerParty(userB, false);
    private static final PlayerParty playerPartyC = createPlayerParty(userC, false);
    private static final PlayerParty playerPartyD = createPlayerParty(userD, false);

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

    public static Stream<Arguments> getBattleRounds() {
        Battle battleAB = new Battle();
        battleAB.setPlayerParties(List.of(playerPartyA, playerPartyB));
        battleAB.setFinished(false);
        battleAB.setWinner(null);

        BattleRound battleRoundAB1 = new BattleRound();
        battleRoundAB1.setBattle(battleAB);
        battleRoundAB1.setActive(false);
        battleRoundAB1.setRoundNumber(1);

        BattleRound battleRoundAB2 = new BattleRound();
        battleRoundAB2.setBattle(battleAB);
        battleRoundAB2.setActive(false);
        battleRoundAB2.setRoundNumber(2);

        Battle battleBC= new Battle();
        battleBC.setPlayerParties(List.of(playerPartyB, playerPartyC));
        battleBC.setFinished(false);
        battleBC.setWinner(null);

        BattleRound battleRoundBC1 = new BattleRound();
        battleRoundBC1.setBattle(battleBC);
        battleRoundBC1.setActive(false);
        battleRoundBC1.setRoundNumber(1);

        BattleRound battleRoundBC2 = new BattleRound();
        battleRoundBC2.setBattle(battleBC);
        battleRoundBC2.setActive(false);
        battleRoundBC2.setRoundNumber(2);

        Battle battleCD= new Battle();
        battleCD.setPlayerParties(List.of(playerPartyD, playerPartyC));
        battleCD.setFinished(false);
        battleCD.setWinner(null);

        return Stream.of(Arguments.of(userA, battleAB, List.of(battleRoundBC2, battleRoundBC1, battleRoundAB1, battleRoundAB2), List.of(battleRoundAB1, battleRoundAB2)),
                                      Arguments.of(userB, battleAB, List.of(battleRoundBC2, battleRoundBC1, battleRoundAB1, battleRoundAB2), List.of(battleRoundAB1, battleRoundAB2)),
                         Arguments.of(userD, battleCD, List.of(battleRoundBC2, battleRoundBC1, battleRoundAB1, battleRoundAB2), List.of())
        );

    }

    @ParameterizedTest
    @MethodSource("getBattleRounds")
    public void testGetRoundsByBattle(PlayerUser playerUser, Battle battle, List<BattleRound> allRounds, List<BattleRound> expectedRounds) {
        Mockito.when(battleRoundSupplier.get()).thenReturn(allRounds);
        BattleProcessor battleProcessor = new BattleProcessor(playerUser, battle, battleRoundSupplier, battleRoundSaver, playerPartySaver);
        List<BattleRound> actualBattleRounds = battleProcessor.getRoundsByBattle();
        assert actualBattleRounds.equals(expectedRounds);
    }


    public static BattleRound createBattleRound(List<PlayerParty> parties, Battle battle, int roundNumber, boolean isActive) {
        BattleRound battleRound = new BattleRound();
        battleRound.setId(idIncrement++);
        battleRound.setPlayerParties(parties);
        battleRound.setBattle(battle);
        battleRound.setRoundNumber(roundNumber);
        battleRound.setActive(isActive);
        return battleRound;
    }


}
