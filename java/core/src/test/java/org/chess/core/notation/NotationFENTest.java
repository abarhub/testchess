package org.chess.core.notation;

import org.chess.core.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NotationFENTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(NotationFENTest.class);

    private NotationFEN notationFEN = new NotationFEN();

    public static Stream<Arguments> createSerialisationValues() {
        return Stream.of(
                Arguments.of("rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R w KQkq - 0 1"),
                Arguments.of("r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R4KnP/2B4R w KQkq - 0 1"),
                Arguments.of("r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R5nP/2B3KR w KQkq - 0 1")
        );
    }

    @ParameterizedTest
    @MethodSource("createSerialisationValues")
    public void createSerialisation(String fenFormat) {

        LOGGER.info("fenFormat={}", fenFormat);

        // methode testée
        Partie partie = notationFEN.createPlateau(fenFormat);

        // vérifications
        assertNotNull(partie);

        Plateau plateau = partie.getPlateau();

        assertNotNull(plateau);

        String res = notationFEN.serialize(partie);

        LOGGER.info("res={}", res);

        assertNotNull(res);
        assertEquals(fenFormat, res);

    }

    @Test
    public void createPlateau() {

        String fenFormat = "rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R w KQkq - 0 1";

        // methode testée
        Partie partie = notationFEN.createPlateau(fenFormat);

        // vérifications
        assertNotNull(partie);

        assertNotNull(partie.getConfigurationPartie());

        assertEquals(Couleur.Blanc, partie.getJoueurCourant());

        ConfigurationPartie config = partie.getConfigurationPartie();

        assertEquals(true, config.isRoqueBlancRoi());
        assertEquals(true, config.isRoqueBlancDame());
        assertEquals(true, config.isRoqueNoirRoi());
        assertEquals(true, config.isRoqueNoirDame());

        assertEquals(0, config.getNbDemiCoupSansCapture());

        assertEquals(1, config.getNbCoup());

        String res = notationFEN.serialize(partie);

        assertEquals(fenFormat, res);
    }

    // fen avec des valeurs differentes
    @Test
    public void createPlateau2() {

        String fenFormat = "rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R b Kq - 15 34";

        // methode testée
        Partie partie = notationFEN.createPlateau(fenFormat);

        // vérifications
        assertNotNull(partie);

        assertNotNull(partie.getConfigurationPartie());

        assertEquals(Couleur.Noir, partie.getJoueurCourant());

        ConfigurationPartie config = partie.getConfigurationPartie();

        assertEquals(true, config.isRoqueBlancRoi());
        assertEquals(false, config.isRoqueBlancDame());
        assertEquals(false, config.isRoqueNoirRoi());
        assertEquals(true, config.isRoqueNoirDame());

        assertEquals(15, config.getNbDemiCoupSansCapture());

        assertEquals(34, config.getNbCoup());

        String res = notationFEN.serialize(partie);

        assertEquals(fenFormat, res);
    }

    // fen sans les information de fin
    @Test
    public void createPlateau3() {

        String fenFormat = "rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R";

        // methode testée
        Partie partie = notationFEN.createPlateau(fenFormat);

        // vérifications
        assertNotNull(partie);

        assertNotNull(partie.getConfigurationPartie());

        assertEquals(Couleur.Blanc, partie.getJoueurCourant());

        ConfigurationPartie config = partie.getConfigurationPartie();

        assertEquals(true, config.isRoqueBlancRoi());
        assertEquals(true, config.isRoqueBlancDame());
        assertEquals(true, config.isRoqueNoirRoi());
        assertEquals(true, config.isRoqueNoirDame());

        assertEquals(0, config.getNbDemiCoupSansCapture());

        assertEquals(1, config.getNbCoup());

        String res = notationFEN.serialize(partie);

        assertEquals(fenFormat + " w KQkq - 0 1", res);
    }

    public static Stream<Arguments> provideCreatePlateauTestEnPassant() {
        return Stream.of(
                Arguments.of("rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R w KQkq - 0 1", Optional.empty()),
                Arguments.of("r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R4KnP/2B4R w KQkq - 0 1", Optional.empty()),
                Arguments.of("r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R5nP/2B3KR w KQkq - 0 1", Optional.empty()),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", Optional.empty()),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq e3 0 1", Optional.of(Position.getPosition("e3"))),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq c6 0 1", Optional.of(Position.getPosition("c6")))
        );
    }

    @ParameterizedTest
    @MethodSource("provideCreatePlateauTestEnPassant")
    public void createPlateauTestEnPassant(String fenFormat, Optional<Position> enPassant) {

        // methode testée
        Partie partie = notationFEN.createPlateau(fenFormat);

        // vérifications
        assertNotNull(partie);

        assertNotNull(partie.getConfigurationPartie());

        assertEquals(Couleur.Blanc, partie.getJoueurCourant());

        ConfigurationPartie config = partie.getConfigurationPartie();

        assertEquals(true, config.isRoqueBlancRoi());
        assertEquals(true, config.isRoqueBlancDame());
        assertEquals(true, config.isRoqueNoirRoi());
        assertEquals(true, config.isRoqueNoirDame());

        assertEquals(0, config.getNbDemiCoupSansCapture());

        assertEquals(1, config.getNbCoup());

        assertEquals(enPassant,config.getPriseEnPassant());

        String res = notationFEN.serialize(partie);

        assertEquals(fenFormat, res);
    }



    public static Stream<Arguments> provideSerialize() {
        return Stream.of(
                Arguments.of("rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R w KQkq - 0 1"),
                Arguments.of("r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R4KnP/2B4R w KQkq - 0 1"),
                Arguments.of("r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R5nP/2B3KR w KQkq - 0 1"),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq e3 0 1"),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq c6 0 1")
        );
    }

    @ParameterizedTest
    @MethodSource("provideSerialize")
    public void serialize(String fenFormat) {

        Partie partie = notationFEN.createPlateau(fenFormat);

        // methode testée
        String res = notationFEN.serialize(partie);

        // vérifications
        assertEquals(fenFormat, res);
    }
}