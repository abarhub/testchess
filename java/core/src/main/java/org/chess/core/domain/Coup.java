package org.chess.core.domain;

public class Coup {

    private final PieceCouleur pieceCouleur;
    private final IMouvement iMouvement;

    public Coup(PieceCouleur pieceCouleur, IMouvement iMouvement) {
        this.pieceCouleur = pieceCouleur;
        this.iMouvement = iMouvement;
    }

    public PieceCouleur getPieceCouleur() {
        return pieceCouleur;
    }

    public IMouvement getiMouvement() {
        return iMouvement;
    }
}
