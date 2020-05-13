package org.chess.core.utils;

import com.google.common.base.Verify;
import org.apache.commons.collections4.CollectionUtils;
import org.chess.core.domain.*;
import org.chess.core.service.CalculMouvementSimpleService;

import java.util.List;
import java.util.Map;

public class CalculPerft {

    private CalculMouvementSimpleService calculMouvementSimpleService=new CalculMouvementSimpleService();

    public long calculPerft(Partie partie, int depth){
        ResultatPerft resultatPerft=calculPerft2(partie,depth);
        return resultatPerft.getPerft();
    }

    public ResultatPerft calculPerft2(Partie partie, int depth){
        return calculPerf(partie.getPlateau(), partie.getJoueurCourant(), depth, partie.getConfigurationPartie());
    }

    private ResultatPerft calculPerf(Plateau plateau, Couleur joueurCourant, int depth, ConfigurationPartie configurationPartie) {
        ResultatPerft resultatPerft=new ResultatPerft();
        long resultat = 0;

        if (depth <= 0) {
            resultat = 1;
        } else {
            var res = calculMouvementSimpleService.calcul(plateau, joueurCourant, configurationPartie);

            var map = res.getMapMouvements();
            Verify.verifyNotNull(map);
            if (!map.isEmpty()) {
                var iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<PieceCouleurPosition, List<IMouvement>> tmp = iter.next();
                    if (!CollectionUtils.isEmpty(tmp.getValue())) {
                        for (var tmp2 : tmp.getValue()) {
                            Plateau plateau2 = new Plateau(plateau);
                            plateau2.move(tmp.getKey().getPosition(), tmp2);
                            PlateauTools plateauTools=new PlateauTools();
                            ConfigurationPartie configurationPartie2 = plateauTools.updateConfiguration(configurationPartie, tmp.getKey(), tmp2);
                            var res2= calculPerf(plateau2, calculMouvementSimpleService.joueurAdversaire(joueurCourant), depth - 1, configurationPartie2);
                            resultat+=res2.getPerft();
                        }
                    }
                }
            }
        }
        resultatPerft.setPerft(resultat);
        return resultatPerft;
    }
}
