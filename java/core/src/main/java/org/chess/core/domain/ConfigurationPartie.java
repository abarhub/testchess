package org.chess.core.domain;

import com.google.common.base.Preconditions;

import java.util.Optional;

public class ConfigurationPartie {

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
}
