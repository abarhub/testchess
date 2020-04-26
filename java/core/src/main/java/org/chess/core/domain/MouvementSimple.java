package org.chess.core.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class MouvementSimple implements IMouvement {

    private final Position positionSource;
    private final Position positionDestination;
    private final boolean attaque;

    public MouvementSimple(Position positionSource, Position positionDestination, boolean attaque) {
        this.positionSource = positionSource;
        this.positionDestination = positionDestination;
        this.attaque = attaque;
    }

    public Position getPositionSource() {
        return positionSource;
    }

    public Position getPositionDestination() {
        return positionDestination;
    }

    public boolean isAttaque() {
        return attaque;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MouvementSimple that = (MouvementSimple) o;
        return attaque == that.attaque &&
                Objects.equals(positionSource, that.positionSource) &&
                Objects.equals(positionDestination, that.positionDestination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionSource, positionDestination, attaque);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MouvementSimple.class.getSimpleName() + "[", "]")
                .add("positionSource=" + positionSource)
                .add("positionDestination=" + positionDestination)
                .add("attaque=" + attaque)
                .toString();
    }
}
