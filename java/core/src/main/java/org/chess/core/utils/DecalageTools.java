package org.chess.core.utils;

import org.chess.core.domain.Decalage;

public class DecalageTools {

    public static Decalage decalage(int rangee, int colonne) {
        return new Decalage(rangee, colonne);
    }
}
