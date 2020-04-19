package org.chess.core.utils;


import org.chess.core.domain.ColonneEnum;
import org.chess.core.domain.Position;
import org.chess.core.domain.RangeeEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IteratorPlateau {

	public static Iterable<RangeeEnum> getIterableRangee() {
		return Arrays.asList(RangeeEnum.values());
	}

	public static Iterable<RangeeEnum> getIterableRangeeInverse() {
		List<RangeeEnum> liste = Arrays.asList(RangeeEnum.values());
		Collections.reverse(liste);
		return liste;
	}

	public static Iterable<ColonneEnum> getIterableColonne() {
		return Arrays.asList(ColonneEnum.values());
	}

	public static Iterable<ColonneEnum> getIterableColonneInverse() {
		List<ColonneEnum> liste = Arrays.asList(ColonneEnum.values());
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
