package org.chess.core.domain;

import com.google.common.base.Preconditions;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class MouvementSimple implements IMouvement {

    private final Position positionSource;
    private final Position positionDestination;
    private final boolean attaque;
    private final Piece piece;
    private final Couleur joueur;
    private final Optional<Piece> promotion;

    public MouvementSimple(Position positionSource, Position positionDestination, boolean attaque,
                           Piece piece, Couleur joueur) {
        Preconditions.checkNotNull(positionSource);
        Preconditions.checkNotNull(positionDestination);
        Preconditions.checkNotNull(piece);
        Preconditions.checkNotNull(joueur);
        this.positionSource = positionSource;
        this.positionDestination = positionDestination;
        this.attaque = attaque;
        this.piece = piece;
        this.joueur = joueur;
        this.promotion=Optional.empty();
    }

    public MouvementSimple(Position positionSource, Position positionDestination, boolean attaque,
                           Piece piece, Couleur joueur, Piece promotion) {
        Preconditions.checkNotNull(positionSource);
        Preconditions.checkNotNull(positionDestination);
        Preconditions.checkNotNull(piece);
        Preconditions.checkNotNull(joueur);
        Preconditions.checkNotNull(promotion);
        this.positionSource = positionSource;
        this.positionDestination = positionDestination;
        this.attaque = attaque;
        this.piece = piece;
        this.joueur = joueur;
        this.promotion=Optional.of(promotion);
    }

    public Position getPositionSource() {
        return positionSource;
    }

    public Position getPositionDestination() {
        return positionDestination;
    }

    public boolean isAttaque() {
        return attaque;
    }

    @Override
    public Piece getPiece() {
        return piece;
    }

    @Override
    public Couleur getJoueur() {
        return joueur;
    }

    public Optional<Piece> getPromotion() {
        return promotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MouvementSimple that = (MouvementSimple) o;
        return attaque == that.attaque &&
                Objects.equals(positionSource, that.positionSource) &&
                Objects.equals(positionDestination, that.positionDestination) &&
                piece == that.piece &&
                joueur == that.joueur &&
                Objects.equals(promotion, that.promotion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionSource, positionDestination, attaque, piece, joueur, promotion);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MouvementSimple.class.getSimpleName() + "[", "]")
                .add("positionSource=" + positionSource)
                .add("positionDestination=" + positionDestination)
                .add("attaque=" + attaque)
                .add("piece=" + piece)
                .add("joueur=" + joueur)
                .add("promotion=" + promotion)
                .toString();
    }
}
