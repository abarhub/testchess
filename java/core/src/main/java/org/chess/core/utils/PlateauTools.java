package org.chess.core.utils;

import org.chess.core.domain.*;

import java.util.Optional;

public class PlateauTools {

    public ConfigurationPartie updateConfiguration(ConfigurationPartie configurationPartie, PieceCouleurPosition piece, IMouvement mouvement) {
        ConfigurationPartie configurationPartie2=new ConfigurationPartie(configurationPartie);
        configurationPartie2.setPriseEnPassant(Optional.empty());
        if(mouvement instanceof MouvementRoque){
            if(piece.getCouleur()== Couleur.Blanc) {
                configurationPartie2.setRoqueNoirRoi(false);
                configurationPartie2.setRoqueBlancDame(false);
            } else {
                configurationPartie2.setRoqueNoirRoi(false);
                configurationPartie2.setRoqueNoirDame(false);
            }
        } else if(mouvement instanceof MouvementSimple){
            if(piece.getPiece()==Piece.ROI){
                configurationPartie2.setRoqueNoirRoi(false);
                configurationPartie2.setRoqueNoirDame(false);
            } else if(piece.getPiece()==Piece.PION){
                if(piece.getCouleur()==Couleur.Blanc){
                    if(mouvement.getPositionSource().getRangee()==RangeeEnum.RANGEE2 &&
                            mouvement.getPositionDestination().getRangee()==RangeeEnum.RANGEE4){
                        Optional<Position> position = PositionTools.getPosition(mouvement.getPositionDestination(), -1, 0);
                        configurationPartie2.setPriseEnPassant(position);
                    }
                } else {
                    if(mouvement.getPositionSource().getRangee()==RangeeEnum.RANGEE7 &&
                            mouvement.getPositionDestination().getRangee()==RangeeEnum.RANGEE5){
                        Optional<Position> position = PositionTools.getPosition(mouvement.getPositionDestination(), 1, 0);
                        configurationPartie2.setPriseEnPassant(position);
                    }
                }
            }
        } else if(mouvement instanceof MouvementEnPassant){
//            Optional<Position> position;
//            if(piece.getCouleur()== Couleur.Blanc) {
//                position= PositionTools.getPosition(mouvement.getPositionDestination(),-1,0);
//            } else {
//                position= PositionTools.getPosition(mouvement.getPositionDestination(),1,0);
//            }
//            configurationPartie2.setPriseEnPassant(position);
        }
        return configurationPartie2;
    }

}
