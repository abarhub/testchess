package org.chess.core.service;

import org.chess.core.domain.*;
import org.chess.core.notation.NotationFEN;
import org.chess.core.utils.CalculPerft;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculMouvementBisServiceTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(CalculMouvementBisServiceTest.class);

    private CalculMouvementBisService calculMouvementBisService;

    private NotationFEN notationFEN = new NotationFEN();

    @BeforeEach
    void setUp() {
        calculMouvementBisService = new CalculMouvementBisService();
    }

    @Test
    void calculMouvements() {

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Partie partie = notationFEN.createPlateau(plateau);

        // methode testée
        var res = calculMouvementBisService.calculMouvements(partie);

        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups = nbCoups(res);
        LOGGER.info("nbCoups={}", nbCoups);
        assertEquals(20, nbCoups);
    }

    @Test
    @Disabled("C'est la classe CalculMouvementBisServiceTest qui ne prends pas en compte si les roques sont possibles")
    void calculMouvements2() {

        String plateau = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -";
        Partie partie = notationFEN.createPlateau(plateau);

        /**
         * Il donne 46, car il manque les roque droit et gauche (e1g1 et e1c1)
         * C'est la classe calculMouvementBisService qui ne prends pas en compte l'état de la partie
         * La classe CalculMouvementBisServiceTest n'a pas ce bug
         */

        // methode testée
        var res = calculMouvementBisService.calculMouvements(partie);

        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups = nbCoups(res);
        LOGGER.info("nbCoups={}", nbCoups);
        if (nbCoups != 48) {
            afficheDeplacements(res);
        }
        assertEquals(48, nbCoups);
    }

    private static Stream<Arguments> provideCalculMouvementsParameters() {
        return Stream.of(
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 20),
                // le droit de faire le roque n'est pas pris en compte d'ou l'erreurt de 46 au lieux de 48
                //Arguments.of("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -", 48),
                // il propose le déplacement b5b6, ce qui met le roi en echec.
                // Bug de la classe CalculMouvementBisService
                // qui ne prends pas bien en compte cela
                //Arguments.of("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ", 14),
                Arguments.of("n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - - 0 1", 24)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCalculMouvementsParameters")
    void calculMouvementsParameters(String plateau, long nbCoupsRef) {

        Partie partie = notationFEN.createPlateau(plateau);

        // methode testée
        var res = calculMouvementBisService.calculMouvements(partie);

        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups = nbCoups(res);
        LOGGER.info("nbCoups={}", nbCoups);
        if (nbCoupsRef != nbCoups) {
            LOGGER.error("plateau: \n{}", affichePlateau(partie.getPlateau()));
            afficheDeplacements(res);
        }
        assertEquals(nbCoupsRef, nbCoups);
    }


    private static Stream<Arguments> provideCalculPerf() {
        return Stream.of(
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 1, 20),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 2, 400),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 3, 8_902),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 4, 197_281),
                //Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",5, 4_865_609),
                //Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",6, 119_060_324),
                Arguments.of("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 1", 1, 18),

                //Arguments.of("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -", 48),
                //Arguments.of("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ", 14),
                //Arguments.of("n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - - 0 1", 24)
                Arguments.of("8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -", 1, 5),
                Arguments.of("8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -", 2, 39),
                Arguments.of("8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -", 3, 237),
                Arguments.of("r3k2r/p6p/8/B7/1pp1p3/3b4/P6P/R3K2R w KQkq -", 1, 17),
                Arguments.of("r3k2r/p6p/8/B7/1pp1p3/3b4/P6P/R3K2R w KQkq -", 2, 341),
                Arguments.of("r3k2r/p6p/8/B7/1pp1p3/3b4/P6P/R3K2R w KQkq -", 3, 6666),
                Arguments.of("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -", 1, 9),
                Arguments.of("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -", 2, 85),
                Arguments.of("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -", 3, 795),
                Arguments.of("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq -", 1, 29),
                Arguments.of("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq -", 2, 953),
                Arguments.of("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq -", 3, 27990),
                Arguments.of("8/p7/8/1P6/K1k3pP/6P1/8/8 b - h3 0 1", 1, 8)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCalculPerf")
    void calculPerf(String plateau, int depth, long perfRef) {

        Partie partie = notationFEN.createPlateau(plateau);

        CalculPerft calculPerft = new CalculPerft();

        // methode testée
        long res = calculPerft.calculPerftAfficheNiveau1(partie, depth);

        // vérifications
        LOGGER.info("fen={}", plateau);
        LOGGER.info("depth={}", depth);
        LOGGER.info("res={}", res);
        assertEquals(perfRef, res);
    }

    @Test
    void calculPerfTest() {

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        final int max = 4;

        for (int i = 0; i <= max; i++) {

            int depth = i;

            LOGGER.info("depth={}", depth);

            Partie partie = notationFEN.createPlateau(plateau);

            CalculPerft calculPerft = new CalculPerft();

            // methode testée
            Instant debut = Instant.now();
            long res = calculPerft.calculPerft(partie, depth);
            Instant fin = Instant.now();

            // vérifications

            LOGGER.info("res={}", res);
            LOGGER.info("duree={}", Duration.between(debut, fin));
            //assertEquals(perfRef, res);
        }
    }


    @Test
    void calculPerfTest2() {

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        final int max = 4;

        for (int i = 0; i <= max; i++) {

            int depth = i;

            LOGGER.info("depth={}", depth);

            Partie partie = notationFEN.createPlateau(plateau);

            CalculPerft calculPerft = new CalculPerft();

            // methode testée
            Instant debut = Instant.now();
            long res = calculPerft.calculPerft(partie, depth);
            Instant fin = Instant.now();

            // vérifications

            LOGGER.info("res={}", res);
            LOGGER.info("duree={}", Duration.between(debut, fin));
            //assertEquals(perfRef, res);
        }
    }

    @Test
    void calculPerfTest3() {

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        int depth = 4;

        LOGGER.info("depth={}", depth);

        Partie partie = notationFEN.createPlateau(plateau);

        CalculPerft calculPerft = new CalculPerft();

        // methode testée
        Instant debut = Instant.now();
        long res = calculPerft.calculPerft(partie, depth);
        Instant fin = Instant.now();

        // vérifications

        LOGGER.info("res={}", res);
        LOGGER.info("duree={}", Duration.between(debut, fin));
        //assertEquals(perfRef, res);
        //}

        LongSummaryStatistics res2 = calculMouvementBisService.getDureeTotal().stream().mapToLong(x -> x.toMillis()).summaryStatistics();
        LOGGER.info("res2={}", res2);

        //LOGGER.info("stopWatch={}",calculMouvementBisService.getStopWatch().prettyPrint());
        LOGGER.info("stopWatch2={}", calculMouvementBisService.getStopWatch2());

        LOGGER.info("stopWatch liste depl={}", calculMouvementBisService.getStopWatchListeDeplacement());
        LOGGER.info("stopWatch genere mvt={}", calculMouvementBisService.getStopWatchGenereDeplacement());
        LOGGER.info("stopWatch suppr echecs={}", calculMouvementBisService.getStopWatchSupprEchecs());
    }

    @Test
    void calculPerfTestAnalyseErreurs() {

        //String plateau = "8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -";
        String plateau = "8/p7/8/1P6/K1k3pP/6P1/8/8 b - h3 0 1";

        final int depth = 1;
        //final int perfRef=39;
        final int perfRef = 8;

        LOGGER.info("depth={}", depth);


        Partie partie = notationFEN.createPlateau(plateau);

        CalculPerft calculPerft = new CalculPerft();

        // methode testée
        long res = calculPerft.calculPerft(partie, depth);

        // vérifications
        LOGGER.info("fen={}", plateau);
        LOGGER.info("depth={}", depth);
        LOGGER.info("res={}", res);
        assertEquals(perfRef, res);
    }

    // methodes utilitaires

    private long nbCoups(ListeMouvements2 listeMouvements) {
        long nb = 0;
        for (Entry<PieceCouleurPosition, List<IMouvement>> entry : listeMouvements.getMapMouvements().entrySet()) {
            nb += entry.getValue().size();
        }
        return nb;
    }

    private String affichePlateau(Plateau plateau) {
        return plateau.getRepresentation2().replaceAll(" ", "_");
    }

    private void afficheDeplacements(ListeMouvements2 res) {
        Map<PieceCouleurPosition, List<IMouvement>> map = res.getMapMouvements();
        StringBuilder str = new StringBuilder();
        Set<String> set = new TreeSet<>();
        for (Entry<PieceCouleurPosition, List<IMouvement>> move : map.entrySet()) {
            for (IMouvement mvt : move.getValue()) {
                str.setLength(0);
                str.append(mvt.getPositionSource());
                str.append(mvt.getPositionDestination());
                set.add(str.toString());
            }
        }
        str.setLength(0);
        for (String s : set) {
            str.append(s);
            str.append("\n");
        }
        LOGGER.info("mvt(count:{}):\n{}", set.size(),str);
    }
}