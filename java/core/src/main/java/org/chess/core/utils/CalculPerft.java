package org.chess.core.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.apache.commons.collections4.CollectionUtils;
import org.chess.core.domain.*;
import org.chess.core.service.CalculMouvementSimpleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class CalculPerft {

    public static final Logger LOGGER = LoggerFactory.getLogger(CalculPerft.class);

    private CalculMouvementSimpleService calculMouvementSimpleService=new CalculMouvementSimpleService();

    public long calculPerft(Partie partie, int depth){
        Preconditions.checkNotNull(partie);
        ResultatPerft resultatPerft=calculPerft2(partie,depth, false, null, null,null);
        Verify.verifyNotNull(resultatPerft);
        return resultatPerft.getPerft();
    }

    public long calculPerftAfficheNiveau1(Partie partie, int depth){
        Preconditions.checkNotNull(partie);
        ResultatPerft resultatPerft=calculPerft2(partie,depth, true, null, null,null);
        Verify.verifyNotNull(resultatPerft);
        return resultatPerft.getPerft();
    }

    public ResultatPerft calculPerft2(Partie partie, int depth, boolean afficheNiveau, PrintWriter writer, Position logPositionSrc, Position logPositionCible){
        Preconditions.checkNotNull(partie);
        Preconditions.checkState(partie.getConfigurationPartie().getJoueurTrait()==partie.getJoueurCourant());
//        return calculPerf3(partie.getPlateau(), partie.getJoueurCourant(), depth, partie.getConfigurationPartie());
//    }

    //private ResultatPerft calculPerf3(Plateau plateau, Couleur joueurCourant, int depth, ConfigurationPartie configurationPartie) {
        ResultatPerft resultatPerft=new ResultatPerft();
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
//                            Plateau plateau2 = new Plateau(plateau);
//                            plateau2.move(tmp.getKey().getPosition(), tmp2);
//                            PlateauTools plateauTools=new PlateauTools();
//                            ConfigurationPartie configurationPartie2 = plateauTools.updateConfiguration(configurationPartie, tmp.getKey(), tmp2);
                            Partie partie2=new Partie(partie);
                            partie2.mouvement(tmp2);
                            //var res2= calculPerf(plateau2, calculMouvementSimpleService.joueurAdversaire(joueurCourant), depth - 1, configurationPartie2);
                            var res2= calculPerft2(partie2, depth-1, false, null, null, null);
                            resultat+=res2.getPerft();
                            if(writer!=null){
                                writer.println(""+tmp2.getPositionSource()+tmp2.getPositionDestination()+":"+res2.getPerft());
                            }
                            if(logPositionSrc!=null&&logPositionCible!=null){
                                if(logPositionSrc.equals(tmp2.getPositionSource())&&
                                        logPositionCible.equals(tmp2.getPositionDestination())){
                                    LOGGER.info("fen({}): {}",logPositionSrc, partie2.getFen());
                                }
                            }
                            if(afficheNiveau){
                                //LOGGER.info("mvt: {}, perft: {}", tmp2, res2.getPerft());
                                LOGGER.info("mvt: {}{}, perft: {}", tmp2.getPositionSource(),tmp2.getPositionDestination(), res2.getPerft());
                            }
//                            if(depth==2) {
//                                LOGGER.info("depth={}, perft={}, mvt={},total={}", depth, res2.getPerft(),
//                                        "" + tmp2.getPositionSource() + tmp2.getPositionDestination(), resultat);
//                            }
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
