package org.chess.web.dto;

import java.util.StringJoiner;

public class ChessBordMvtDTO {
    private String fen;
    private String mouvement;

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public String getMouvement() {
        return mouvement;
    }

    public void setMouvement(String mouvement) {
        this.mouvement = mouvement;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ChessBordMvtDTO.class.getSimpleName() + "[", "]")
                .add("fen='" + fen + "'")
                .add("mouvement='" + mouvement + "'")
                .toString();
    }
}
