package org.chess.core.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.chess.core.domain.*;
import org.chess.core.utils.PlateauTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CalculMouvementSimpleService extends AbstractCalculMouvementService implements CalculMouvementService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CalculMouvementSimpleService.class);

    private CalculMouvementBaseService calculMouvementBaseService = new CalculMouvementBaseService();

    private StopWatch stopWatch2 = new StopWatch();
    private StopWatch stopWatchListeDeplacement = new StopWatch();
    private StopWatch stopWatchSupprEchecs = new StopWatch();
    private StopWatch stopWatchGenereDeplacement = new StopWatch();

    @Override
    public ListeMouvements2 calcul(Plateau plateau, Couleur joueurCourant) {
        return calcul(plateau,joueurCourant, new HistoriqueCoups());
    }

    public ListeMouvements2 calcul(Plateau plateau, Couleur joueurCourant, EtatPartie etatPartie) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueurCourant);
        Preconditions.checkNotNull(etatPartie);

        //Instant debut= Instant.now();
        //stopWatch.start();
        if (stopWatch2.isStopped()) {
            stopWatch2.start();
        } else {
            stopWatch2.resume();
        }
        ListeMouvements2 resultat;

        // recherche du roi du joueur courant
        Position positionRoi = rechercheRoi(plateau, joueurCourant);

        // vérification si le roi du joueur courant est en echec
        boolean roiEnEchec = roiEnEchecs(plateau, positionRoi, joueurAdversaire(joueurCourant), etatPartie);

        if (roiEnEchec) {
            // si echec, recherche des coup pour stoper echec

            resultat = rechercheMouvementStoperEchecRoi(plateau, joueurCourant, positionRoi, etatPartie);
        } else {
            // si pas echec, recherche des coups possibles
            var listeMouvement = getPieceJoueur(plateau, joueurCourant, etatPartie);

            // pour chaque coup possible, verification si cela met le roi en echecs
            // la mise en echec, ne peut être fait que par tour (ligne, colonne), dame (ligne, colonne diagonale), fou (diagonale)
            suppressionMouvementMiseEnEchecsRoi(plateau, listeMouvement, positionRoi, joueurCourant, etatPartie, false);

            resultat = listeMouvement;
        }

        //dureeTotal.add(Duration.between(debut, Instant.now()));
