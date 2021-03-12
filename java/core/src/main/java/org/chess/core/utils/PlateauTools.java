package org.chess.core.utils;

import org.chess.core.domain.*;

import java.util.Optional;

public class PlateauTools {

    public ConfigurationPartie createConfiguration(ConfigurationPartie configurationPartie,
                                                   PieceCouleurPosition piece, IMouvement mouvement) {
        ConfigurationPartie configurationPartie2 = new ConfigurationPartie(configurationPartie);
        updateConfiguration(configurationPartie2, configurationPartie, piece, mouvement);
        return configurationPartie2;
    }

    public void updateConfiguration(ConfigurationPartie configurationPartieResultat,
                                    ConfigurationPartie configurationPartieInitiale, PieceCouleurPosition piece, IMouvement mouvement) {
        configurationPartieResultat.setPriseEnPassant(Optional.empty());
        if (configurationPartieInitiale.getJoueurTrait() == Couleur.Blanc) {
            configurationPartieResultat.setJoueurTrait(Couleur.Noir);
        } else {
            configurationPartieResultat.setJoueurTrait(Couleur.Blanc);
        }
        if (mouvement instanceof MouvementRoque) {
            if (piece.getCouleur() == Couleur.Blanc) {
                configurationPartieResultat.setRoqueBlancRoi(false);
                configurationPartieResultat.setRoqueBlancDame(false);
            } else {
                configurationPartieResultat.setRoqueNoirRoi(false);
                configurationPartieResultat.setRoqueNoirDame(false);
            }
        } else if (mouvement instanceof MouvementSimple) {
            if (piece.getPiece() == Piece.ROI) {
                if (piece.getCouleur() == Couleur.Blanc) {
                    configurationPartieResultat.setRoqueBlancRoi(false);
                    configurationPartieResultat.setRoqueBlancDame(false);
                } else {
                    configurationPartieResultat.setRoqueNoirRoi(false);
                    configurationPartieResultat.setRoqueNoirDame(false);
                }
            } else if (piece.getPiece() == Piece.PION) {
                if (piece.getCouleur() == Couleur.Blanc) {
                    if (mouvement.getPositionSource().getRangee() == RangeeEnum.RANGEE2 &&
                            mouvement.getPositionDestination().getRangee() == RangeeEnum.RANGEE4) {
                        Optional<Position> position = PositionTools.getPosition(mouvement.getPositionDestination(), -1, 0);
                        configurationPartieResultat.setPriseEnPassant(position);
                    }
                } else {
                    if (mouvement.getPositionSource().getRangee() == RangeeEnum.RANGEE7 &&
                            mouvement.getPositionDestination().getRangee() == RangeeEnum.RANGEE5) {
                        Optional<Position> position = PositionTools.getPosition(mouvement.getPositionDestination(), 1, 0);
                        configurationPartieResultat.setPriseEnPassant(position);
                    }
                }
            }
        } else if (mouvement instanceof MouvementEnPassant) {
        }
    }

}
