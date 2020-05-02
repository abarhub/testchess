package org.chess.core.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.chess.core.exception.NotImplementedException;

import java.util.Optional;

public class ConfigurationPartie implements EtatPartie {

    private boolean roqueBlancRoi;
    private boolean roqueBlancDame;
    private boolean roqueNoirRoi;
    private boolean roqueNoirDame;
    private Couleur joueurTrait;
    private int nbDemiCoupSansCapture;
    private int nbCoup;
    private Optional<Position> priseEnPassant;

    public ConfigurationPartie(Couleur joueurTrait) {
        Preconditions.checkNotNull(joueurTrait);
        this.joueurTrait = joueurTrait;
        nbDemiCoupSansCapture = 0;
        nbCoup = 1;
    }

    public ConfigurationPartie(ConfigurationPartie configurationPartie) {
        Preconditions.checkNotNull(configurationPartie);
        roqueBlancRoi = configurationPartie.isRoqueBlancRoi();
        roqueBlancDame = configurationPartie.isRoqueBlancDame();
        roqueNoirRoi = configurationPartie.isRoqueNoirRoi();
        roqueNoirDame = configurationPartie.isRoqueNoirDame();
        joueurTrait = configurationPartie.joueurTrait;
        nbDemiCoupSansCapture = configurationPartie.getNbDemiCoupSansCapture();
        nbCoup = configurationPartie.nbCoup;
        priseEnPassant=configurationPartie.priseEnPassant;
    }

    public boolean isRoqueBlancRoi() {
        return roqueBlancRoi;
    }

    public void setRoqueBlancRoi(boolean roqueBlancRoi) {
        this.roqueBlancRoi = roqueBlancRoi;
    }

    public boolean isRoqueBlancDame() {
        return roqueBlancDame;
    }

    public void setRoqueBlancDame(boolean roqueBlancDame) {
        this.roqueBlancDame = roqueBlancDame;
    }

    public boolean isRoqueNoirRoi() {
        return roqueNoirRoi;
    }

    public void setRoqueNoirRoi(boolean roqueNoirRoi) {
        this.roqueNoirRoi = roqueNoirRoi;
    }

    public boolean isRoqueNoirDame() {
        return roqueNoirDame;
    }

    public void setRoqueNoirDame(boolean roqueNoirDame) {
        this.roqueNoirDame = roqueNoirDame;
    }

    public Couleur getJoueurTrait() {
        return joueurTrait;
    }

    public void setJoueurTrait(Couleur joueurTrait) {
        this.joueurTrait = joueurTrait;
    }

    public int getNbDemiCoupSansCapture() {
        return nbDemiCoupSansCapture;
    }

    public void setNbDemiCoupSansCapture(int nbDemiCoupSansCapture) {
        this.nbDemiCoupSansCapture = nbDemiCoupSansCapture;
    }

    public int getNbCoup() {
        return nbCoup;
    }

    public void setNbCoup(int nbCoup) {
        this.nbCoup = nbCoup;
    }

    public Optional<Position> getPriseEnPassant() {
        return priseEnPassant;
    }

    public void setPriseEnPassant(Optional<Position> priseEnPassant) {
        this.priseEnPassant = priseEnPassant;
    }

    @Override
    public Optional<Position> attaqueEnPassant(Couleur joueur) {
        Verify.verifyNotNull(priseEnPassant);
        return priseEnPassant;
    }

    @Override
    public boolean roquePossible(Couleur joueur, boolean coteRoi) {
        Preconditions.checkNotNull(joueur);
        if(joueur==Couleur.Blanc){
            if(coteRoi){
                return roqueBlancRoi;
            } else {
                return roqueBlancDame;
            }
        } else if(joueur==Couleur.Noir){
            if(coteRoi){
                return roqueNoirRoi;
            } else {
                return roqueNoirDame;
            }
        } else {
            throw new NotImplementedException("joueur invalide:"+joueur);
        }
    }
}
