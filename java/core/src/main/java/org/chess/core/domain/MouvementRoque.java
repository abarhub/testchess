package org.chess.core.domain;

import com.google.common.base.Preconditions;

import java.util.Objects;
import java.util.StringJoiner;

public class MouvementRoque implements IMouvement {

    private final Position positionSource;
    private final Position positionDestination;
    private final boolean roqueCoteRoi;
    private final Position positionTourSrc;
    private final Position positionTourDest;
    private final Couleur joueur;

    public MouvementRoque(Position positionSource, Position positionDestination, boolean roqueCoteRoi,
                          Position positionTourSrc, Position positionTourDest, Couleur joueur) {
        Preconditions.checkNotNull(positionSource);
        Preconditions.checkNotNull(positionDestination);
        Preconditions.checkNotNull(positionTourSrc);
        Preconditions.checkNotNull(positionTourDest);
        Preconditions.checkNotNull(joueur);
        this.positionSource = positionSource;
        this.positionDestination = positionDestination;
        this.roqueCoteRoi = roqueCoteRoi;
        this.positionTourSrc = positionTourSrc;
        this.positionTourDest = positionTourDest;
        this.joueur = joueur;
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
        return false;
    }

    @Override
    public Piece getPiece() {
        return Piece.ROI;
    }

    @Override
    public Couleur getJoueur() {
        return joueur;
    }

    public boolean isRoqueCoteRoi() {
        return roqueCoteRoi;
    }

    public Position getPositionTourSrc() {
        return positionTourSrc;
    }

    public Position getPositionTourDest() {
        return positionTourDest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MouvementRoque that = (MouvementRoque) o;
        return roqueCoteRoi == that.roqueCoteRoi &&
                Objects.equals(positionSource, that.positionSource) &&
                Objects.equals(positionDestination, that.positionDestination) &&
                Objects.equals(positionTourSrc, that.positionTourSrc) &&
                Objects.equals(positionTourDest, that.positionTourDest) &&
                joueur == that.joueur;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionSource, positionDestination, roqueCoteRoi, positionTourSrc, positionTourDest, joueur);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MouvementRoque.class.getSimpleName() + "[", "]")
                .add("positionSource=" + positionSource)
                .add("positionDestination=" + positionDestination)
                .add("roqueCoteRoi=" + roqueCoteRoi)
                .add("positionTourSrc=" + positionTourSrc)
                .add("positionTourDest=" + positionTourDest)
                .add("joueur=" + joueur)
                .toString();
    }
}
