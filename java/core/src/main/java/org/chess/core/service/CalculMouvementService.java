package org.chess.core.service;

import org.chess.core.domain.Couleur;
import org.chess.core.domain.ListeMouvements;
import org.chess.core.domain.Plateau;

public interface CalculMouvementService {

    ListeMouvements calcul(Plateau plateau, Couleur joueurCourant);

}
