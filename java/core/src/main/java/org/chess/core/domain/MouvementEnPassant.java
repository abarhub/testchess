package org.chess.core.domain;

import com.google.common.base.Preconditions;

public class MouvementEnPassant implements IMouvement {

    private final Position positionSource;
    private final Position positionDestination;
    private final Position pieceAttaquee;
    private final Couleur joueur;

    public MouvementEnPassant(Position positionSource, Position positionDestination, Position pieceAttaquee,Couleur joueur) {
        Preconditions.checkNotNull(positionSource);
        Preconditions.checkNotNull(positionDestination);
        Preconditions.checkNotNull(pieceAttaquee);
        Preconditions.checkNotNull(joueur);
        this.positionSource = positionSource;
        this.positionDestination = positionDestination;
        this.pieceAttaquee = pieceAttaquee;
        this.joueur=joueur;
    }

    @Override
    public Position getPositionSource() {
        return positionSource;
    }

    @Override
    public Position getPositionDestination() {
        return positionDestination;
    }

    @Override
    public boolean isAttaque() {
        return true;
    }

    @Override
    public Piece getPiece() {
        return Piece.PION;
    }

    @Override
    public Couleur getJoueur() {
        return joueur;
    }
}
