package org.chess.core.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class MouvementSimple implements IMouvement {

    private final Position position;
    private final boolean attaque;

    public MouvementSimple(Position position, boolean attaque) {
        this.position = position;
        this.attaque = attaque;
    }

    public Position getPosition() {
        return position;
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
                Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, attaque);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MouvementSimple.class.getSimpleName() + "[", "]")
                .add("position=" + position)
                .add("attaque=" + attaque)
                .toString();
    }
}
