package org.chess.core.utils;


import com.google.common.collect.Lists;
import org.chess.core.domain.ColonneEnum;
import org.chess.core.domain.Position;
import org.chess.core.domain.RangeeEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IteratorPlateau {

    private static final List<RangeeEnum> LISTE_RANGEE = Arrays.asList(RangeeEnum.values());

    private static final List<ColonneEnum> LISTE_COLONNES = Arrays.asList(ColonneEnum.values());

    public static Iterable<RangeeEnum> getIterableRangee() {
        return Lists.newArrayList(LISTE_RANGEE);
    }

    public static Iterable<RangeeEnum> getIterableRangeeInverse() {
        List<RangeeEnum> liste = Lists.newArrayList(LISTE_RANGEE);
        Collections.reverse(liste);
        return liste;
    }

    public static Iterable<ColonneEnum> getIterableColonne() {
        return Lists.newArrayList(LISTE_COLONNES);
    }

    public static Iterable<ColonneEnum> getIterableColonneInverse() {
        List<ColonneEnum> liste = Lists.newArrayList(LISTE_COLONNES);
        Collections.reverse(liste);
        return liste;
    }

    public static Iterable<Position> getIterablePlateau() {
        List<Position> liste = new ArrayList<>();
        for (RangeeEnum range : getIterableRangeeInverse()) {
            for (ColonneEnum colonne : getIterableColonne()) {
                liste.add(new Position(range, colonne));
            }
        }
        return liste;
    }
}
