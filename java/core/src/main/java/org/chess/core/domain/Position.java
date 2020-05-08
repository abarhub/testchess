package org.chess.core.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Position {

    private final RangeeEnum rangee;
    private final ColonneEnum colonne;

    public Position(RangeeEnum rangee, ColonneEnum colonne) {
        Verify.verifyNotNull(rangee);
        Verify.verifyNotNull(colonne);
        this.rangee = rangee;
        this.colonne = colonne;
    }

    public RangeeEnum getRangee() {
        return rangee;
    }

    public ColonneEnum getColonne() {
        return colonne;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position2 = (Position) o;
        return rangee == position2.rangee &&
                colonne == position2.colonne;
    }

    @Override
    public int hashCode() {

        return Objects.hash(rangee, colonne);
    }

    @Override
    public String toString() {
        return colonne.getText() + rangee.getText();
    }

    public static Position getPosition(String pos){
        Preconditions.checkState(StringUtils.isNotBlank(pos), "pos=%s", pos);
        Preconditions.checkState(StringUtils.length(pos)==2, "pos=%s", pos);
        char c=pos.charAt(0);
        char c2=pos.charAt(1);
        Preconditions.checkState((c>='a'&&c<='h')||(c>='A'&&c<='H'));
        Preconditions.checkState(c2>='1'&&c2<='8');
        RangeeEnum rangeeEnum=RangeeEnum.get(Character.toLowerCase(c2)-'1'+1);
        Verify.verifyNotNull(rangeeEnum,"pos=%s",pos);
        ColonneEnum colonneEnum=ColonneEnum.get(c-'a'+1);
        Verify.verifyNotNull(colonneEnum,"pos=%s",pos);
        return new Position(rangeeEnum,colonneEnum);
    }
}
