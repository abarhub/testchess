package org.chess.core.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.apache.commons.collections4.CollectionUtils;
import org.chess.core.domain.IMouvement;
import org.chess.core.domain.Partie;
import org.chess.core.domain.PieceCouleurPosition;
import org.chess.core.domain.Position;
import org.chess.core.service.CalculMouvementSimpleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class CalculPerft {

    public static final Logger LOGGER = LoggerFactory.getLogger(CalculPerft.class);

    private CalculMouvementSimpleService calculMouvementSimpleService = new CalculMouvementSimpleService();

    public long calculPerft(Partie partie, int depth) {
        Preconditions.checkNotNull(partie);
        ResultatPerft resultatPerft = calculPerft(partie, depth, false, null, null, null);
        Verify.verifyNotNull(resultatPerft);
        return resultatPerft.getPerft();
    }

    public ResultatPerft calculPerft(Partie partie, int depth, boolean afficheNiveau, PrintWriter writer, Position logPositionSrc, Position logPositionCible) {
        Preconditions.checkNotNull(partie);
        Preconditions.checkState(partie.getConfigurationPartie().getJoueurTrait() == partie.getJoueurCourant());

        ResultatPerft resultatPerft = new ResultatPerft();
        long resultat = 0;

        if (depth <= 0) {
            resultat = 1;
        } else {
            var res = calculMouvementSimpleService.calcul(partie.getPlateau(), partie.getJoueurCourant(), partie.getConfigurationPartie());

            var map = res.getMapMouvements();
            Verify.verifyNotNull(map);
            if (!map.isEmpty()) {
                var iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<PieceCouleurPosition, List<IMouvement>> tmp = iter.next();
                    if (!CollectionUtils.isEmpty(tmp.getValue())) {
                        for (var tmp2 : tmp.getValue()) {
                            Partie partie2 = new Partie(partie);
                            partie2.mouvement(tmp2);
                            var res2 = calculPerft(partie2, depth - 1, false, null, null, null);
                            resultat += res2.getPerft();
                            if (writer != null) {
                                writer.println("" + tmp2.getPositionSource() + tmp2.getPositionDestination() + ":" + res2.getPerft());
                            }
                            if (logPositionSrc != null && logPositionCible != null) {
                                if (logPositionSrc.equals(tmp2.getPositionSource()) &&
                                        logPositionCible.equals(tmp2.getPositionDestination())) {
                                    LOGGER.info("fen({}): {}", logPositionSrc, partie2.getFen());
                                }
                            }
                            if (afficheNiveau) {
                                LOGGER.info("mvt: {}{}, perft: {}", tmp2.getPositionSource(), tmp2.getPositionDestination(), res2.getPerft());
                            }
                        }
                    }
                }
            }
        }
        resultatPerft.setPerft(resultat);
        Verify.verifyNotNull(resultatPerft);
        return resultatPerft;
    }
}
