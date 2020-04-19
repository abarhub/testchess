package org.chess.core.service;

import com.google.common.base.Preconditions;
import org.chess.core.domain.*;
import org.chess.core.utils.IteratorPlateau;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AbstractCalculMouvementService {

    public Couleur joueurAdversaire(Couleur couleur) {
        Preconditions.checkNotNull(couleur);
        if (couleur == Couleur.Blanc) {
            return Couleur.Noir;
        } else {
            return Couleur.Blanc;
        }
    }


    public Optional<PieceCouleur> getPiece(IPlateau plateau, Position position) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(position);
        PieceCouleur piece = plateau.getCase(position);
        return Optional.ofNullable(piece);
    }

    public List<Position> getPositionsPieces(IPlateau plateau, Couleur joueur, Piece piece) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueur);
        Preconditions.checkNotNull(piece);
        List<Position> liste = new ArrayList<>();
        for (Position pos : IteratorPlateau.getIterablePlateau()) {
            if (pos != null) {
                Optional<PieceCouleur> pieceOpt = getPiece(plateau, pos);
                if (pieceOpt.isPresent()) {
                    PieceCouleur p = pieceOpt.get();
                    if (p.getCouleur() == joueur && p.getPiece() == piece) {
                        liste.add(pos);
                    }
                }
            }
        }
        return liste;
    }
}
