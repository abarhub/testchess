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
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

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
        no=2;
        no=3;

        if(no==1) {
            plateau = "rnb2k1r/pp1Pbppp/2p5/q7/2B5/8/PPPQNnPP/RNB1K2R w QK - 3 9";
            depth = 1;
            perftRef = 39;
        } else if(no==2){
            //
            plateau = "r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq - 0 1";
            depth = 1;
            perftRef = 29;
        } else if(no==3){
            //
            plateau = "8/7p/p5pb/4k3/P1pPn3/8/P5PP/1rB2RK1 b - d3 0 1";
            depth = 1;
            perftRef = 4;
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
        } else if((perftRef==perftJs &&perftRef!=perftJava)||perftRef!=perftJava){
            LOGGER.info("calcul Perft Java invalide");

            Partie partie3=new Partie(partie);
            List<JsonReponse> res = chessJsEngine.getMoves2(partie3);

            Plateau plateau2=partie3.getPlateau();
            Couleur joueurCourant=partie3.getJoueurCourant();
            EtatPartie configurationPartie=partie3.getConfigurationPartie();
            var res2=calculMouvementSimpleService.calcul(plateau2, joueurCourant, configurationPartie);

            LOGGER.info("mvt java: {}", res2);

            LOGGER.info("nb mvt js: {}", res.size());

            LOGGER.info("nb mvt java: {}", res2.getMapMouvements().size());

            compare(res,res2.getMapMouvements());

        } else {
            LOGGER.info("Impossible de continuer");
        }
    }

    @Test
    void test6() throws Exception {

        LOGGER.info("test6");

        ChessJsEngine chessJsEngine = new ChessJsEngine();

        NotationFEN notationFEN = new NotationFEN();

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        int depth=1;
        long perftRef=0;

        int no;

        no=1;
        //no=2;
        //no=3;

        if(no==1) {
            plateau = "rnb2k1r/pp1Pbppp/2p5/q7/2B5/8/PPPQNnPP/RNB1K2R w QK - 3 9";
            depth = 2;
            perftRef = 39;
        } else if(no==2){
            //
            plateau = "r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq - 0 1";
            depth = 1;
            perftRef = 29;
        } else if(no==3){
            //
            plateau = "8/7p/p5pb/4k3/P1pPn3/8/P5PP/1rB2RK1 b - d3 0 1";
            depth = 1;
            perftRef = 4;
        }

        Partie partieInitiale = notationFEN.createPlateau(plateau);

        boolean erreur=false;
        int noDepthErreur=-1;

        for(int i=0;i<depth;i++){

            final int noDepth=i+1;

            LOGGER.info("noDepth = {}", noDepth);

            Partie partie =new Partie(partieInitiale);

            long perftJs=chessJsEngine.calculPerft(partie, noDepth);

            Partie partie2=new Partie(partieInitiale);

            long perftJava=calculPerf(partie2, noDepth);

            if(perftJs==perftJava){
                LOGGER.info("pas de différence pour le depth {} : ", noDepth, perftJs);
            } else {
                LOGGER.info("différence pour le depth {}: js={}, java={}, diff={}",
                        noDepth, perftJs, perftJava, perftJava-perftJs);
                erreur=true;
                noDepthErreur=noDepth;
                break;
            }

        }

        if(erreur){
            assertTrue(noDepthErreur>0);

            Partie partie2=new Partie(partieInitiale);

            List<Test02> listeErreur=new ArrayList<>();

            try {
                construitDeplacement(partie2, noDepthErreur, x -> {
                    try {
                        ConfigurationPartie configurationPartie = new ConfigurationPartie(x.configurationPartie);
                        configurationPartie.setJoueurTrait(x.joueurCourant);
                        Partie partie = new Partie(x.plateau, x.joueurCourant, new InformationPartie(), configurationPartie);
                        String fen = notationFEN.serialize(partie);
                        //LOGGER.info("fen={}", fen);

                        verifieJs(partie, fen, listeErreur);

                    } catch(RuntimeException e){
                        throw e;
                    } catch (Exception e) {
                        LOGGER.error("Erreur", e);
                    }
                });
            }catch(RuntimeException e){
                LOGGER.info("Erreur", e);
            }

            LOGGER.info("nb mvt en erreur: {}", listeErreur.size());

            if(!listeErreur.isEmpty()){
                var tmp=listeErreur.get(0);
                LOGGER.info("fen={}",tmp.fen);
                LOGGER.info("mvt en trop={}",tmp.mvtEnTropDansJava);
                LOGGER.info("mvt absent={}",tmp.mvtManquantDansJava);
            }

        }

    }

    private void verifieJs(Partie partie, String fen, List<Test02> listeErreur) throws IOException, InterruptedException {
        var res = calculMouvementSimpleService.calcul(partie.getPlateau(), partie.getJoueurCourant(), partie.getConfigurationPartie());

//        LOGGER.info("res java={}", res);

        Partie partie3=new Partie(partie);

        ChessJsEngine chessJsEngine = new ChessJsEngine();

        List<JsonReponse> res2 = chessJsEngine.getMoves2(partie3);

        //LOGGER.info("res js={}", res2);

        Test02 test02=new Test02();
        test02.fen=fen;

        if(!compare2(res2,res.getMapMouvements(), test02)){
            LOGGER.info("Erreur: différence entre le Java et le JS !!!");
            listeErreur.add(test02);
            throw new RuntimeException("Erreur: fin de traitement");
        }
    }

    private void construitDeplacement(Partie partie, int noDepthErreur, Consumer<Test01> consumer) {
        construitDeplacement(partie.getPlateau(),partie.getJoueurCourant(),
                partie.getConfigurationPartie(), noDepthErreur, consumer);
    }

    private void construitDeplacement(Plateau plateau, Couleur joueurCourant,
                                      ConfigurationPartie configurationPartie,
                                      int noDepthErreur, Consumer<Test01> consumer) {

        if(noDepthErreur<=0){
            Test01 test01=new Test01();
            test01.plateau=plateau;
            test01.joueurCourant=joueurCourant;
            test01.configurationPartie=configurationPartie;
            consumer.accept(test01);
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
                            PlateauTools plateauTools = new PlateauTools();
                            ConfigurationPartie configurationPartie2 = plateauTools.updateConfiguration(configurationPartie, tmp.getKey(), tmp2);
                            construitDeplacement(plateau2,
                                    calculMouvementSimpleService.joueurAdversaire(joueurCourant),
                                    configurationPartie2, noDepthErreur - 1, consumer);
                        }
                    }
                }
            }
        }
    }

    class Test01 {
        Plateau plateau;
        Couleur joueurCourant;
        ConfigurationPartie configurationPartie;

    }

    class Test02 {
        List<IMouvement> mvtEnTropDansJava;
        List<JsonReponse> mvtManquantDansJava;
        String fen;
    }

    private boolean compare2(List<JsonReponse> res, Map<PieceCouleurPosition, List<IMouvement>> res2,
                             Test02 test02) {

        if(res2!=null){

            var resNonTraite=new ArrayList<>(res);

            List<IMouvement> resAbsent=new ArrayList<>();

            for(var entry:res2.entrySet()) {
                var p = entry.getKey();
                var liste = entry.getValue();

                for(var mvt:liste){

                    var iter=resNonTraite.iterator();
                    var trouve=false;
                    while(iter.hasNext()){
                        var tmp=iter.next();

                        if(tmp.getColor()==p.getCouleur() &&tmp.getPiece()==p.getPiece() &&
                                tmp.getPositionSource().equals(mvt.getPositionSource())&&
                                tmp.getPositionDestination().equals(mvt.getPositionDestination())){
                            iter.remove();
                            trouve=true;
                            break;
                        }

                    }

                    if(!trouve){
                        resAbsent.add(mvt);
                    }
                }
            }

            if(!resAbsent.isEmpty()||!resNonTraite.isEmpty()) {
                if(!resAbsent.isEmpty()) {
                    LOGGER.info("nb mvt java non trouve dans json: {}", resAbsent.size());
                }
                if(!resNonTraite.isEmpty()) {
                    LOGGER.info("nb mvt js non trouve dans java: {}", resNonTraite.size());
                }

                if (!resAbsent.isEmpty()) {
                    //LOGGER.info("mvt en trop dans java : {}", resAbsent);
                    test02.mvtEnTropDansJava = resAbsent;
                }
                if (!resNonTraite.isEmpty()) {
                    //LOGGER.info("mvt absent du java : {}", resNonTraite);
                    test02.mvtManquantDansJava = resNonTraite;
                }
            }

            return resAbsent.isEmpty() &&resNonTraite.isEmpty();

        } else {
            return CollectionUtils.isEmpty(res);
        }

    }

    private void compare(List<JsonReponse> res, Map<PieceCouleurPosition, List<IMouvement>> res2) {

        Map<PieceCouleurPosition, Set<Position>> mapJs=new HashMap<>();
        Map<PieceCouleurPosition, Set<Position>> mapJava=new HashMap<>();

        if(res2!=null){

            var resNonTraite=new ArrayList<>(res);

            List<IMouvement> resAbsent=new ArrayList<>();

            for(var entry:res2.entrySet()) {
                var p = entry.getKey();
                var liste = entry.getValue();

                for(var mvt:liste){

                    var iter=resNonTraite.iterator();
                    var trouve=false;
                    while(iter.hasNext()){
                        var tmp=iter.next();

                        if(tmp.getColor()==p.getCouleur() &&tmp.getPiece()==p.getPiece() &&
                                tmp.getPositionSource().equals(mvt.getPositionSource())&&
                                tmp.getPositionDestination().equals(mvt.getPositionDestination())){
                            iter.remove();
                            trouve=true;
                            break;
                        }

                    }

                    if(!trouve){
                        resAbsent.add(mvt);
                    }
                }
            }

            LOGGER.info("nb mvt java non trouve dans json: {}",resAbsent.size());
            LOGGER.info("nb mvt js non trouve dans java: {}",resNonTraite.size());

            if(!resAbsent.isEmpty()){
                LOGGER.info("mvt en trop dans java : {}",resAbsent);
            }
            if(!resNonTraite.isEmpty()){
                LOGGER.info("mvt absent du java : {}",resNonTraite);
            }

            assertAll(
                    ()->assertEquals(0,resAbsent.size(), "en trop dans java"),
                    ()->assertEquals(0,resNonTraite.size(), "absent du java")
            );

        } else {
            assertTrue(CollectionUtils.isEmpty(res));
        }


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
