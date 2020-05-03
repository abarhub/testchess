package org.chess.web.dto;

public class InitChessBoardDTO {

    private int id;
    private String fen;
    private String joueurCourant;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public String getJoueurCourant() {
        return joueurCourant;
    }

    public void setJoueurCourant(String joueurCourant) {
        this.joueurCourant = joueurCourant;
    }
}
