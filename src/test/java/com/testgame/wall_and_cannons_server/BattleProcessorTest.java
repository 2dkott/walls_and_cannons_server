package com.testgame.wall_and_cannons_server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.testgame.wall_and_cannons_server.domain.Battle;
import com.testgame.wall_and_cannons_server.domain.BattleProcessor;
import com.testgame.wall_and_cannons_server.domain.BattleRound;
import com.testgame.wall_and_cannons_server.domain.PlayerParty;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import com.testgame.wall_and_cannons_server.exceptions.NoRoundsForBattleException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
public class BattleProcessorTest {

    private final Consumer<BattleRound> battleRoundSaver = (battleRound -> {});
    private final Consumer<PlayerParty> playerPartySaver = (PlayerParty -> {});

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

        return Stream.of(
                Arguments.of(
                        userA,
                        battleAB,
                        List.of(battleRoundBC2, battleRoundBC1, battleRoundAB1, battleRoundAB2),
                        List.of(battleRoundAB1, battleRoundAB2)),
                Arguments.of(
                        userB,
                        battleAB,
                        List.of(battleRoundBC2, battleRoundBC1, battleRoundAB1, battleRoundAB2),
                        List.of(battleRoundAB1, battleRoundAB2)),
                Arguments.of(
                        userD,
                        battleCD,
                        List.of(battleRoundBC2, battleRoundBC1, battleRoundAB1, battleRoundAB2),
                        List.of()));
    }

    public static Stream<Arguments> getBattleRound() {
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

        Battle battleBC = new Battle();
        battleBC.setPlayerParties(List.of(playerPartyB, playerPartyC));
        battleBC.setFinished(false);
        battleBC.setWinner(null);

        BattleRound battleRoundBC1 = new BattleRound();
        battleRoundBC1.setBattle(battleBC);
        battleRoundBC1.setActive(false);
        battleRoundBC1.setRoundNumber(1);

        return Stream.of(
                Arguments.of(
                        userA,
                        battleAB,
                        List.of(battleRoundBC1, battleRoundAB1, battleRoundAB2),
                        battleRoundAB2),
                Arguments.of(
                        userB,
                        battleBC,
                        List.of(battleRoundBC1, battleRoundAB1, battleRoundAB2),
                        battleRoundBC1));
    }

    public static Stream<Arguments> getBattleRoundsForConfirmation() {
        Battle battle = new Battle();
        battle.setPlayerParties(List.of(playerPartyA, playerPartyB));
        battle.setFinished(false);
        battle.setWinner(null);

        BattleRound battleRoundANotCnfrmdB = new BattleRound();
        battleRoundANotCnfrmdB.setRoundNumber(1);
        battleRoundANotCnfrmdB.setActive(false);
        battleRoundANotCnfrmdB.setPlayerParties(List.of(playerPartyANotCnfrmd, playerPartyB));
        battleRoundANotCnfrmdB.setBattle(battle);

        BattleRound battleRoundANotCnfrmdBNotCnfrmd = new BattleRound();
        battleRoundANotCnfrmdBNotCnfrmd.setRoundNumber(1);
        battleRoundANotCnfrmdBNotCnfrmd.setActive(false);
        battleRoundANotCnfrmdBNotCnfrmd.setBattle(battle);
        battleRoundANotCnfrmdBNotCnfrmd.setPlayerParties(
                List.of(playerPartyANotCnfrmd, playerPartyBNotCnfrmd));

        BattleRound battleRoundAB = new BattleRound();
        battleRoundAB.setRoundNumber(1);
        battleRoundAB.setActive(false);
        battleRoundAB.setBattle(battle);
        battleRoundAB.setPlayerParties(List.of(playerPartyA, playerPartyB));

        return Stream.of(
                Arguments.of(userA, battle, battleRoundANotCnfrmdB, List.of(playerPartyANotCnfrmd)),
                Arguments.of(userB, battle, battleRoundANotCnfrmdB, List.of(playerPartyANotCnfrmd)),
                Arguments.of(
                        userA,
                        battle,
                        battleRoundANotCnfrmdBNotCnfrmd,
                        List.of(playerPartyANotCnfrmd, playerPartyBNotCnfrmd)),
                Arguments.of(
                        userB,
                        battle,
                        battleRoundANotCnfrmdBNotCnfrmd,
                        List.of(playerPartyANotCnfrmd, playerPartyBNotCnfrmd)),
                Arguments.of(userA, battle, battleRoundAB, List.of()));
    }

    public static Stream<Arguments> getRoundForDurationTest() {

        Battle battle = new Battle();

        BattleRound battleRoundA = new BattleRound();
        battleRoundA.setRoundNumber(0);
        battleRoundA.setActive(false);
        battleRoundA.setBattle(battle);
        battleRoundA.setPlayerParties(List.of(playerPartyA, playerPartyB));
        battleRoundA.setRoundStartTime(LocalDateTime.now());
        battleRoundA.setRoundDuration(60);

        BattleRound battleRoundB = new BattleRound();
        battleRoundB.setRoundNumber(0);
        battleRoundB.setActive(false);
        battleRoundB.setBattle(battle);
        battleRoundB.setPlayerParties(List.of(playerPartyA, playerPartyB));
        battleRoundB.setRoundStartTime(LocalDateTime.now().minusSeconds(30));
        battleRoundB.setRoundDuration(60);

        BattleRound battleRoundC = new BattleRound();
        battleRoundC.setRoundNumber(0);
        battleRoundC.setActive(false);
        battleRoundC.setBattle(battle);
        battleRoundC.setPlayerParties(List.of(playerPartyA, playerPartyB));
        battleRoundC.setRoundStartTime(LocalDateTime.now().minusSeconds(60));
        battleRoundC.setRoundDuration(60);

        BattleRound battleRoundD = new BattleRound();
        battleRoundD.setRoundNumber(0);
        battleRoundD.setActive(false);
        battleRoundD.setBattle(battle);
        battleRoundD.setPlayerParties(List.of(playerPartyA, playerPartyB));
        battleRoundD.setRoundStartTime(LocalDateTime.now().minusSeconds(61));
        battleRoundD.setRoundDuration(60);

        BattleRound battleRoundE = new BattleRound();
        battleRoundE.setRoundNumber(0);
        battleRoundE.setActive(false);
        battleRoundE.setBattle(battle);
        battleRoundE.setPlayerParties(List.of(playerPartyA, playerPartyB));
        battleRoundE.setRoundStartTime(LocalDateTime.now().minusSeconds(500));
        battleRoundE.setRoundDuration(60);

        return Stream.of(
                Arguments.of(userA, battle, battleRoundA, true),
                Arguments.of(userA, battle, battleRoundB, true),
                Arguments.of(userA, battle, battleRoundC, true),
                Arguments.of(userA, battle, battleRoundD, false),
                Arguments.of(userA, battle, battleRoundE, false));
    }

    @ParameterizedTest
    @MethodSource("getBattleRounds")
    public void testGetRoundsByBattle(
            PlayerUser playerUser,
            Battle battle,
            List<BattleRound> allRounds,
            List<BattleRound> expectedRounds) {
        BattleProcessor battleProcessor =
                new BattleProcessor(
                        playerUser, battle, () -> allRounds, battleRoundSaver, playerPartySaver);
        List<BattleRound> actualBattleRounds = battleProcessor.getRoundsByBattle();
        assert actualBattleRounds.equals(expectedRounds);
    }

    @ParameterizedTest
    @MethodSource("getBattleRound")
    public void testGetLatestRound(
            PlayerUser playerUser,
            Battle battle,
            List<BattleRound> allRounds,
            BattleRound expectedRound) {
        BattleProcessor battleProcessor =
                new BattleProcessor(
                        playerUser, battle, () -> allRounds, battleRoundSaver, playerPartySaver);
        BattleRound actualBattleRound = battleProcessor.getLatestRound();
        assertThat(actualBattleRound).isEqualTo(expectedRound);
    }

    @Test
    public void testGetLatestRoundEmpty() {

        Battle battle = new Battle();
        battle.setPlayerParties(List.of(playerPartyA, playerPartyB));
        battle.setFinished(false);
        battle.setWinner(null);

        Battle battleBC = new Battle();
        battleBC.setPlayerParties(List.of(playerPartyB, playerPartyC));
        battleBC.setFinished(false);
        battleBC.setWinner(null);

        BattleRound battleRoundBC1 = new BattleRound();
        battleRoundBC1.setBattle(battleBC);
        battleRoundBC1.setActive(false);
        battleRoundBC1.setRoundNumber(1);

        BattleProcessor battleProcessor =
                new BattleProcessor(
                        userA,
                        battle,
                        () -> List.of(battleRoundBC1),
                        battleRoundSaver,
                        playerPartySaver);
        assertThatThrownBy(battleProcessor::getLatestRound)
                .isInstanceOf(NoRoundsForBattleException.class);
    }

    @ParameterizedTest
    @MethodSource("getBattleRoundsForConfirmation")
    public void testGetNotConfirmedPartiesFromRound(
            PlayerUser user,
            Battle battle,
            BattleRound battleRound,
            List<PlayerParty> expectedParties) {
        BattleProcessor battleProcessor =
                new BattleProcessor(
                        user,
                        battle,
                        () -> List.of(battleRound),
                        battleRoundSaver,
                        playerPartySaver);
        List<PlayerParty> actualPlayerParties = battleProcessor.getNotConfirmedPartiesFromRound(battleRound);
        assertThat(actualPlayerParties).isEqualTo(expectedParties);
    }

    @ParameterizedTest
    @MethodSource("getRoundForDurationTest")
    public void testIsStillInFineTimeout(PlayerUser user, Battle battle, BattleRound battleRound, boolean expectedIsGood) {
        BattleProcessor battleProcessor =
                new BattleProcessor(
                        user,
                        battle,
                        () -> List.of(battleRound),
                        battleRoundSaver,
                        playerPartySaver);
        boolean actualIsGood = battleProcessor.isStillInFineTimeout(battleRound);
        assertThat(actualIsGood).isEqualTo(expectedIsGood);
    }
}
