package org.chess.core.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class MouvementRoque implements IMouvement {

    private final Position position;
    private final boolean roqueCoteRoi;
    private final Position positionTourSrc;
    private final Position positionTourDest;

    public MouvementRoque(Position position, boolean roqueCoteRoi, Position positionTourSrc, Position positionTourDest) {
        this.position = position;
        this.roqueCoteRoi = roqueCoteRoi;
        this.positionTourSrc = positionTourSrc;
        this.positionTourDest = positionTourDest;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean isAttaque() {
        return false;
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
                Objects.equals(position, that.position) &&
                Objects.equals(positionTourSrc, that.positionTourSrc) &&
                Objects.equals(positionTourDest, that.positionTourDest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, roqueCoteRoi, positionTourSrc, positionTourDest);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MouvementRoque.class.getSimpleName() + "[", "]")
                .add("position=" + position)
                .add("roqueCoteRoi=" + roqueCoteRoi)
                .add("positionTourSrc=" + positionTourSrc)
                .add("positionTourDest=" + positionTourDest)
                .toString();
    }
}
