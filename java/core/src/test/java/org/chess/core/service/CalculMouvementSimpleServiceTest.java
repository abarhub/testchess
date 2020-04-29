package org.chess.core.service;

import org.apache.commons.collections4.CollectionUtils;
import org.chess.core.domain.*;
import org.chess.core.notation.NotationFEN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CalculMouvementSimpleServiceTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(CalculMouvementSimpleServiceTest.class);

    private CalculMouvementSimpleService calculMouvementSimpleService;

    private NotationFEN notationFEN = new NotationFEN();


    @BeforeEach
    void setUp() {
        calculMouvementSimpleService = new CalculMouvementSimpleService();
    }

    @Test
    void calcul() {

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Partie partie = notationFEN.createPlateau(plateau);

        // methode testée
        var res = calculMouvementSimpleService.calcul(partie.getPlateau(), partie.getJoueurCourant());

        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups = nbCoups(res);
        LOGGER.info("nbCoups={}", nbCoups);
        assertEquals(20, nbCoups);
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
                Arguments.of("8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -", 1, 39),
                Arguments.of("8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -", 1, 237),
                Arguments.of("r3k2r/p6p/8/B7/1pp1p3/3b4/P6P/R3K2R w KQkq -", 1, 17),
                Arguments.of("r3k2r/p6p/8/B7/1pp1p3/3b4/P6P/R3K2R w KQkq -", 2, 341),
                Arguments.of("r3k2r/p6p/8/B7/1pp1p3/3b4/P6P/R3K2R w KQkq -", 3, 6666),
                Arguments.of("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -", 1, 9),
                Arguments.of("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -", 2, 85),
                Arguments.of("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -", 3, 795),
                Arguments.of("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq -", 1, 29),
                Arguments.of("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq -", 2, 953),
                Arguments.of("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq -", 3, 27990)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCalculPerf")
    void calculPerf(String plateau, int depth, long perfRef) {

        Partie partie = notationFEN.createPlateau(plateau);

        // methode testée
        long res = calculPerf(partie, depth);

        // vérifications
        LOGGER.info("res={}", res);
        assertEquals(perfRef, res);
    }

    // methodes utilitaires

    private long nbCoups(ListeMouvements2 listeMouvements) {
        long nb = 0;
        for (Map.Entry<PieceCouleurPosition, List<IMouvement>> entry : listeMouvements.getMapMouvements().entrySet()) {
            nb += entry.getValue().size();
        }
        return nb;
    }

    private long calculPerf(Partie partie, int depth) {

        return calculPerf(partie.getPlateau(), partie.getJoueurCourant(), depth);
    }

    private long calculPerf(Plateau plateau, Couleur joueurCourant, int depth) {
        long resultat = 0;

        if (depth <= 0) {
            resultat = 1;
        } else {
            var res = calculMouvementSimpleService.calcul(plateau, joueurCourant);

            var map = res.getMapMouvements();
            assertNotNull(map);
            if (!map.isEmpty()) {
                var iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<PieceCouleurPosition, List<IMouvement>> tmp = iter.next();
                    if (!CollectionUtils.isEmpty(tmp.getValue())) {
                        for (var tmp2 : tmp.getValue()) {
//                            if(plateau instanceof PlateauBis){
//                                PlateauBis plateau2= (PlateauBis) plateau;
//                                plateau2.move(tmp.getKey().getPosition(), tmp2.getPosition());
//                                resultat += calculPerf(plateau2, calculMouvementBisService.joueurAdversaire(joueurCourant), depth - 1);
//                                plateau2.undo();
//                            } else {
                            //Partie partie2 = new Partie(partie);
                            Plateau plateau2 = new Plateau(plateau);
                            //assertEquals(joueurCourant, partie2.getJoueurCourant());
                            //plateau2.move(tmp.getKey().getPosition(), tmp2.getPosition());
                            plateau2.move(tmp.getKey().getPosition(), tmp2);
                            //partie2.setMove(tmp.getKey().getPosition(), tmp2.getPosition());
                            //assertEquals(calculMouvementBisService.joueurAdversaire(joueurCourant), partie2.getJoueurCourant());
                            resultat += calculPerf(plateau2, calculMouvementSimpleService.joueurAdversaire(joueurCourant), depth - 1);
//                            }
                        }
                    }
                }
            }
        }
        return resultat;
    }

}