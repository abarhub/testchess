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
        calculMouvementBaseService = new CalculMouvementBaseService();
    }

    @Test
    void getMouvementsRoiMillieux() {

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Partie partie = notationFEN.createPlateau(plateau);

        var roi = getPosition(Piece.ROI, RangeeEnum.RANGEE4, ColonneEnum.COLONNEE, Couleur.Blanc);

        // methode testée
        var res = calculMouvementBaseService.getMouvements(partie.getPlateau(), roi, partie.getConfigurationPartie());

        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups = res.size();
        LOGGER.info("nbCoups={}", nbCoups);
        assertEquals(8, nbCoups);


        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE5, ColonneEnum.COLONNED, false, Piece.ROI, Couleur.Blanc)));
        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE5, ColonneEnum.COLONNEE, false, Piece.ROI, Couleur.Blanc)));
        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE5, ColonneEnum.COLONNEF, false, Piece.ROI, Couleur.Blanc)));
        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE4, ColonneEnum.COLONNED, false, Piece.ROI, Couleur.Blanc)));
        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE4, ColonneEnum.COLONNEF, false, Piece.ROI, Couleur.Blanc)));
        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE3, ColonneEnum.COLONNED, false, Piece.ROI, Couleur.Blanc)));
        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE3, ColonneEnum.COLONNEE, false, Piece.ROI, Couleur.Blanc)));
        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE3, ColonneEnum.COLONNEF, false, Piece.ROI, Couleur.Blanc)));
    }

    @Test
    void getMouvementsRoiMillieuxTour() {

        String plateau = "8/8/8/3PPP2/4K3/8/8/8 w - - 0 1";
        Partie partie = notationFEN.createPlateau(plateau);

        var roi = getPosition(Piece.ROI, RangeeEnum.RANGEE4, ColonneEnum.COLONNEE, Couleur.Blanc);

        // methode testée
        var res = calculMouvementBaseService.getMouvements(partie.getPlateau(), roi, partie.getConfigurationPartie());

        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups = res.size();
        LOGGER.info("nbCoups={}", nbCoups);
        assertEquals(5, nbCoups);

        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE4, ColonneEnum.COLONNED, false, Piece.ROI, Couleur.Blanc)));
        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE4, ColonneEnum.COLONNEF, false, Piece.ROI, Couleur.Blanc)));
        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE3, ColonneEnum.COLONNED, false, Piece.ROI, Couleur.Blanc)));
        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE3, ColonneEnum.COLONNEE, false, Piece.ROI, Couleur.Blanc)));
        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE3, ColonneEnum.COLONNEF, false, Piece.ROI, Couleur.Blanc)));
    }

    @Test
    void getMouvementsRoiRoqueBlanc() {

        String plateau = "8/8/8/8/8/8/3PPP2/R3K2R w KQ - 0 1";
        Partie partie = notationFEN.createPlateau(plateau);

        var roi = getPosition(Piece.ROI, RangeeEnum.RANGEE1, ColonneEnum.COLONNEE, Couleur.Blanc);

        // methode testée
        var res = calculMouvementBaseService.getMouvements(partie.getPlateau(), roi, partie.getConfigurationPartie());

        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups = res.size();
        LOGGER.info("nbCoups={}", nbCoups);
        assertEquals(4, nbCoups);

        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE1, ColonneEnum.COLONNED, false, Piece.ROI, Couleur.Blanc)));
        assertTrue(contient(res, getMouvement(roi.getPosition(), RangeeEnum.RANGEE1, ColonneEnum.COLONNEF, false, Piece.ROI, Couleur.Blanc)));

        assertTrue(contient(res, getMouvementRoque(roi.getPosition(), RangeeEnum.RANGEE1, ColonneEnum.COLONNEG,
                true, ColonneEnum.COLONNEH, ColonneEnum.COLONNEF, Couleur.Blanc)), () -> "res=" + res);

        assertTrue(contient(res, getMouvementRoque(roi.getPosition(), RangeeEnum.RANGEE1, ColonneEnum.COLONNEC,
                false, ColonneEnum.COLONNEA, ColonneEnum.COLONNED, Couleur.Blanc)), () -> "res=" + res);
    }

    // methodes utilitaires

    private PieceCouleurPosition getPosition(Piece piece, RangeeEnum rangeeEnum, ColonneEnum colonneEnum, Couleur couleur) {
        return new PieceCouleurPosition(piece, couleur,
                new Position(rangeeEnum, colonneEnum));
    }

    private boolean contient(List<IMouvement> listeMouvement, IMouvement mouvement) {
        return TestFixture.contient(listeMouvement, mouvement);
    }

    private Position getPosition(RangeeEnum rangeeEnum, ColonneEnum colonneEnum) {
        return TestFixture.getPosition(rangeeEnum, colonneEnum);
    }

    private MouvementSimple getMouvement(Position positionSrc, RangeeEnum rangeeEnum, ColonneEnum colonneEnum,
                                         boolean attaque, Piece piece, Couleur joueur) {
        return TestFixture.getMouvement(positionSrc, rangeeEnum, colonneEnum, attaque, piece, joueur);
    }

    private MouvementRoque getMouvementRoque(Position positionSrc, RangeeEnum rangeeEnum, ColonneEnum colonneRoi, boolean roqueCoteeRoi,
                                             ColonneEnum colonneTourSrc, ColonneEnum colonneTourDest, Couleur joueur) {
        return TestFixture.getMouvementRoque(positionSrc, rangeeEnum, colonneRoi, roqueCoteeRoi, colonneTourSrc, colonneTourDest, joueur);
    }
}