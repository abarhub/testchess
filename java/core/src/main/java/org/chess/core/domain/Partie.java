package org.chess.core.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.ImmutableList;
import org.chess.core.notation.NotationFEN;
import org.chess.core.utils.PlateauTools;


import java.util.ArrayList;
import java.util.List;

public class Partie {

    private final Plateau plateau;
    private final List<DemiCoup> listeCoupsBlancs;
    private final List<DemiCoup> listeCoupsNoirs;
    private final InformationPartie informationPartie;
    private final ConfigurationPartie configurationPartie;
    private Couleur joueurCourant;

    public Partie(Plateau plateau,
                  Couleur joueurCourant, InformationPartie informationPartie, ConfigurationPartie configurationPartie) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueurCourant);
        Preconditions.checkNotNull(informationPartie);
        Preconditions.checkNotNull(configurationPartie);
        Preconditions.checkArgument(joueurCourant == configurationPartie.getJoueurTrait());
        this.plateau = plateau;
        this.joueurCourant = joueurCourant;
        listeCoupsBlancs = new ArrayList<>();
        listeCoupsNoirs = new ArrayList<>();
        this.informationPartie = informationPartie;
        this.configurationPartie = configurationPartie;
    }

    public Partie(Partie partie) {
        this(new Plateau(partie.getPlateau()), partie.getJoueurCourant(),
                partie.informationPartie, new ConfigurationPartie(partie.getConfigurationPartie()));
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Couleur getJoueurCourant() {
        return joueurCourant;
    }

    public List<DemiCoup> getListeCoupsBlancs() {
        if (listeCoupsBlancs == null) {
            return ImmutableList.of();
        } else {
            return ImmutableList.copyOf(listeCoupsBlancs);
        }
    }

    public List<DemiCoup> getListeCoupsNoirs() {
        if (listeCoupsNoirs == null) {
            return ImmutableList.of();
        } else {
            return ImmutableList.copyOf(listeCoupsNoirs);
        }
    }

    public void mouvement(IMouvement mouvement){
        Preconditions.checkNotNull(mouvement);
        Preconditions.checkState(joueurCourant==configurationPartie.getJoueurTrait());
        plateau.move(mouvement.getPositionSource(),mouvement);

        PlateauTools plateauTools=new PlateauTools();
        PieceCouleurPosition pieceCouleurPosition =new PieceCouleurPosition(mouvement.getPiece(),
                mouvement.getJoueur(),mouvement.getPositionSource());
        plateauTools.updateConfiguration(configurationPartie, configurationPartie,pieceCouleurPosition,mouvement);

        if (joueurCourant == Couleur.Blanc) {
            joueurCourant = Couleur.Noir;
        } else {
            joueurCourant = Couleur.Blanc;
        }
        Verify.verify(joueurCourant==configurationPartie.getJoueurTrait());
    }

    public InformationPartie getInformationPartie() {
        return informationPartie;
    }

    public ConfigurationPartie getConfigurationPartie() {
        return configurationPartie;
    }

    public String getFen(){
        NotationFEN notationFEN=new NotationFEN();
        return notationFEN.serialize(this);
    }
}