//        stopWatch.stop();
        //stopWatch2.stop();
        stopWatch2.suspend();
        return resultat;
    }


    private ListeMouvements2 rechercheMouvementStoperEchecRoi(IPlateau plateau, Couleur joueurCourant, Position positionRoi, EtatPartie etatPartie) {
        var listeMouvement = getPieceJoueur(plateau, joueurCourant, etatPartie);

        suppressionMouvementMiseEnEchecsRoi(plateau, listeMouvement, positionRoi, joueurCourant, etatPartie, true);

        return listeMouvement;
    }

    private void suppressionMouvementMiseEnEchecsRoi(IPlateau plateau, ListeMouvements2 listeMouvement,
                                                     Position positionRoi, Couleur joueurCourant, EtatPartie etatPartie,
                                                     boolean enleveAutresMouvements) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(listeMouvement);
        Preconditions.checkNotNull(positionRoi);
        Preconditions.checkNotNull(joueurCourant);

        start(stopWatchSupprEchecs);

        Map<PieceCouleurPosition, List<IMouvement>> map = listeMouvement.getMapMouvements();
        var iter2 = map.entrySet().iterator();
        while (iter2.hasNext()) {
            var tmp = iter2.next();
            if (tmp.getKey().getPiece() == Piece.ROI) {
                supprimeDeplacementRoitAttaque(plateau, joueurCourant, tmp, etatPartie);
            } else {
                if(enleveAutresMouvements) {
                    var iter = tmp.getValue().iterator();
                    Verify.verify(tmp.getKey().getCouleur() == joueurCourant);
                    while (iter.hasNext()) {
                        var mouvement = iter.next();

                        Plateau plateauApresModification = new Plateau((Plateau) plateau);
                        //plateauApresModification.move(tmp.getKey().getPosition(), mouvement.getPositionDestination());
                        plateauApresModification.move(tmp.getKey().getPosition(), mouvement);

                        if (roiAttaqueApresDeplacement(plateauApresModification, positionRoi, joueurAdversaire(joueurCourant), etatPartie)) {
                            iter.remove();
                        }
                    }
                } else {
                    if (mvtAVerifier(tmp.getKey().getPosition(), positionRoi, plateau)) {
                        var iter = tmp.getValue().iterator();
                        Verify.verify(tmp.getKey().getCouleur() == joueurCourant);
                        while (iter.hasNext()) {
                            var mouvement = iter.next();

                            Plateau plateauApresModification = new Plateau((Plateau) plateau);
                            //plateauApresModification.move(tmp.getKey().getPosition(), mouvement.getPositionDestination());
                            plateauApresModification.move(tmp.getKey().getPosition(), mouvement);

                            if (roiAttaqueApresDeplacement(plateauApresModification, positionRoi, joueurAdversaire(joueurCourant), etatPartie)) {
                                iter.remove();
                            }
                        }
                    } else {
                        // le mouvement ne peut pas bloquer l'attaque => on enleve les deplacements
                        if (enleveAutresMouvements) {
                            tmp.getValue().clear();
                        }
                    }
                }
            }
            if (tmp.getValue().isEmpty()) {
                iter2.remove();
            }
        }

        stop(stopWatchSupprEchecs);
    }

    private void supprimeDeplacementRoitAttaque(IPlateau plateau, Couleur joueurCourant,
                                                Map.Entry<PieceCouleurPosition, List<IMouvement>> tmp,
                                                EtatPartie etatPartie) {
        var iter = tmp.getValue().iterator();
        Verify.verify(tmp.getKey().getCouleur() == joueurCourant);
        while (iter.hasNext()) {
            var mouvement = iter.next();
            Plateau plateau2=new Plateau(plateau);
            plateau2.move(tmp.getKey().getPosition(),mouvement);
            if (this.caseAttaquee(plateau2, mouvement.getPositionDestination(),
                    joueurAdversaire(joueurCourant), etatPartie)) {
                iter.remove();
            }
        }
    }

    private boolean mvtAVerifier(Position positionSrc, Position positionRoi, IPlateau plateau) {
        int x, y;
        if (positionSrc.getRangee() == positionRoi.getRangee()) {
            // ils sont sur la même ligne
            int min = Math.min(positionSrc.getColonne().getNo(), positionRoi.getColonne().getNo());
            int max = Math.max(positionSrc.getColonne().getNo(), positionRoi.getColonne().getNo());
            if (min + 1 == max) {
                return true;
            } else {
                for (int i = min + 1; i < max; i++) {
                    PieceCouleur tmp = plateau.getCase(new Position(positionRoi.getRangee(), ColonneEnum.get(i)));
                    if (tmp != null) {
                        return false;
                    }
                }
                // il n'y a pas de piece entre les deux
                return true;
            }
        } else if (positionSrc.getColonne() == positionRoi.getColonne()) {
            // ils sont sur la même colonne
            int min = Math.min(positionSrc.getRangee().getNo(), positionRoi.getRangee().getNo());
            int max = Math.max(positionSrc.getRangee().getNo(), positionRoi.getRangee().getNo());
            if (min + 1 == max) {
                return true;
            } else {
                for (int i = min + 1; i < max; i++) {
                    PieceCouleur tmp = plateau.getCase(new Position(RangeeEnum.get(i), positionRoi.getColonne()));
                    if (tmp != null) {
                        return false;
                    }
                }
                // il n'y a pas de piece entre les deux
                return true;
            }
        }
        x = positionRoi.getRangee().getNo() - positionSrc.getRangee().getNo();
        y = positionRoi.getColonne().getNo() - positionSrc.getColonne().getNo();
        if (Math.abs(x) == Math.abs(y)) {
            // ils sont sur la même diagonale
            return true;
        } else {
            return false;
        }
    }


    private boolean roiAttaqueApresDeplacement(IPlateau plateau, Position positionRoi, Couleur couleurAttaquant, EtatPartie etatPartie) {
        return plateau.getStreamPosition()
                .filter(x -> x.getCouleur() == couleurAttaquant)
                //.filter(x -> x.getPiece() == Piece.FOU || x.getPiece() == Piece.TOUR || x.getPiece() == Piece.REINE ||)
                .map(pos -> calculMouvementBaseService.getMouvements(plateau, pos))
                .flatMap(x -> x.stream())
                .map(x -> x.getPositionDestination())
                .anyMatch(x -> x.equals(positionRoi));
    }

    private Position rechercheRoi(IPlateau plateau, Couleur joueurCourant) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueurCourant);
        List<Position> liste = getPositionsPieces(plateau, joueurCourant, Piece.ROI);
        Verify.verifyNotNull(liste);
        Verify.verify(liste.size() == 1, "liste: size=%s, %s",liste.size(),liste);
        return liste.get(0);
    }

    private boolean roiEnEchecs(IPlateau plateau, Position positionRoi, Couleur joueurAdversaire, EtatPartie etatPartie) {
        return caseAttaquee(plateau, positionRoi, joueurAdversaire, etatPartie);
    }

    public ListeMouvements2 getPieceJoueur(IPlateau plateau, Couleur joueur, EtatPartie etatPartie) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueur);
        Preconditions.checkNotNull(etatPartie);

        //Instant debut= Instant.now();
        start(stopWatchListeDeplacement);
        var res = new ListeMouvements2();

        Map<PieceCouleurPosition, List<IMouvement>> map = new HashMap<>();
        plateau.getStreamPosition()
                .filter(x -> x.getCouleur() == joueur)
                .forEach(pos -> {
                    List<IMouvement> liste2 = ajoute(plateau, pos, etatPartie);
                    if (!CollectionUtils.isEmpty(liste2)) {
                        map.put(pos, liste2);
                    }
                });
        res.setMapMouvements(map);

        //dureeListeMouvement.add(Duration.between(debut, Instant.now()));
        stop(stopWatchListeDeplacement);
        return res;
    }

    private List<IMouvement> ajoute(IPlateau plateau, PieceCouleurPosition pos, EtatPartie etatPartie) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(pos);
        Preconditions.checkNotNull(etatPartie);

        start(stopWatchGenereDeplacement);
        List<IMouvement> liste = calculMouvementBaseService.getMouvements(plateau, pos, etatPartie);

        stop(stopWatchGenereDeplacement);
        return liste;
    }

    public boolean caseAttaquee(IPlateau plateau, Position position, Couleur couleurAttaquant,
                                EtatPartie etatPartie) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(position);
        Preconditions.checkNotNull(couleurAttaquant);

        return plateau.getStreamPosition()
                .filter(x -> x.getCouleur() == couleurAttaquant)
                .map(pos -> calculMouvementBaseService.getMouvements(plateau, pos, etatPartie))
                .flatMap(x -> x.stream())
                .filter(x -> x.getPiece()!=Piece.PION || x.isAttaque())
                .map(x -> x.getPositionDestination())
                .anyMatch(x -> x.equals(position));
    }

    private void start(StopWatch stopWatch) {
        if (stopWatch.isStopped()) {
            stopWatch.start();
        } else {
            stopWatch.resume();
        }
    }

    private void stop(StopWatch stopWatch) {
        stopWatch.suspend();
    }

}
