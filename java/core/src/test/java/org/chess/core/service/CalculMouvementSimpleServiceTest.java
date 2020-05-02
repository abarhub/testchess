package org.chess.core.service;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.chess.core.domain.*;
import org.chess.core.notation.NotationFEN;
import org.chess.core.utils.PositionTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
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
    @Disabled
    void calculPerf(String plateau, int depth, long perfRef) {

        Partie partie = notationFEN.createPlateau(plateau);

        // methode testée
        long res = calculPerf(partie, depth);

        // vérifications
        LOGGER.info("res={}", res);
        assertEquals(perfRef, res, "fen="+plateau+"\n"+ getPlateau(partie));
    }

    private static Stream<Arguments> provideTestDeplacementPiece() {
        return Stream.of(
                // 1er déplacement blanc
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "a2", liste("a3", "a4")),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "b2", liste("b3", "b4")),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "c2", liste("c3", "c4")),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "d2", liste("d3", "d4")),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "e2", liste("e3", "e4")),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "f2", liste("f3", "f4")),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "g2", liste("g3", "g4")),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "h2", liste("h3", "h4")),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "b1", liste("a3", "c3")),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "g1", liste("f3", "h3")),


                // déplacements roi
                Arguments.of("3k4/4q3/8/8/8/8/8/3K4 w - - 0 1", "d1", liste("c1", "c2", "d2")),
                Arguments.of("3k4/8/8/8/4q3/8/8/3K4 w - - 0 1", "d1", liste("c1", "d2")),
                Arguments.of("8/7p/8/8/8/3K1k2/8/8 w - - 0 1", "d3", liste("d2", "c2", "c3", "c4", "d4")),
                Arguments.of("8/7p/8/8/8/3K1k2/8/8 w - - 0 1", "d3", liste("d2", "c2", "d4", "c3", "c4")),
                Arguments.of("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1", "e1", liste("d2", "e2", "f2", "g1", "c1", "f1", "d1")), // roque blanc

                // déplacement pion
                Arguments.of("1k6/8/8/8/8/8/4P3/1K6 w - - 0 1", "e2", liste("e3", "e4")),
                Arguments.of("1k6/8/8/8/8/5p2/4P3/1K6 w - - 0 1", "e2", liste("e3", "e4","f3")),
                Arguments.of("1k6/8/8/8/8/3p4/4P3/1K6 w - - 0 1", "e2", liste("e3", "e4","d3")),
                Arguments.of("k7/8/8/3Pp3/8/8/8/K7 w - e6 0 1", "d5", liste("d6", "e6")), // en passant blanc
                Arguments.of("k7/8/8/2pP4/8/8/8/K7 w - c6 0 1", "d5", liste("d6", "c6")), // en passant blanc
                Arguments.of("k7/8/8/3Pp3/8/8/8/K7 w - - 0 1", "d5", liste("d6")), // en passant blanc
                Arguments.of("k7/8/8/3Pp3/8/8/8/K7 w - e6 0 1", "d5", liste("d6", "e6")), // en passant blanc
                Arguments.of("k7/8/3P4/4p3/8/8/8/K7 w - e6 0 1", "d6", liste("d7")), // en passant blanc (pion pas à la bonne place)
                Arguments.of("k5b1/8/8/3Pp3/8/8/K7/8 w - e6 0 1", "d5", liste("e6")), // en passant blanc avec roi attaque
                Arguments.of("1k6/8/8/1b6/8/8/4P3/5K2 b - - 0 1", "e2", liste()),// roi attaqué
                Arguments.of("1k6/8/8/7b/8/8/4P3/3K4 w - - 0 1", "e2", liste()),// roi attaqué
                Arguments.of("k7/8/8/8/3Pp3/8/8/K7 b - d3 0 1", "e4", liste("e3", "d3")), // en passant noir
                Arguments.of("k7/8/8/8/4pP2/8/8/K7 b - f3 0 1", "e4", liste("e3", "f3")) // en passant noir
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestDeplacementPiece")
    void testDeplacementPiece(String plateau, String position, List<String> deplacementsPossible) {

        assertTrue(StringUtils.isNotBlank(plateau));
        assertTrue(StringUtils.isNotBlank(position));

        final Partie partie = notationFEN.createPlateau(plateau);

        final Position positionPieceTeste = Position.getPosition(position);
        assertNotNull(positionPieceTeste);

        var p=partie.getPlateau().getCase(positionPieceTeste);
        assertNotNull(p);

        final List<Position> listeDeplacementPossible = getListePosition(deplacementsPossible);

        // methode testée
        final var res = calculMouvementSimpleService.calcul(partie.getPlateau(), partie.getJoueurCourant(), partie.getConfigurationPartie());

        // vérifications
        LOGGER.info("res={}", res);

        var map = res.getMapMouvements();

        var liste = getListeMouvements(positionPieceTeste, map);

        var plus = diff(liste, listeDeplacementPossible);
        assertTrue(plus.isEmpty(), () -> "plus=" + plus+" ("+positionPieceTeste+"):\n"+ getPlateau(partie));

        var moins = diff(listeDeplacementPossible, liste);
        assertTrue(moins.isEmpty(), () -> "moins=" + moins+" ("+positionPieceTeste+"):\n"+ getPlateau(partie));
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

        return calculPerf(partie.getPlateau(), partie.getJoueurCourant(), depth, partie.getConfigurationPartie());
    }

    private long calculPerf(Plateau plateau, Couleur joueurCourant, int depth, ConfigurationPartie configurationPartie) {
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
                            ConfigurationPartie configurationPartie2 = updateConfiguration(configurationPartie, tmp, tmp2);
                            //partie2.setMove(tmp.getKey().getPosition(), tmp2.getPosition());
                            //assertEquals(calculMouvementBisService.joueurAdversaire(joueurCourant), partie2.getJoueurCourant());
                            resultat += calculPerf(plateau2, calculMouvementSimpleService.joueurAdversaire(joueurCourant), depth - 1, configurationPartie2);
//                            }
                        }
                    }
                }
            }
        }
        return resultat;
    }

    private ConfigurationPartie updateConfiguration(ConfigurationPartie configurationPartie, Map.Entry<PieceCouleurPosition, List<IMouvement>> tmp, IMouvement tmp2) {
        ConfigurationPartie configurationPartie2=new ConfigurationPartie(configurationPartie);
        configurationPartie2.setPriseEnPassant(Optional.empty());
        if(tmp2 instanceof MouvementRoque){
            if(tmp.getKey().getCouleur()== Couleur.Blanc) {
                configurationPartie2.setRoqueNoirRoi(false);
                configurationPartie2.setRoqueBlancDame(false);
            } else {
                configurationPartie2.setRoqueNoirRoi(false);
                configurationPartie2.setRoqueNoirDame(false);
            }
        } else if(tmp2 instanceof MouvementSimple){
            if(tmp.getKey().getPiece()==Piece.ROI){
                configurationPartie2.setRoqueNoirRoi(false);
                configurationPartie2.setRoqueNoirDame(false);
            }
        } else if(tmp2 instanceof MouvementEnPassant){
            Optional<Position> position;
            if(tmp.getKey().getCouleur()== Couleur.Blanc) {
                position= PositionTools.getPosition(tmp2.getPositionDestination(),-1,0);
            } else {
                position= PositionTools.getPosition(tmp2.getPositionDestination(),1,0);
            }
            configurationPartie2.setPriseEnPassant(position);
        }
        return configurationPartie2;
    }

    private static List<String> liste(String... liste) {
        if (liste == null) {
            return Lists.newArrayList();
        } else {
            return Lists.newArrayList(liste);
        }
    }

    private <T> Set<T> diff(Collection<T> s, Collection<T> s2) {
        var res = new HashSet<T>();
        res.addAll(s);
        res.removeAll(s2);
        return res;
    }

    private List<Position> getListeMouvements(Position positionPieceTeste, Map<PieceCouleurPosition, List<IMouvement>> map) {
        return map.entrySet().stream()
                .filter(x -> x.getKey().getPosition().equals(positionPieceTeste))
                .map(x -> x.getValue())
                .flatMap(x -> x.stream())
                .map(x -> x.getPositionDestination())
                .collect(toList());
    }

    private List<Position> getListePosition(List<String> deplacementsPossible) {
        final List<Position> listeDeplacementPossible = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deplacementsPossible)) {
            for (var s : deplacementsPossible) {
                Position p = Position.getPosition(s);
                assertNotNull(p);
                assertFalse(listeDeplacementPossible.contains(p));
                listeDeplacementPossible.add(p);
            }
        }
        return listeDeplacementPossible;
    }

    private String getPlateau(Partie partie){
        return getPlateau(partie.getPlateau());
    }

    private String getPlateau(Plateau plateau){
        return plateau.getRepresentation2().replaceAll(" ","_");
    }
}