package org.chess.core.domain;

import java.util.Optional;

public interface EtatPartie {

    Optional<Position> attaqueEnPassant(Couleur joueur);

    boolean roquePossible(Couleur joueur, boolean coteRoi);
}
