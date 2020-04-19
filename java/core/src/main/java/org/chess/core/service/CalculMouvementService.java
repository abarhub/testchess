package org.chess.core.service;

import org.chess.core.domain.Couleur;
import org.chess.core.domain.ListeMouvements2;
import org.chess.core.domain.Plateau;

public interface CalculMouvementService {

    ListeMouvements2 calcul(Plateau plateau, Couleur joueurCourant);

}
