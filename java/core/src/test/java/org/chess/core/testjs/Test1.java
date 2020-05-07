package org.chess.core.testjs;

import org.apache.commons.collections4.CollectionUtils;
import org.chess.core.domain.*;
import org.chess.core.notation.NotationFEN;
import org.chess.core.service.CalculMouvementSimpleService;
import org.chess.core.utils.PlateauTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Test1 {

    public static final Logger LOGGER = LoggerFactory.getLogger(Test1.class);

    private CalculMouvementSimpleService calculMouvementSimpleService;

    private NotationFEN notationFEN = new NotationFEN();

    @BeforeEach
    public void setUp() {
        calculMouvementSimpleService = new CalculMouvementSimpleService();
    }

    @Test
    void test1() throws Exception {

        LOGGER.info("test1");

        ChessJsEngine chessJsEngine=new ChessJsEngine();

        NotationFEN notationFEN = new NotationFEN();

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Partie partie = notationFEN.createPlateau(plateau);

        List<JsonReponse> res = chessJsEngine.getMoves(partie);

        LOGGER.info("res={}", res);
    }

    @Test
    void test2() throws Exception {

        LOGGER.info("test2");

        ChessJsEngine chessJsEngine=new ChessJsEngine();

        NotationFEN notationFEN = new NotationFEN();

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Partie partie = notationFEN.createPlateau(plateau);

        List<JsonReponse> res = chessJsEngine.getMoves2(partie);

        LOGGER.info("res={}", res);
    }

    @Test
    void test3() throws Exception {

        LOGGER.info("test3");

        ChessJsEngine chessJsEngine=new ChessJsEngine();

        NotationFEN notationFEN = new NotationFEN();

        String plateau = "8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 1";
        Partie partie = notationFEN.createPlateau(plateau);

        List<JsonReponse> res = chessJsEngine.getMoves2(partie);

        int nb=0;
        if(res!=null){
            nb=res.size();
        }
        LOGGER.info("nb={}", nb);

        LOGGER.info("res={}", res);
    }

    @Test
    void test4() throws Exception {

        LOGGER.info("test4");

        ChessJsEngine chessJsEngine = new ChessJsEngine();

        NotationFEN notationFEN = new NotationFEN();

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        plateau="rnb2k1r/pp1Pbppp/2p5/q7/2B5/8/PPPQNnPP/RNB1K2R w QK - 3 9";

        Partie partie = notationFEN.createPlateau(plateau);

        int depth=1;

        depth=1;

        LOGGER.info("depth:{}",depth);

        long debut=System.currentTimeMillis();

        long nb=chessJsEngine.calculPerft(partie, depth);

        long fin=System.currentTimeMillis();

        LOGGER.info("nb:{}",nb);

        LOGGER.info("duree: {} ms", fin-debut);
    }

    @Test
    void test5() throws Exception {

        LOGGER.info("test5");

        ChessJsEngine chessJsEngine = new ChessJsEngine();

        NotationFEN notationFEN = new NotationFEN();

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        int depth=1;
        long perftRef=0;

        int no;

        no=1;

        if(no==1) {
            plateau = "rnb2k1r/pp1Pbppp/2p5/q7/2B5/8/PPPQNnPP/RNB1K2R w QK - 3 9";
            depth = 1;
            perftRef = 39;
        }

        Partie partie = notationFEN.createPlateau(plateau);

        LOGGER.info("depth:{}, perftRef:{}",depth, perftRef);

        long debut=System.currentTimeMillis();

        long perftJs=chessJsEngine.calculPerft(partie, depth);

        long fin=System.currentTimeMillis();

        LOGGER.info("perftJs:{}",perftJs);

        LOGGER.info("duree js: {} ms", fin-debut);

        Partie partie2=new Partie(partie);

        debut=System.currentTimeMillis();

        long perftJava=calculPerf(partie2, depth);

        fin=System.currentTimeMillis();

        LOGGER.info("perftJava:{}",perftJava);

        LOGGER.info("duree java: {} ms", fin-debut);

        LOGGER.info("Perft: ref={}, js={}, java={}", perftRef, perftJs, perftJava);

        if(perftRef==perftJs &&perftRef==perftJava){
            LOGGER.info("tout est bon !!!");
        } else if(perftRef==perftJs &&perftRef!=perftJava){
            LOGGER.info("calcul Perft Java invalide");

            Partie partie3=new Partie(partie);
            List<JsonReponse> res = chessJsEngine.getMoves2(partie3);

            Plateau plateau2=partie3.getPlateau();
            Couleur joueurCourant=partie3.getJoueurCourant();
            EtatPartie configurationPartie=partie3.getConfigurationPartie();
            var res2=calculMouvementSimpleService.calcul(plateau2, joueurCourant, configurationPartie);

            LOGGER.info("nb mvt js: {}", res.size());

            LOGGER.info("nb mvt java: {}", res2.getMapMouvements().size());

            compare(res,res2.getMapMouvements());

        } else {
            LOGGER.info("Impossible de continuer");
        }
    }

    private void compare(List<JsonReponse> res, Map<PieceCouleurPosition, List<IMouvement>> res2) {

        Map<PieceCouleurPosition, Set<Position>> mapJs=new HashMap<>();
        Map<PieceCouleurPosition, Set<Position>> mapJava=new HashMap<>();

        if(res!=null){
            for(JsonReponse rep:res){
                var joueur=rep.getColor();
                var posSrc=rep.getPositionSource();
                var posDest=rep.getPositionDestination();
                var piece=rep.getPiece();
                assertNotNull(joueur);
                assertNotNull(posSrc);
                assertNotNull(posDest);
                assertNotNull(piece,()-> "rep="+rep);

                var p=new PieceCouleurPosition(piece,joueur,posSrc);
                if(mapJs.containsKey(p)){
                    mapJs.get(p).add(posDest);
                } else {
                    mapJs.put(p,new HashSet<>());
                    mapJs.get(p).add(posDest);
                }
            }
        }

        if(res2!=null){
            for(var entry:res2.entrySet()){
                var p=entry.getKey();
                var liste=entry.getValue();

                if(!mapJava.containsKey(p)){
                    mapJava.put(p,new HashSet<>());
                }
                for(var m:liste){
                    mapJava.get(p).add(m.getPositionDestination());
                }
            }
        }

        long nbJs=mapJs.values().stream().flatMap(x -> x.stream()).count();
        long nbJava=mapJava.values().stream().flatMap(x -> x.stream()).count();

        LOGGER.info("nbJs={}, nbJava={}", nbJs, nbJava);

        assertEquals(mapJs,mapJava);
    }


    private long calculPerf(Partie partie, int depth) {

        return calculPerf(partie.getPlateau(), partie.getJoueurCourant(), depth, partie.getConfigurationPartie());
    }

    private long calculPerf(Plateau plateau, Couleur joueurCourant, int depth, ConfigurationPartie configurationPartie) {
        long resultat = 0;

        if (depth <= 0) {
            resultat = 1;
        } else {
            var res = calculMouvementSimpleService.calcul(plateau, joueurCourant, configurationPartie);

            var map = res.getMapMouvements();
            assertNotNull(map);
            if (!map.isEmpty()) {
                var iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<PieceCouleurPosition, List<IMouvement>> tmp = iter.next();
                    if (!CollectionUtils.isEmpty(tmp.getValue())) {
                        for (var tmp2 : tmp.getValue()) {
                            Plateau plateau2 = new Plateau(plateau);
                            plateau2.move(tmp.getKey().getPosition(), tmp2);
                            PlateauTools plateauTools=new PlateauTools();
                            ConfigurationPartie configurationPartie2 = plateauTools.updateConfiguration(configurationPartie, tmp.getKey(), tmp2);
                            resultat += calculPerf(plateau2, calculMouvementSimpleService.joueurAdversaire(joueurCourant), depth - 1, configurationPartie2);
                        }
                    }
                }
            }
        }
        return resultat;
    }


}
