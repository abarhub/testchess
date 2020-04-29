package org.chess.core.domain;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ListeMouvements2 {

    private Map<PieceCouleurPosition, List<IMouvement>> mapMouvements;

    public Map<PieceCouleurPosition, List<IMouvement>> getMapMouvements() {
        return mapMouvements;
    }

    public void setMapMouvements(Map<PieceCouleurPosition, List<IMouvement>> mapMouvements) {
        this.mapMouvements = mapMouvements;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ListeMouvements2.class.getSimpleName() + "[", "]")
                .add("mapMouvements=" + mapMouvements)
                .toString();
    }
}
