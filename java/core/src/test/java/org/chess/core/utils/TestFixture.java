package org.chess.core.utils;

import org.chess.core.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestFixture {

    public static boolean contient(List<IMouvement> listeMouvement, IMouvement mouvement){
        assertNotNull(listeMouvement);
        assertNotNull(mouvement);
        return listeMouvement.contains(mouvement);
    }

    public static Position getPosition(RangeeEnum rangeeEnum, ColonneEnum colonneEnum){
        return new Position(rangeeEnum,colonneEnum);
    }

    public static MouvementSimple getMouvement(Position positionSource, RangeeEnum rangeeEnum, ColonneEnum colonneEnum, boolean attaque){
        return new MouvementSimple(positionSource, getPosition(rangeeEnum,colonneEnum),attaque);
    }

    public static MouvementRoque getMouvementRoque(Position positionSource, RangeeEnum rangeeEnum, ColonneEnum colonneRoi,
                                                    boolean roqueCoteRoi, ColonneEnum colonneTourSrc,
                                                    ColonneEnum colonneTourDest){
        return new MouvementRoque(positionSource, getPosition(rangeeEnum,colonneRoi),roqueCoteRoi,
                getPosition(rangeeEnum,colonneTourSrc),getPosition(rangeeEnum,colonneTourDest));
    }
}
