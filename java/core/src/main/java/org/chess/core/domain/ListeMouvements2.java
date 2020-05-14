package org.chess.core.domain;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ListeMouvements2 {

    private EtatJeux etatJeux;

    private Map<PieceCouleurPosition, List<IMouvement>> mapMouvements;

    public Map<PieceCouleurPosition, List<IMouvement>> getMapMouvements() {
        return mapMouvements;
    }

    public void setMapMouvements(Map<PieceCouleurPosition, List<IMouvement>> mapMouvements) {
        this.mapMouvements = mapMouvements;
    }

    public EtatJeux getEtatJeux() {
        return etatJeux;
    }

    public void setEtatJeux(EtatJeux etatJeux) {
        this.etatJeux = etatJeux;
    }

    public boolean isJeuxFini() {
        return etatJeux == EtatJeux.EchecEtMat || etatJeux == EtatJeux.Pat;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ListeMouvements2.class.getSimpleName() + "[", "]")
                .add("etatJeux=" + etatJeux)
                .add("mapMouvements=" + mapMouvements)
                .toString();
    }
}
