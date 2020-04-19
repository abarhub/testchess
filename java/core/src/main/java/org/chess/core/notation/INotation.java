package org.chess.core.notation;


import org.chess.core.domain.Partie;

public interface INotation {

	Partie createPlateau(String str);

	String serialize(Partie partie);

}
