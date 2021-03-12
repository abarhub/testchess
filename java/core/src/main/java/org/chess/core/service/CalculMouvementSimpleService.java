package org.chess.core.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.chess.core.domain.*;
import org.chess.core.utils.IteratorPlateau;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CalculMouvementSimpleService implements CalculMouvementService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CalculMouvementSimpleService.class);

    private CalculMouvementBaseService calculMouvementBaseService = new CalculMouvementBaseService();

    private StopWatch stopWatch2 = new StopWatch();
    private StopWatch stopWatchListeDeplacement = new StopWatch();
    private StopWatch stopWatchSupprEchecs = new StopWatch();
    private StopWatch stopWatchGenereDeplacement = new StopWatch();

    @Override
    public ListeMouvements2 calcul(Plateau plateau, Couleur joueurCourant) {
        return calcul(plateau, joueurCourant, new HistoriqueCoups());
    }

    public ListeMouvements2 calcul(Plateau plateau, Couleur joueurCourant, EtatPartie etatPartie) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueurCourant);
        Preconditions.checkNotNull(etatPartie);

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
            // si echec, recherche des coup pour stopper echec

            resultat = rechercheMouvementStoperEchecRoi(plateau, joueurCourant, positionRoi, etatPartie);
        } else {
            // si pas echec, recherche des coups possibles
            var listeMouvement = getPieceJoueur(plateau, joueurCourant, etatPartie);

            // pour chaque coup possible, verification si cela met le roi en echecs
            // la mise en echec, ne peut être fait que par tour (ligne, colonne), dame (ligne, colonne diagonale), fou (diagonale)
            suppressionMouvementMiseEnEchecsRoi(plateau, listeMouvement, positionRoi, joueurCourant, etatPartie, false);

            resultat = listeMouvement;
        }

        calculEtatJeux(resultat, roiEnEchec);

        stopWatch2.suspend();
        return resultat;
    }

    private void calculEtatJeux(ListeMouvements2 resultat, boolean roiEnEchec) {
        if (MapUtils.isEmpty(resultat.getMapMouvements())) {
            if (roiEnEchec) {
                resultat.setEtatJeux(EtatJeux.EchecEtMat);
            } else {
                resultat.setEtatJeux(EtatJeux.Pat);
            }
        } else {
            resultat.setEtatJeux(EtatJeux.EnCours);
        }
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
                if (enleveAutresMouvements) {
                    var iter = tmp.getValue().iterator();
                    Verify.verify(tmp.getKey().getCouleur() == joueurCourant);
                    while (iter.hasNext()) {
                        var mouvement = iter.next();

                        var p = plateau.getCase(mouvement.getPositionDestination());
                        if (p != null && p.getPiece() == Piece.ROI) {
                            iter.remove();
                        } else {
                            Plateau plateauApresModification = new Plateau(plateau);
                            //plateauApresModification.move(tmp.getKey().getPosition(), mouvement.getPositionDestination());
                            plateauApresModification.move(tmp.getKey().getPosition(), mouvement);

                            if (roiAttaqueApresDeplacement(plateauApresModification, positionRoi, joueurAdversaire(joueurCourant), etatPartie)) {
                                iter.remove();
                            }
                        }
                    }
                } else {
                    if (mvtAVerifier(tmp.getKey().getPosition(), positionRoi, plateau, tmp)) {
                        var iter = tmp.getValue().iterator();
                        Verify.verify(tmp.getKey().getCouleur() == joueurCourant);
                        while (iter.hasNext()) {
                            var mouvement = iter.next();

                            var p = plateau.getCase(mouvement.getPositionDestination());
                            if (p != null && p.getPiece() == Piece.ROI) {
                                iter.remove();
                            } else {
                                Plateau plateauApresModification = new Plateau((Plateau) plateau);
                                plateauApresModification.move(tmp.getKey().getPosition(), mouvement);

                                if (roiAttaqueApresDeplacement(plateauApresModification, positionRoi, joueurAdversaire(joueurCourant), etatPartie)) {
                                    iter.remove();
                                }
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
            Plateau plateau2 = new Plateau(plateau);
            plateau2.move(tmp.getKey().getPosition(), mouvement);
            if (this.caseAttaquee(plateau2, mouvement.getPositionDestination(),
                    joueurAdversaire(joueurCourant), etatPartie)) {
                iter.remove();
            }
        }
    }

    private boolean mvtAVerifier(Position positionSrc, Position positionRoi, IPlateau plateau, Map.Entry<PieceCouleurPosition, List<IMouvement>> tmp2) {
        int x, y;
        if (tmp2.getKey().getPiece() == Piece.PION && isMouvementEnPassant(tmp2)) {
            return true;
        } else if (positionSrc.getRangee() == positionRoi.getRangee()) {
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

    private boolean isMouvementEnPassant(Map.Entry<PieceCouleurPosition, List<IMouvement>> tmp2) {
        return tmp2.getValue().stream().anyMatch(x -> x instanceof MouvementEnPassant);
    }

    private boolean roiAttaqueApresDeplacement(IPlateau plateau, Position positionRoi, Couleur couleurAttaquant, EtatPartie etatPartie) {
        return plateau.getStreamPosition()
                .filter(x -> x.getCouleur() == couleurAttaquant)
                .map(pos -> calculMouvementBaseService.getMouvements(plateau, pos, etatPartie))
                .flatMap(x -> x.stream())
                .map(x -> x.getPositionDestination())
                .anyMatch(x -> x.equals(positionRoi));
    }

    private Position rechercheRoi(IPlateau plateau, Couleur joueurCourant) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueurCourant);
        List<Position> liste = getPositionsPieces(plateau, joueurCourant, Piece.ROI);
        Verify.verifyNotNull(liste);
        Verify.verify(liste.size() == 1, "liste: size=%s, %s", liste.size(), liste);
        return liste.get(0);
    }

    private boolean roiEnEchecs(IPlateau plateau, Position positionRoi, Couleur joueurAdversaire, EtatPartie etatPartie) {
        return caseAttaquee(plateau, positionRoi, joueurAdversaire, etatPartie);
    }

    public ListeMouvements2 getPieceJoueur(IPlateau plateau, Couleur joueur, EtatPartie etatPartie) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueur);
        Preconditions.checkNotNull(etatPartie);

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
                .filter(x -> x.getPiece() != Piece.PION || x.isAttaque())
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

    public Couleur joueurAdversaire(Couleur couleur) {
        Preconditions.checkNotNull(couleur);
        if (couleur == Couleur.Blanc) {
            return Couleur.Noir;
        } else {
            return Couleur.Blanc;
        }
    }

    public Optional<PieceCouleur> getPiece(IPlateau plateau, Position position) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(position);
        PieceCouleur piece = plateau.getCase(position);
        return Optional.ofNullable(piece);
    }

    public List<Position> getPositionsPieces(IPlateau plateau, Couleur joueur, Piece piece) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueur);
        Preconditions.checkNotNull(piece);
        List<Position> liste = new ArrayList<>();
        for (Position pos : IteratorPlateau.getIterablePlateau()) {
            if (pos != null) {
                Optional<PieceCouleur> pieceOpt = getPiece(plateau, pos);
                if (pieceOpt.isPresent()) {
                    PieceCouleur p = pieceOpt.get();
                    if (p.getCouleur() == joueur && p.getPiece() == piece) {
                        liste.add(pos);
                    }
                }
            }
        }
        return liste;
    }
}
