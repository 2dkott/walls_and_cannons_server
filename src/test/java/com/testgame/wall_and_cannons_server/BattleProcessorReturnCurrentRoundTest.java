package com.testgame.wall_and_cannons_server;

import static org.assertj.core.api.Assertions.assertThat;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.BattleProcessor;
import com.testgame.wall_and_cannons_server.domain.BattleRound;
import com.testgame.wall_and_cannons_server.domain.PlayerParty;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
public class BattleProcessorReturnCurrentRoundTest {

    private static Long idIncrement = 0L;

    private static final PlayerUser userA = createPlayerUser();
    private static final PlayerUser userB = createPlayerUser();
    private static final PlayerUser userC = createPlayerUser();
    private static final PlayerUser userD = createPlayerUser();

    private static final PlayerParty playerPartyA = createPlayerParty(userA, true);
    private static final PlayerParty playerPartyANotCnfrmd = createPlayerParty(userA, false);
    private static final PlayerParty playerPartyB = createPlayerParty(userB, true);
    private static final PlayerParty playerPartyBNotCnfrmd = createPlayerParty(userB, false);
    private static final PlayerParty playerPartyC = createPlayerParty(userC, true);
    private static final PlayerParty playerPartyD = createPlayerParty(userD, true);

    List<BattleRound> battleRoundStorage = new ArrayList<>();
    List<PlayerParty> playerPartyStorage = new ArrayList<>();
    private final Consumer<BattleRound> battleRoundSaver =
            (battleRound -> battleRoundStorage.add(battleRound));
    private final Consumer<PlayerParty> playerPartySaver =
            (playerParty -> playerPartyStorage.add(playerParty));

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

        BattleRound battleRoundGoodA1 = new BattleRound();
        battleRoundGoodA1.setBattle(battleAB);
        battleRoundGoodA1.setActive(false);
        battleRoundGoodA1.setRoundNumber(0);
        battleRoundGoodA1.setRoundDuration(60);
        battleRoundGoodA1.setRoundStartTime(LocalDateTime.now().minusSeconds(120));

        BattleRound battleRoundGoodA2 = new BattleRound();
        battleRoundGoodA2.setBattle(battleAB);
        battleRoundGoodA2.setActive(true);
        battleRoundGoodA2.setRoundNumber(1);
        battleRoundGoodA2.setRoundDuration(60);
        battleRoundGoodA2.setRoundStartTime(LocalDateTime.now());

        BattleRound battleRoundBadA1 = new BattleRound();
        battleRoundBadA1.setBattle(battleAB);
        battleRoundBadA1.setActive(false);
        battleRoundBadA1.setRoundNumber(0);
        battleRoundBadA1.setRoundDuration(60);
        battleRoundBadA1.setRoundStartTime(LocalDateTime.now().minusSeconds(180));

        BattleRound battleRoundBadA2 = new BattleRound();
        battleRoundBadA2.setBattle(battleAB);
        battleRoundBadA2.setActive(true);
        battleRoundBadA2.setRoundNumber(1);
        battleRoundBadA2.setRoundDuration(60);
        battleRoundBadA2.setRoundStartTime(LocalDateTime.now().minusSeconds(61));

        BattleRound battleRoundBadB1 = new BattleRound();
        battleRoundBadB1.setBattle(battleAB);
        battleRoundBadB1.setActive(false);
        battleRoundBadB1.setRoundNumber(0);
        battleRoundBadB1.setRoundDuration(60);
        battleRoundBadB1.setRoundStartTime(LocalDateTime.now().minusSeconds(240));

        BattleRound battleRoundBadB2 = new BattleRound();
        battleRoundBadB2.setBattle(battleAB);
        battleRoundBadB2.setActive(false);
        battleRoundBadB2.setRoundNumber(1);
        battleRoundBadB2.setRoundDuration(60);
        battleRoundBadB2.setRoundStartTime(LocalDateTime.now().minusSeconds(180));

        BattleRound battleRoundBadB3 = new BattleRound();
        battleRoundBadB3.setBattle(battleAB);
        battleRoundBadB3.setActive(true);
        battleRoundBadB3.setRoundNumber(2);
        battleRoundBadB3.setRoundDuration(60);
        battleRoundBadB3.setRoundStartTime(LocalDateTime.now().minusSeconds(30));

        BattleRound battleRoundBadC1 = new BattleRound();
        battleRoundBadC1.setBattle(battleAB);
        battleRoundBadC1.setActive(false);
        battleRoundBadC1.setRoundNumber(0);
        battleRoundBadC1.setRoundDuration(60);
        battleRoundBadC1.setRoundStartTime(LocalDateTime.now().minusSeconds(180));

        BattleRound battleRoundBadC2 = new BattleRound();
        battleRoundBadC2.setBattle(battleAB);
        battleRoundBadC2.setActive(false);
        battleRoundBadC2.setRoundNumber(1);
        battleRoundBadC2.setRoundDuration(60);
        battleRoundBadC2.setRoundStartTime(LocalDateTime.now().minusSeconds(30));

        return Stream.of(
                Arguments.of(
                        userA,
                        battleAB,
                        List.of(battleRoundGoodA1, battleRoundGoodA2),
                        1,
                        Optional.of(battleRoundGoodA2)),
                Arguments.of(
                        userB,
                        battleAB,
                        List.of(battleRoundGoodA1, battleRoundGoodA2),
                        1,
                        Optional.of(battleRoundGoodA2)),
                Arguments.of(
                        userA,
                        battleAB,
                        List.of(battleRoundBadA1, battleRoundBadA1),
                        1,
                        Optional.empty()),
                Arguments.of(
                        userB,
                        battleAB,
                        List.of(battleRoundBadA1, battleRoundBadA1),
                        1,
                        Optional.empty()),
                Arguments.of(
                        userA,
                        battleAB,
                        List.of(battleRoundBadB1, battleRoundBadB2, battleRoundBadB3),
                        1,
                        Optional.empty()),
                Arguments.of(
                        userB,
                        battleAB,
                        List.of(battleRoundBadB1, battleRoundBadB2, battleRoundBadB3),
                        1,
                        Optional.empty()),
                Arguments.of(
                        userA,
                        battleAB,
                        List.of(battleRoundBadC1, battleRoundBadC2),
                        1,
                        Optional.empty()),
                Arguments.of(
                        userB,
                        battleAB,
                        List.of(battleRoundBadC1, battleRoundBadC2),
                        1,
                        Optional.empty()));
    }

    @ParameterizedTest
    @MethodSource("getBattleRounds")
    public void testGetRoundsByBattle(
            PlayerUser playerUser,
            Battle battle,
            List<BattleRound> allRounds,
            int roundNumber,
            Optional<BattleRound> expectedRound) {
        BattleProcessor battleProcessor =
                new BattleProcessor(
                        playerUser, battle, () -> allRounds, battleRoundSaver, playerPartySaver);
        Optional<BattleRound> battleRound = battleProcessor.returnCurrentRound(roundNumber);
        assertThat(battleRound).isEqualTo(expectedRound);
    }
}
