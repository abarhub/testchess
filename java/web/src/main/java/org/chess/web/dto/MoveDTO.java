package org.chess.web.dto;

public class MoveDTO {

    private String positionSource;
    private String positionDestination;
    private String fenResultat;
    private String joueurCourant;

    public String getPositionSource() {
        return positionSource;
    }

    public void setPositionSource(String positionSource) {
        this.positionSource = positionSource;
    }

    public String getPositionDestination() {
        return positionDestination;
    }

    public void setPositionDestination(String positionDestination) {
        this.positionDestination = positionDestination;
    }

    public String getFenResultat() {
        return fenResultat;
    }

    public void setFenResultat(String fenResultat) {
        this.fenResultat = fenResultat;
    }

    public String getJoueurCourant() {
        return joueurCourant;
    }

    public void setJoueurCourant(String joueurCourant) {
        this.joueurCourant = joueurCourant;
    }
}
