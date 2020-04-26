package org.chess.core.domain;

public class MouvementEnPassant implements IMouvement {

    private final Position positionSource;
    private final Position positionDestination;
    private final Position pieceAttaquee;

    public MouvementEnPassant(Position positionSource, Position positionDestination, Position pieceAttaquee) {
        this.positionSource = positionSource;
        this.positionDestination = positionDestination;
        this.pieceAttaquee = pieceAttaquee;
    }

    @Override
    public Position getPositionDestination() {
        return positionDestination;
    }

    @Override
    public boolean isAttaque() {
        return true;
    }

}
