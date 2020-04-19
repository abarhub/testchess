package org.chess.core.service;

import org.chess.core.domain.*;
import org.chess.core.notation.NotationFEN;
import org.chess.core.utils.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculMouvementBaseServiceTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(CalculMouvementBaseServiceTest.class);

    private CalculMouvementBaseService calculMouvementBaseService;

    private NotationFEN notationFEN = new NotationFEN();


    @BeforeEach
    void setUp() {
        calculMouvementBaseService=new CalculMouvementBaseService();
    }

    @Test
    void getMouvementsRoiMillieux() {

        String plateau="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Partie partie=notationFEN.createPlateau(plateau);

        // methode testée
        var res = calculMouvementBaseService.getMouvements(partie.getPlateau(),
                getPosition(Piece.ROI,RangeeEnum.RANGEE4, ColonneEnum.COLONNEE));

        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups=res.size();
        LOGGER.info("nbCoups={}", nbCoups);
        assertEquals(8, nbCoups);


        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE5,ColonneEnum.COLONNED,false)));
        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE5,ColonneEnum.COLONNEE,false)));
        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE5,ColonneEnum.COLONNEF,false)));
        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE4,ColonneEnum.COLONNED,false)));
        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE4,ColonneEnum.COLONNEF,false)));
        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE3,ColonneEnum.COLONNED,false)));
        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE3,ColonneEnum.COLONNEE,false)));
        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE3,ColonneEnum.COLONNEF,false)));
    }

    @Test
    void getMouvementsRoiMillieuxTour() {

        String plateau="8/8/8/3PPP2/4K3/8/8/8 w - - 0 1";
        Partie partie=notationFEN.createPlateau(plateau);

        // methode testée
        var res = calculMouvementBaseService.getMouvements(partie.getPlateau(),
                getPosition(Piece.ROI,RangeeEnum.RANGEE4, ColonneEnum.COLONNEE));

        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups=res.size();
        LOGGER.info("nbCoups={}", nbCoups);
        assertEquals(5, nbCoups);

        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE4,ColonneEnum.COLONNED,false)));
        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE4,ColonneEnum.COLONNEF,false)));
        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE3,ColonneEnum.COLONNED,false)));
        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE3,ColonneEnum.COLONNEE,false)));
        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE3,ColonneEnum.COLONNEF,false)));
    }

    @Test
    void getMouvementsRoiRoque() {

        String plateau="8/8/8/8/8/8/3PPP2/R3K2R w KQ - 0 1";
        Partie partie=notationFEN.createPlateau(plateau);

        // methode testée
        var res = calculMouvementBaseService.getMouvements(partie.getPlateau(),
                getPosition(Piece.ROI,RangeeEnum.RANGEE1, ColonneEnum.COLONNEE));

        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups=res.size();
        LOGGER.info("nbCoups={}", nbCoups);
        assertEquals(4, nbCoups);

        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE1,ColonneEnum.COLONNED,false)));
        assertTrue(contient(res,getMouvement(RangeeEnum.RANGEE1,ColonneEnum.COLONNEF,false)));

        assertTrue(contient(res,getMouvementRoque(RangeeEnum.RANGEE1,ColonneEnum.COLONNEG,
                true, ColonneEnum.COLONNEH, ColonneEnum.COLONNEF)), () -> "res="+res);

        assertTrue(contient(res,getMouvementRoque(RangeeEnum.RANGEE1,ColonneEnum.COLONNEC,
                false, ColonneEnum.COLONNEA, ColonneEnum.COLONNED)), () -> "res="+res);
    }

    // methodes utilitaires

    private PieceCouleurPosition getPosition(Piece piece, RangeeEnum rangeeEnum, ColonneEnum colonneEnum){
        return new PieceCouleurPosition(piece,Couleur.Blanc,
                new Position(rangeeEnum, colonneEnum));
    }

    private boolean contient(List<IMouvement> listeMouvement, IMouvement mouvement){
        return TestFixture.contient(listeMouvement,mouvement);
    }

    private Position getPosition(RangeeEnum rangeeEnum, ColonneEnum colonneEnum){
        return TestFixture.getPosition(rangeeEnum,colonneEnum);
    }

    private MouvementSimple getMouvement(RangeeEnum rangeeEnum, ColonneEnum colonneEnum, boolean attaque){
        return TestFixture.getMouvement(rangeeEnum,colonneEnum,attaque);
    }

    private MouvementRoque getMouvementRoque(RangeeEnum rangeeEnum, ColonneEnum colonneRoi, boolean roqueCoteeRoi,
                                             ColonneEnum colonneTourSrc, ColonneEnum colonneTourDest){
        return TestFixture.getMouvementRoque(rangeeEnum,colonneRoi,roqueCoteeRoi, colonneTourSrc, colonneTourDest);
    }
}