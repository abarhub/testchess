package org.chess.core.domain;

public interface IMouvement {

    Position getPositionSource();

    Position getPositionDestination();

    boolean isAttaque();

    Piece getPiece();

    Couleur getJoueur();
}
