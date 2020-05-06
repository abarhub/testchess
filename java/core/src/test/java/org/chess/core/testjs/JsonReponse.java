package org.chess.core.testjs;

import org.chess.core.domain.Couleur;
import org.chess.core.domain.Piece;
import org.chess.core.domain.Position;

import java.util.StringJoiner;

public class JsonReponse {

    private Couleur color;
    private Position positionSource;
    private Position positionDestination;
    private String flag;
    private Piece piece;
    private String san;

    public Couleur getColor() {
        return color;
    }

    public void setColor(Couleur color) {
        this.color = color;
    }

    public Position getPositionSource() {
        return positionSource;
    }

    public void setPositionSource(Position positionSource) {
        this.positionSource = positionSource;
    }

    public Position getPositionDestination() {
        return positionDestination;
    }

    public void setPositionDestination(Position positionDestination) {
        this.positionDestination = positionDestination;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public String getSan() {
        return san;
    }

    public void setSan(String san) {
        this.san = san;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JsonReponse.class.getSimpleName() + "[", "]")
                .add("color=" + color)
                .add("positionSource=" + positionSource)
                .add("positionDestination=" + positionDestination)
                .add("flag='" + flag + "'")
                .add("piece=" + piece)
                .add("san='" + san + "'")
                .toString();
    }
}
