package org.chess.core.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import org.chess.core.domain.*;
import org.chess.core.utils.PlateauTools;
import org.chess.core.utils.PositionTools;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CalculMouvementBaseService {

    public List<IMouvement> getMouvements(IPlateau plateau, PieceCouleurPosition piece) {
        return getMouvements(plateau, piece, new HistoriqueCoups());
    }

    public List<IMouvement> getMouvements(IPlateau plateau, PieceCouleurPosition piece, EtatPartie etatPartie) {
        List<IMouvement> list;
        list = null;
        switch (piece.getPiece()) {
            case PION:
                list = calculPion(piece, plateau, etatPartie);
                break;
            case CAVALIER:
                list = calculCavalier(piece, plateau);
                break;
            case FOU:
                list = calculFou(piece, plateau);
                break;
            case TOUR:
                list = calculTour(piece, plateau);
                break;
            case REINE:
                list = calculReine(piece, plateau);
                break;
            case ROI:
                list = calculRoi(piece, plateau, etatPartie);
                break;
        }
        return list;
    }

    private List<IMouvement> calculRoi(PieceCouleurPosition piece, IPlateau plateau, EtatPartie etatPartie) {

        Verify.verifyNotNull(piece);
        Verify.verify(piece.getPiece() == Piece.ROI);

        List<IMouvement> mouvements = new ArrayList<>();

        for (int ligne2 = -1; ligne2 <= 1; ligne2++) {
            for (int colonne2 = -1; colonne2 <= 1; colonne2++) {
                if (!(ligne2 == 0 && colonne2 == 0)) {
                    Optional<Position> optPosition = PositionTools.getPosition(piece.getPosition(), ligne2, colonne2);
                    if (optPosition.isPresent()) {
                        ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
//						if (tousMouvementRois) {
//							ajoutePositionRois(plateau, liste, ligne3, colonne3, joueurCourant);
//						} else if (!caseAttaque(plateau, couleurContraire(piece.getCouleur()), ligne3, colonne3)) {
//							ajoutePositionRois(plateau, liste, ligne3, colonne3, joueurCourant);
//						}
                    }
                }
            }
        }

        ajoutRoqueRoi(piece, plateau, mouvements, etatPartie);

        return mouvements;
    }

    private void ajoutRoqueRoi(PieceCouleurPosition piece, IPlateau plateau, List<IMouvement> mouvements, EtatPartie etatPartie) {
        if (false) {
            ajoutRoqueRoiSansTestAttaque(piece, plateau, mouvements, etatPartie);
        } else {
            ajoutRoqueRoiTestAttaque(piece, plateau, mouvements, etatPartie);
        }
    }

    private void ajoutRoqueRoiSansTestAttaque(PieceCouleurPosition piece, IPlateau plateau, List<IMouvement> mouvements, EtatPartie etatPartie) {
        final RangeeEnum rangeRoi;
        final Couleur couleurRoi = piece.getCouleur();

        if (couleurRoi == Couleur.Blanc) {
            rangeRoi = RangeeEnum.RANGEE1;
        } else {
            rangeRoi = RangeeEnum.RANGEE8;
        }

        if (isPosition(piece, rangeRoi, ColonneEnum.COLONNEE, couleurRoi)) {
            var positionRoi = new Position(rangeRoi, ColonneEnum.COLONNEE);
            if (etatPartie.roquePossible(couleurRoi, true)) {
                // roque coté roi
                Position posTour = new Position(rangeRoi, ColonneEnum.COLONNEH);
                PieceCouleur tour = plateau.getCase(posTour);
                if (tour != null && tour.getPiece() == Piece.TOUR && tour.getCouleur() == couleurRoi) {
                    boolean caseNonVide = false;

                    for (int i = 1; i < 3; i++) {
                        ColonneEnum colonneEnum = ColonneEnum.get(ColonneEnum.COLONNEE.getNo() + i);
                        final Position position = new Position(rangeRoi, colonneEnum);
                        PieceCouleur tmp = plateau.getCase(position);
                        if (tmp != null) {
                            caseNonVide = true;
                            break;
                        }
                    }

                    if (!caseNonVide) {
                        MouvementRoque mouvementRoque = new MouvementRoque(piece.getPosition(), new Position(rangeRoi, ColonneEnum.COLONNEG),
                                true, posTour, new Position(rangeRoi, ColonneEnum.COLONNEF), couleurRoi);
                        mouvements.add(mouvementRoque);
                    }
                }
            }

            if (etatPartie.roquePossible(couleurRoi, false)) {

                // roque cote reine
                Position posTour2 = new Position(rangeRoi, ColonneEnum.COLONNEA);
                PieceCouleur tour2 = plateau.getCase(posTour2);
                if (tour2 != null && tour2.getPiece() == Piece.TOUR && tour2.getCouleur() == couleurRoi) {
                    boolean caseNonVide = false;
                    for (int i = 1; i < 4; i++) {
                        ColonneEnum colonneEnum = ColonneEnum.get(ColonneEnum.COLONNEA.getNo() + i);
                        final Position position = new Position(rangeRoi, colonneEnum);
                        PieceCouleur tmp = plateau.getCase(position);
                        if (tmp != null) {
                            caseNonVide = true;
                            break;
                        }
                    }
                    if (!caseNonVide) {
                        MouvementRoque mouvementRoque = new MouvementRoque(piece.getPosition(), new Position(rangeRoi, ColonneEnum.COLONNEC),
                                false, posTour2, new Position(rangeRoi, ColonneEnum.COLONNED), couleurRoi);
                        mouvements.add(mouvementRoque);
                    }
                }
            }
        }
    }

    private void ajoutRoqueRoiTestAttaque(PieceCouleurPosition piece, IPlateau plateau, List<IMouvement> mouvements, EtatPartie etatPartie) {
        final RangeeEnum rangeRoi;
        final Couleur couleurRoi = piece.getCouleur();

        if (couleurRoi == Couleur.Blanc) {
            rangeRoi = RangeeEnum.RANGEE1;
        } else {
            rangeRoi = RangeeEnum.RANGEE8;
        }

        if (isPosition(piece, rangeRoi, ColonneEnum.COLONNEE, couleurRoi)) {
            var positionRoi = new Position(rangeRoi, ColonneEnum.COLONNEE);
            if (etatPartie.roquePossible(couleurRoi, true)) {
                EtatPartie etatPartie2 = etatPartie;
                if (etatPartie instanceof ConfigurationPartie) {
                    ConfigurationPartie etatPartie3 = new ConfigurationPartie((ConfigurationPartie) etatPartie);
                    etatPartie3.setRoqueNoirRoi(false);
                    etatPartie3.setRoqueNoirDame(false);
                    etatPartie3.setRoqueBlancRoi(false);
                    etatPartie3.setRoqueBlancDame(false);
                    etatPartie2 = etatPartie3;
                }
                if (caseAttaquee(plateau, positionRoi, joueurAdversaire(couleurRoi), etatPartie2)) {
                    // le roi est attaqué => roque impossible
                } else {
                    // roque coté roi
                    Position posTour = new Position(rangeRoi, ColonneEnum.COLONNEH);
                    PieceCouleur tour = plateau.getCase(posTour);
                    if (tour != null && tour.getPiece() == Piece.TOUR && tour.getCouleur() == couleurRoi) {
                        boolean caseNonVide = false;
                        boolean caseAttaque = false;
                        if (caseAttaquee(plateau, posTour, joueurAdversaire(couleurRoi), etatPartie)) {
                            caseAttaque = true;
                        } else {
                            for (int i = 1; i < 3; i++) {
                                ColonneEnum colonneEnum = ColonneEnum.get(ColonneEnum.COLONNEE.getNo() + i);
                                final Position position = new Position(rangeRoi, colonneEnum);
                                PieceCouleur tmp = plateau.getCase(position);
                                if (tmp != null) {
                                    caseNonVide = true;
                                    break;
                                } else if (caseAttaquee(plateau, position, joueurAdversaire(couleurRoi), etatPartie)) {
                                    caseAttaque = true;
                                    break;
                                }
                            }
                        }
                        if (!caseNonVide && !caseAttaque) {
                            MouvementRoque mouvementRoque = new MouvementRoque(piece.getPosition(), new Position(rangeRoi, ColonneEnum.COLONNEG),
                                    true, posTour, new Position(rangeRoi, ColonneEnum.COLONNEF), couleurRoi);
                            mouvements.add(mouvementRoque);
                        }
                    }
                }
            }

            if (etatPartie.roquePossible(couleurRoi, false)) {
                EtatPartie etatPartie2 = etatPartie;
                if (etatPartie instanceof ConfigurationPartie) {
                    ConfigurationPartie etatPartie3 = new ConfigurationPartie((ConfigurationPartie) etatPartie);
                    etatPartie3.setRoqueNoirRoi(false);
                    etatPartie3.setRoqueNoirDame(false);
                    etatPartie3.setRoqueBlancRoi(false);
                    etatPartie3.setRoqueBlancDame(false);
                    etatPartie2 = etatPartie3;
                }
                if (caseAttaquee(plateau, positionRoi, joueurAdversaire(couleurRoi), etatPartie2)) {
                    // le roi est attaqué => roque impossible
                } else {
                    // roque cote reine
                    Position posTour2 = new Position(rangeRoi, ColonneEnum.COLONNEA);
                    PieceCouleur tour2 = plateau.getCase(posTour2);
                    if (tour2 != null && tour2.getPiece() == Piece.TOUR && tour2.getCouleur() == couleurRoi) {
                        boolean caseNonVide = false;
                        boolean caseAttaque = false;
                        for (int i = 1; i < 4; i++) {
                            ColonneEnum colonneEnum = ColonneEnum.get(ColonneEnum.COLONNEA.getNo() + i);
                            final Position position = new Position(rangeRoi, colonneEnum);
                            PieceCouleur tmp = plateau.getCase(position);
                            if (tmp != null) {
                                caseNonVide = true;
                                break;
                            } else if (caseAttaquee(plateau, position, joueurAdversaire(couleurRoi), etatPartie)) {
                                caseAttaque = true;
                                break;
                            }
                        }
                        if (!caseNonVide && !caseAttaque) {
                            MouvementRoque mouvementRoque = new MouvementRoque(piece.getPosition(), new Position(rangeRoi, ColonneEnum.COLONNEC),
                                    false, posTour2, new Position(rangeRoi, ColonneEnum.COLONNED), couleurRoi);
                            mouvements.add(mouvementRoque);
                        }
                    }
                }
            }
        }
    }

    private boolean caseAttaquee(IPlateau plateau, Position position, Couleur couleurAttaquant,
                                 EtatPartie etatPartie) {
        if(true) {
            return caseAttaquee3(plateau,position,couleurAttaquant,etatPartie);
        } else {
            return caseAttaquee2(plateau,position,couleurAttaquant,etatPartie);
        }
    }

    private boolean caseAttaquee3(IPlateau plateau, Position positionTestee, Couleur couleurAttaquant, EtatPartie etatPartie) {

        Position position;
        Optional<Position> positionOpt;
        PieceCouleur piece;

        // test cavalier

        for(var range: Lists.newArrayList(-2,2)) {
            for(var colonne:Lists.newArrayList(-1,1)) {
                if (verifieCaseAttaquee(plateau, positionTestee, couleurAttaquant, range, colonne, Piece.CAVALIER)) {
                    return true;
                }
            }
        }

        for(var range: Lists.newArrayList(-1,1)) {
            for(var colonne:Lists.newArrayList(-2,2)) {
                if (verifieCaseAttaquee(plateau, positionTestee, couleurAttaquant, range, colonne, Piece.CAVALIER)) {
                    return true;
                }
            }
        }

        // test pion
        int decalage;
        if(couleurAttaquant==Couleur.Blanc){
            decalage=1;
        } else {
            decalage=-1;
        }
        if (verifieCaseAttaquee(plateau, positionTestee, couleurAttaquant, decalage, 1, Piece.PION)) {
            return true;
        }
        if (verifieCaseAttaquee(plateau, positionTestee, couleurAttaquant, decalage, -1, Piece.PION)) {
            return true;
        }

        // test du roi
        for(var range=-1;range<=1;range++){
            for(var colonne=-1;colonne<=1;colonne++){
                if(range!=0&&colonne!=0){
                    if (verifieCaseAttaquee(plateau, positionTestee, couleurAttaquant, range, colonne, Piece.ROI)) {
                        return true;
                    }
                }
            }
        }

        // test des colonnes
        for(var colonne=1;colonne<=8;colonne++){
            positionOpt = PositionTools.getPosition(positionTestee, 0, colonne);
            if (positionOpt.isPresent()) {
                piece = plateau.getCase(positionOpt.get());
                if(piece==null) {
                    // case vide => on continue
                } else {
                    if(piece.getCouleur()!=couleurAttaquant){
                        // même couleur => on arrete la boucle
                        break;
                    } else if(piece.getCouleur()==couleurAttaquant) {
                        if (piece.getPiece() == Piece.TOUR ||piece.getPiece() == Piece.REINE) {
                            // la case est attaquee
                            return true;
                        }
                    }
                }
            }
        }

        for(var colonne=1;colonne<=8;colonne++){
            positionOpt = PositionTools.getPosition(positionTestee, 0, -colonne);
            if (positionOpt.isPresent()) {
                piece = plateau.getCase(positionOpt.get());
                if(piece==null) {
                    // case vide => on continue
                } else {
                    if(piece.getCouleur()!=couleurAttaquant){
                        // même couleur => on arrete la boucle
                        break;
                    } else if(piece.getCouleur()==couleurAttaquant) {
                        if (piece.getPiece() == Piece.TOUR ||piece.getPiece() == Piece.REINE) {
                            // la case est attaquee
                            return true;
                        }
                    }
                }
            }
        }

        // test des lignes
        for(var rangee=1;rangee<=8;rangee++){
            positionOpt = PositionTools.getPosition(positionTestee, rangee, 0);
            if (positionOpt.isPresent()) {
                piece = plateau.getCase(positionOpt.get());
                if(piece==null) {
                    // case vide => on continue
                } else {
                    if(piece.getCouleur()!=couleurAttaquant){
                        // même couleur => on arrete la boucle
                        break;
                    } else if(piece.getCouleur()==couleurAttaquant) {
                        if (piece.getPiece() == Piece.TOUR ||piece.getPiece() == Piece.REINE) {
                            // la case est attaquee
                            return true;
                        }
                    }
                }
            }
        }
        for(var rangee=1;rangee<=8;rangee++){
            positionOpt = PositionTools.getPosition(positionTestee, -rangee, 0);
            if (positionOpt.isPresent()) {
                piece = plateau.getCase(positionOpt.get());
                if(piece==null) {
                    // case vide => on continue
                } else {
                    if(piece.getCouleur()!=couleurAttaquant){
                        // même couleur => on arrete la boucle
                        break;
                    } else if(piece.getCouleur()==couleurAttaquant) {
                        if (piece.getPiece() == Piece.TOUR ||piece.getPiece() == Piece.REINE) {
                            // la case est attaquee
                            return true;
                        }
                    }
                }
            }
        }

        // test des diagonales
        for(var rangee=1;rangee<=8;rangee++){
            positionOpt = PositionTools.getPosition(positionTestee, rangee, rangee);
            if (positionOpt.isPresent()) {
                piece = plateau.getCase(positionOpt.get());
                if(piece==null) {
                    // case vide => on continue
                } else {
                    if(piece.getCouleur()!=couleurAttaquant){
                        // même couleur => on arrete la boucle
                        break;
                    } else if(piece.getCouleur()==couleurAttaquant) {
                        if (piece.getPiece() == Piece.FOU ||piece.getPiece() == Piece.REINE) {
                            // la case est attaquee
                            return true;
                        }
                    }
                }
            }
        }
        for(var rangee=1;rangee<=8;rangee++){
            positionOpt = PositionTools.getPosition(positionTestee, rangee, -rangee);
            if (positionOpt.isPresent()) {
                piece = plateau.getCase(positionOpt.get());
                if(piece==null) {
                    // case vide => on continue
                } else {
                    if(piece.getCouleur()!=couleurAttaquant){
                        // même couleur => on arrete la boucle
                        break;
                    } else if(piece.getCouleur()==couleurAttaquant) {
                        if (piece.getPiece() == Piece.FOU ||piece.getPiece() == Piece.REINE) {
                            // la case est attaquee
                            return true;
                        }
                    }
                }
            }
        }
        for(var rangee=1;rangee<=8;rangee++){
            positionOpt = PositionTools.getPosition(positionTestee, -rangee, -rangee);
            if (positionOpt.isPresent()) {
                piece = plateau.getCase(positionOpt.get());
                if(piece==null) {
                    // case vide => on continue
                } else {
                    if(piece.getCouleur()!=couleurAttaquant){
                        // même couleur => on arrete la boucle
                        break;
                    } else if(piece.getCouleur()==couleurAttaquant) {
                        if (piece.getPiece() == Piece.FOU ||piece.getPiece() == Piece.REINE) {
                            // la case est attaquee
                            return true;
                        }
                    }
                }
            }
        }
        for(var rangee=1;rangee<=8;rangee++){
            positionOpt = PositionTools.getPosition(positionTestee, -rangee, rangee);
            if (positionOpt.isPresent()) {
                piece = plateau.getCase(positionOpt.get());
                if(piece==null) {
                    // case vide => on continue
                } else {
                    if(piece.getCouleur()!=couleurAttaquant){
                        // même couleur => on arrete la boucle
                        break;
                    } else if(piece.getCouleur()==couleurAttaquant) {
                        if (piece.getPiece() == Piece.FOU ||piece.getPiece() == Piece.REINE) {
                            // la case est attaquee
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean verifieCaseAttaquee(IPlateau plateau, Position positionTestee, Couleur couleurAttaquant, int range, int colonne, Piece roi) {
        Optional<Position> positionOpt;
        PieceCouleur piece;
        positionOpt = PositionTools.getPosition(positionTestee, range, colonne);
        if (positionOpt.isPresent()) {
            piece = plateau.getCase(positionOpt.get());
            if (piece != null && piece.getCouleur() == couleurAttaquant && piece.getPiece() == roi) {
                return true;
            }
        }
        return false;
    }

    private boolean caseAttaquee2(IPlateau plateau, Position position, Couleur couleurAttaquant,
                                 EtatPartie etatPartie) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(position);
        Preconditions.checkNotNull(couleurAttaquant);

        return plateau.getStreamPosition()
                .filter(x -> x.getCouleur() == couleurAttaquant)
                .map(pos -> getMouvements(plateau, pos, etatPartie))
                .flatMap(x -> x.stream())
                .map(x -> x.getPositionDestination())
                .anyMatch(x -> x.equals(position));
    }

    public Couleur joueurAdversaire(Couleur couleur) {
        Preconditions.checkNotNull(couleur);
        if (couleur == Couleur.Blanc) {
            return Couleur.Noir;
        } else {
            return Couleur.Blanc;
        }
    }

    private boolean isPosition(PieceCouleurPosition piece, RangeeEnum rangeeEnum, ColonneEnum colonneEnum, Couleur couleur) {
        return piece.getPosition().getRangee() == rangeeEnum &&
                piece.getPosition().getColonne() == colonneEnum &&
                piece.getCouleur() == couleur;
    }

    private List<IMouvement> calculReine(PieceCouleurPosition piece, IPlateau plateau) {

        Preconditions.checkNotNull(piece);
        Preconditions.checkState(piece.getPiece() == Piece.REINE);

        List<IMouvement> mouvements = new ArrayList<>();

        for (int j = 0; j < 8; j++) {
            int decalageLigne, decalageColonne;
            if (j == 0) {
                decalageLigne = 1;
                decalageColonne = 0;
            } else if (j == 1) {
                decalageLigne = 0;
                decalageColonne = 1;
            } else if (j == 2) {
                decalageLigne = -1;
                decalageColonne = 0;
            } else if (j == 3) {
                decalageLigne = 0;
                decalageColonne = -1;
            } else if (j == 4) {// diagonales
                decalageLigne = 1;
                decalageColonne = 1;
            } else if (j == 5) {
                decalageLigne = 1;
                decalageColonne = -1;
            } else if (j == 6) {
                decalageLigne = -1;
                decalageColonne = 1;
            } else {
                decalageLigne = -1;
                decalageColonne = -1;
            }
            ajouteDecalage(mouvements, piece.getPosition(), decalageLigne, decalageColonne, piece, plateau);
        }

        return mouvements;
    }

    private List<IMouvement> calculTour(PieceCouleurPosition piece, IPlateau plateau) {

        Preconditions.checkNotNull(piece);
        Preconditions.checkState(piece.getPiece() == Piece.TOUR);

        List<IMouvement> mouvements = new ArrayList<>();

        for (int j = 0; j < 4; j++) {
            int decalageLigne, decalageColonne;
            if (j == 0) {
                decalageLigne = 1;
                decalageColonne = 0;
            } else if (j == 1) {
                decalageLigne = 0;
                decalageColonne = 1;
            } else if (j == 2) {
                decalageLigne = -1;
                decalageColonne = 0;
            } else {
                decalageLigne = 0;
                decalageColonne = -1;
            }
            ajouteDecalage(mouvements, piece.getPosition(), decalageLigne, decalageColonne, piece, plateau);
        }

        return mouvements;
    }

    private List<IMouvement> calculFou(PieceCouleurPosition piece, IPlateau plateau) {
        Preconditions.checkNotNull(piece);
        Preconditions.checkState(piece.getPiece() == Piece.FOU);

        List<IMouvement> mouvements = new ArrayList<>();

        for (int j = 0; j < 4; j++) {
            int decalageLigne, decalageColonne;
            if (j == 0) {
                decalageLigne = 1;
                decalageColonne = 1;
            } else if (j == 1) {
                decalageLigne = 1;
                decalageColonne = -1;
            } else if (j == 2) {
                decalageLigne = -1;
                decalageColonne = 1;
            } else {
                decalageLigne = -1;
                decalageColonne = -1;
            }
            ajouteDecalage(mouvements, piece.getPosition(), decalageLigne, decalageColonne, piece, plateau);
        }

        return mouvements;
    }


    private void ajouteDecalage(List<IMouvement> mouvements, Position position,
                                int decalageLigne, int decalageColonne,
                                PieceCouleurPosition piece, IPlateau plateau) {
        Preconditions.checkNotNull(mouvements);
        Preconditions.checkNotNull(piece);
        Preconditions.checkNotNull(plateau);
        for (int i = 1; i <= 8; i++) {
            boolean res = false;
            Optional<Position> optPosition = PositionTools.getPosition(position, decalageLigne * i, decalageColonne * i);
            if (optPosition.isPresent()) {
                res = ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
            }
            if (!res) {
                break;
            }
        }
    }

    private List<IMouvement> calculCavalier(PieceCouleurPosition piece, IPlateau plateau) {
        Preconditions.checkNotNull(piece);
        Preconditions.checkState(piece.getPiece() == Piece.CAVALIER);

        List<IMouvement> mouvements = new ArrayList<>();

        Optional<Position> optPosition = PositionTools.getPosition(piece.getPosition(), -2, -1);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), -2, 1);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), 1, -2);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), -1, -2);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), 1, 2);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), -1, 2);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), 2, -1);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), 2, 1);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }

        return mouvements;
    }

    private boolean ajoutePositionPiece(List<IMouvement> mouvements, Position position,
                                        PieceCouleurPosition piece, IPlateau plateau) {
        Preconditions.checkNotNull(mouvements);
        Preconditions.checkNotNull(piece);
        Preconditions.checkNotNull(plateau);

        PieceCouleur caseCible = plateau.getCase(position);
        if (caseCible == null) {
            var mouvement = new MouvementSimple(piece.getPosition(), position, false, piece.getPiece(), piece.getCouleur());
            mouvements.add(mouvement);
            return true;
        } else if (caseCible.getCouleur() != piece.getCouleur()) {
            var mouvement = new MouvementSimple(piece.getPosition(), position, true, piece.getPiece(), piece.getCouleur());
            mouvements.add(mouvement);
            return false;
        }

        return false;
    }

    private List<IMouvement> calculPion(PieceCouleurPosition piece, IPlateau plateau, EtatPartie etatPartie) {
        Preconditions.checkNotNull(piece);
        Preconditions.checkState(piece.getPiece() == Piece.PION);

        List<IMouvement> mouvements = new ArrayList<>();

        int decalage, decalage2 = 0;
        if (piece.getCouleur() == Couleur.Blanc) {
            decalage = 1;
            if (piece.getPosition().getRangee() == RangeeEnum.RANGEE2) {
                decalage2 = 2;
            }
        } else {
            decalage = -1;
            if (piece.getPosition().getRangee() == RangeeEnum.RANGEE7) {
                decalage2 = -2;
            }
        }
        // piece avant
        Optional<Position> optPosition = PositionTools.getPosition(piece.getPosition(), decalage, 0);
        if (optPosition.isPresent()) {
            ajoutePositionPions(mouvements, optPosition.get(), piece, plateau, false);
        }
        // piece mange
        optPosition = PositionTools.getPosition(piece.getPosition(), decalage, -1);
        if (optPosition.isPresent()) {
            ajoutePositionPions(mouvements, optPosition.get(), piece, plateau, true);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), decalage, 1);
        if (optPosition.isPresent()) {
            ajoutePositionPions(mouvements, optPosition.get(), piece, plateau, true);
        }
        // piece avant de 2 cases
        if (decalage2 != 0) {
            PieceCouleur caseIntermediaire = null;
            if (decalage2 > 0) {
                optPosition = PositionTools.getPosition(piece.getPosition(), decalage2 - 1, 0);
                if (optPosition.isPresent()) {
                    caseIntermediaire = plateau.getCase(optPosition.get());
                }
            } else {
                optPosition = PositionTools.getPosition(piece.getPosition(), decalage2 + 1, 0);
                if (optPosition.isPresent()) {
                    caseIntermediaire = plateau.getCase(optPosition.get());
                }
            }
            if (caseIntermediaire == null) {
                optPosition = PositionTools.getPosition(piece.getPosition(), decalage2, 0);
                if (optPosition.isPresent()) {
                    ajoutePositionPions(mouvements, optPosition.get(), piece, plateau, false);
                }
            }
        }

        // en passant
        var derniercoupOpt = etatPartie.attaqueEnPassant(piece.getCouleur());
        if (derniercoupOpt.isPresent()) {
            var positionAttaque = derniercoupOpt.get();
            if (piece.getCouleur() == Couleur.Blanc) {
                if (piece.getPosition().getRangee() == RangeeEnum.RANGEE5 && positionAttaque.getRangee() == RangeeEnum.RANGEE6) {
                    ajouteAttaqueEnPassant(piece, plateau, mouvements, positionAttaque);
                }
            } else {
                if (piece.getPosition().getRangee() == RangeeEnum.RANGEE4 && positionAttaque.getRangee() == RangeeEnum.RANGEE3) {
                    ajouteAttaqueEnPassant(piece, plateau, mouvements, positionAttaque);
                }
            }
        }

        return mouvements;
    }

    private void ajouteAttaqueEnPassant(PieceCouleurPosition piece, IPlateau plateau, List<IMouvement> mouvements, Position positionAttaque) {
        var decalage3 = declageColonne(piece.getPosition().getColonne(), positionAttaque.getColonne());
        Verify.verify(decalage3 == 1 || decalage3 == -1);
        var pieceAttaqueOpt = PositionTools.getPosition(piece.getPosition(), 0, decalage3);
        Verify.verify(pieceAttaqueOpt.isPresent());
        var pieceAttaque = pieceAttaqueOpt.get();
        var p = plateau.getCase(pieceAttaque);
        Verify.verifyNotNull(p);
        Verify.verify(p.getCouleur() != piece.getCouleur());
        Verify.verify(p.getPiece() == Piece.PION);
        var p2 = plateau.getCase(positionAttaque);
        Verify.verify(p2 == null);
        var pos = PositionTools.getPosition(piece.getPosition(), (piece.getCouleur() == Couleur.Blanc) ? 1 : -1, decalage3);
        if (pos.isPresent()) {
            Verify.verify(pos.get().equals(positionAttaque));
            var mouvement = new MouvementEnPassant(piece.getPosition(), pos.get(), pieceAttaque, piece.getCouleur());
            mouvements.add(mouvement);
        }
    }

    private int declageColonne(ColonneEnum colonneDepart, ColonneEnum colonneArrivee) {
        return colonneArrivee.getNo() - colonneDepart.getNo();
    }

    private void ajoutePositionPions(List<IMouvement> mouvements, Position position,
                                     PieceCouleurPosition piece, IPlateau plateau, boolean mangePiece) {
        Preconditions.checkNotNull(mouvements);
        Preconditions.checkNotNull(piece);
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(position);
        Preconditions.checkState(piece.getPiece() == Piece.PION);

        if ((piece.getCouleur() == Couleur.Blanc && position.getRangee() == RangeeEnum.RANGEE8) ||
                (piece.getCouleur() == Couleur.Noir && position.getRangee() == RangeeEnum.RANGEE1)) {
            // promotion du pion
            for (Piece p : Piece.values()) {
                if (p != Piece.PION) {
                    PieceCouleur caseCible = plateau.getCase(position);
                    if (mangePiece) {
                        if (caseCible != null && caseCible.getCouleur() != piece.getCouleur()) {
                            var mouvement = new MouvementSimple(piece.getPosition(), position, true, piece.getPiece(), piece.getCouleur(), p);
                            mouvements.add(mouvement);
                        }
                    } else {
                        if (caseCible == null) {
                            var mouvement = new MouvementSimple(piece.getPosition(), position, false, piece.getPiece(), piece.getCouleur(), p);
                            mouvements.add(mouvement);
                        }
                    }
                }
            }
        } else {
            PieceCouleur caseCible = plateau.getCase(position);
            if (mangePiece) {
                if (caseCible != null && caseCible.getCouleur() != piece.getCouleur()) {
                    var mouvement = new MouvementSimple(piece.getPosition(), position, true, piece.getPiece(), piece.getCouleur());
                    mouvements.add(mouvement);
                }
            } else {
                if (caseCible == null) {
                    var mouvement = new MouvementSimple(piece.getPosition(), position, false, piece.getPiece(), piece.getCouleur());
                    mouvements.add(mouvement);
                }
            }
        }
    }

}
