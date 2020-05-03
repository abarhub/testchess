package org.chess.web.domain;

import org.chess.core.domain.ConfigurationPartie;
import org.chess.core.domain.Couleur;
import org.chess.core.domain.Partie;

public class Game {

    private Partie partie;

    public Game(Partie partie) {
        this.partie = partie;
    }

    public Partie getPartie() {
        return partie;
    }

    public Couleur getJoueur() {
        return partie.getJoueurCourant();
    }

    public ConfigurationPartie getConfigurationPartie() {
        return partie.getConfigurationPartie();
    }
}
