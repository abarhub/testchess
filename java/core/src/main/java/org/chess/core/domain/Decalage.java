package org.chess.core.domain;

import com.google.common.base.Verify;

import java.util.Objects;
import java.util.StringJoiner;

public class Decalage {

    private final int rangee;
    private final int colonne;

    public Decalage(int rangee, int colonne) {
        this.rangee = rangee;
        this.colonne = colonne;
    }

    public int getRangee() {
        return rangee;
    }

    public int getColonne() {
        return colonne;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Decalage decalage = (Decalage) o;
        return rangee == decalage.rangee &&
                colonne == decalage.colonne;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rangee, colonne);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Decalage.class.getSimpleName() + "[", "]")
                .add("rangee=" + rangee)
                .add("colonne=" + colonne)
                .toString();
    }
}
