package org.chess.core.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.chess.core.exception.NotImplementedException;
import org.chess.core.utils.Check;
import org.chess.core.utils.IteratorPlateau;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.chess.core.utils.IteratorPlateau.getIterablePlateau;


public class Plateau implements IPlateau {

    public static final int NB_LIGNES = 8;
    public static final int NB_COLONNES = 8;

    private PieceCouleur[][] tableau;

    public Plateau() {
        // constructeur vide
    }

    public Plateau(IPlateau plateau) {
        tableau = new PieceCouleur[NB_LIGNES][NB_COLONNES];
        for (Position position : getIterablePlateau()) {
            setTableau(position.getRangee(), position.getColonne(),
                    plateau.getCase(position));
        }
    }

    public Plateau(String str) {
        tableau = new PieceCouleur[NB_LIGNES][NB_COLONNES];

        if (str != null && !str.isEmpty()) {
            int pos = 0;
            while (pos < str.length()) {

                Couleur couleur = null;
                Piece piece = null;
                char c = str.charAt(pos);
                couleur = Couleur.getValue(c);
                Verify.verifyNotNull(couleur);
                char p = str.charAt(pos + 1);
                piece = Piece.getValue(p);
                Verify.verifyNotNull(piece);

                int ligne = str.charAt(pos + 2) - '0';
                Verify.verify(str.charAt(pos + 3) == '.');
                int colonne = str.charAt(pos + 4) - '0';
                Verify.verify(str.charAt(pos + 5) == ';');
                Check.isPositionValide(ligne, colonne);

                PieceCouleur pieceCouleur = new PieceCouleur(piece, couleur);
                Verify.verify(tableau[ligne][colonne] == null);
                tableau[ligne][colonne] = pieceCouleur;

                pos += 6;
            }
        }
    }

    public Plateau(List<PieceCouleurPosition> listePieces) {
        tableau = new PieceCouleur[NB_LIGNES][NB_COLONNES];

        if (listePieces != null && !listePieces.isEmpty()) {
            for (PieceCouleurPosition p : listePieces) {
                Verify.verify(getTableau(p.getPosition().getRangee(),
                        p.getPosition().getColonne()) == null);
                setCase(p.getPosition(), new PieceCouleur(p.getPiece(), p.getCouleur()));
            }
        }
    }

    public void initialise() {
        tableau = new PieceCouleur[NB_LIGNES][NB_COLONNES];

        lignePieces(RangeeEnum.RANGEE8, Couleur.Noir);
        lignePions(RangeeEnum.RANGEE7, Couleur.Noir);


        lignePions(RangeeEnum.RANGEE2, Couleur.Blanc);
        lignePieces(RangeeEnum.RANGEE1, Couleur.Blanc);
    }

    private void lignePieces(RangeeEnum rangee, Couleur couleur) {
        setCase(new Position(rangee, ColonneEnum.COLONNEA), new PieceCouleur(Piece.TOUR, couleur));
        setCase(new Position(rangee, ColonneEnum.COLONNEB), new PieceCouleur(Piece.CAVALIER, couleur));
        setCase(new Position(rangee, ColonneEnum.COLONNEC), new PieceCouleur(Piece.FOU, couleur));
        setCase(new Position(rangee, ColonneEnum.COLONNED), new PieceCouleur(Piece.REINE, couleur));
        setCase(new Position(rangee, ColonneEnum.COLONNEE), new PieceCouleur(Piece.ROI, couleur));
        setCase(new Position(rangee, ColonneEnum.COLONNEF), new PieceCouleur(Piece.FOU, couleur));
        setCase(new Position(rangee, ColonneEnum.COLONNEG), new PieceCouleur(Piece.CAVALIER, couleur));
        setCase(new Position(rangee, ColonneEnum.COLONNEH), new PieceCouleur(Piece.TOUR, couleur));
    }

    private void lignePions(RangeeEnum rangee, Couleur couleur) {
        for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
            setCase(new Position(rangee, colonne), new PieceCouleur(Piece.PION, couleur));
        }
    }

    protected void setCase(Position position, PieceCouleur pieceCouleur) {
        Preconditions.checkNotNull(position);
        setTableau(position.getRangee(), position.getColonne(), pieceCouleur);
    }

    public PieceCouleur getCase(Position position) {
        Preconditions.checkNotNull(position);
        return getTableau(position.getRangee(), position.getColonne());
    }

    public void move(Position positionSrc, Position positionDest) {
        Verify.verifyNotNull(positionSrc);
        Verify.verifyNotNull(positionDest);
        Verify.verify(!positionSrc.equals(positionDest));

        PieceCouleur p = getCase(positionSrc);
        Verify.verifyNotNull(p, "La case source est vide");
        setCase(positionSrc, null);
        setCase(positionDest, p);
    }

    public void move(Position positionSrc, IMouvement mouvement) {
        Preconditions.checkNotNull(mouvement);
        Preconditions.checkNotNull(positionSrc);
        Preconditions.checkState(positionSrc.equals(mouvement.getPositionSource()));
        var pieceAttaque=getCase(mouvement.getPositionDestination());
        if(pieceAttaque!=null){
            Preconditions.checkState(pieceAttaque.getCouleur()!=mouvement.getJoueur());
            Preconditions.checkState(pieceAttaque.getPiece()!=Piece.ROI);
        }
        if (mouvement instanceof MouvementSimple) {
            MouvementSimple mouvementSimple= (MouvementSimple) mouvement;
            Position positionDest = mouvementSimple.getPositionDestination();
            Verify.verifyNotNull(positionDest);
            Verify.verify(!positionSrc.equals(positionDest));

            PieceCouleur p = getCase(positionSrc);
            Verify.verifyNotNull(p, "La case source est vide");
            if(mouvementSimple.getPromotion().isPresent()){
                PieceCouleur p2=new PieceCouleur(mouvementSimple.getPiece(),p.getCouleur());
                setCase(positionSrc, null);
                setCase(positionDest, p2);
            } else {
                setCase(positionSrc, null);
                setCase(positionDest, p);
            }
        } else if (mouvement instanceof MouvementRoque) {
            MouvementRoque mouvementRoque = (MouvementRoque) mouvement;
            Position positionDest = mouvement.getPositionDestination();
            Verify.verifyNotNull(positionDest);
            Verify.verify(!positionSrc.equals(positionDest));
            Verify.verify(positionSrc.getRangee() == positionDest.getRangee());

            Position posTourSrc = mouvementRoque.getPositionTourSrc();
            Position posTourDest = mouvementRoque.getPositionTourDest();

            Verify.verifyNotNull(posTourSrc);
            Verify.verifyNotNull(posTourDest);
            Verify.verify(!posTourSrc.equals(posTourDest));

            Verify.verify(posTourSrc.getRangee() == posTourDest.getRangee());
            Verify.verify(posTourSrc.getRangee() == positionSrc.getRangee());

            final PieceCouleur p = getCase(positionSrc);
            Verify.verifyNotNull(p, "La case source est vide");
            Verify.verify(p.getPiece() == Piece.ROI);
            final Couleur couleurRoi = p.getCouleur();
            PieceCouleur tmp = getCase(positionDest);
            Verify.verify(tmp == null);
            final PieceCouleur pTour = getCase(posTourSrc);
            Verify.verifyNotNull(pTour, "La case source est vide");
            Verify.verify(pTour.getPiece() == Piece.TOUR);
            Verify.verify(pTour.getCouleur() == couleurRoi);
            PieceCouleur tmp2 = getCase(posTourDest);
            Verify.verify(tmp2 == null);

            setCase(positionSrc, null);
            setCase(positionDest, p);

            setCase(posTourSrc, null);
            setCase(posTourDest, pTour);
        } else if(mouvement instanceof MouvementEnPassant) {
            MouvementEnPassant mouvementEnPassant= (MouvementEnPassant) mouvement;
            Position positionDest = mouvement.getPositionDestination();
            final var pieceSource = getCase(positionSrc);
            Verify.verifyNotNull(pieceSource);
            Verify.verify(pieceSource.getPiece()==Piece.PION);
            final var pieceAttaque2 = getCase(mouvementEnPassant.getPieceAttaquee());
            Verify.verifyNotNull(pieceAttaque2);
            Verify.verify(pieceAttaque2.getPiece()==Piece.PION);
            Verify.verify(pieceAttaque2.getCouleur()!=pieceSource.getCouleur());
            Verify.verify(mouvementEnPassant.getPieceAttaquee().getRangee()==positionSrc.getRangee());
            Verify.verify(Math.abs(mouvementEnPassant.getPieceAttaquee().getColonne().getNo()-positionSrc.getColonne().getNo())==1);
            Verify.verify(mouvementEnPassant.getPieceAttaquee().getColonne()==positionDest.getColonne());
            Verify.verify(Math.abs(mouvementEnPassant.getPieceAttaquee().getRangee().getNo()-positionDest.getRangee().getNo())==1);
            var p=new PieceCouleur(mouvementEnPassant.getPiece(),mouvementEnPassant.getJoueur());
            setCase(positionSrc, null);
            setCase(positionDest, p);
            setCase(mouvementEnPassant.getPieceAttaquee(), null);
        } else {
            throw new NotImplementedException("type de mouvement non géré:"+mouvement.getClass());
        }
    }

    public void afficheConsole() {
        for (RangeeEnum rangee : IteratorPlateau.getIterableRangee()) {
            for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
                PieceCouleur p = getCase(new Position(rangee, colonne));
                if (p == null) {
                    System.out.print(' ');
                } else {
                    System.out.print(p.getPiece().getNomCourt());
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public Stream<PieceCouleur> getStream() {
        List<PieceCouleur> list = new ArrayList<>();

        for (Position position : getIterablePlateau()) {
            PieceCouleur p = getCase(position);
            if (p != null) {
                list.add(p);
            }
        }

        return list.stream();
    }

    public Stream<PieceCouleurPosition> getStreamPosition() {
        List<PieceCouleurPosition> list = new ArrayList<>();

        for (Position position : getIterablePlateau()) {
            PieceCouleur p = getCase(position);
            if (p != null) {
                PieceCouleurPosition p2 = new PieceCouleurPosition(p.getPiece(), p.getCouleur(), position);
                list.add(p2);
            }
        }

        return list.stream();
    }

    public String getRepresentation() {
        StringBuilder str = new StringBuilder();

        for (Position position : getIterablePlateau()) {
            PieceCouleur p = getCase(position);
            if (p != null) {
                str.append(p.getCouleur().getNomCourt());
                str.append(p.getPiece().getNomCourt());
                str.append(position.getRangee().getText());
                str.append('.');
                str.append(position.getColonne());
                str.append(';');
            }
        }

        return str.toString();
    }

    public String getRepresentation2() {
        StringBuilder str = new StringBuilder();

        for (RangeeEnum rangee : IteratorPlateau.getIterableRangee()) {
            for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
                PieceCouleur p = getCase(new Position(rangee, colonne));
                if (p == null) {
                    str.append(' ');
                } else {
                    str.append(p.getPiece().getNomCourt());
                }
            }
            str.append('\n');
        }

        return str.toString();
    }

    private void setTableau(RangeeEnum rangee, ColonneEnum colonne, PieceCouleur pieceCouleur) {
        Verify.verifyNotNull(rangee);
        Verify.verifyNotNull(colonne);
        tableau[rangee.getNo() - 1][colonne.getNo() - 1] = pieceCouleur;
    }

    private PieceCouleur getTableau(RangeeEnum rangee, ColonneEnum colonne) {
        Verify.verifyNotNull(rangee);
        Verify.verifyNotNull(colonne);
        return tableau[rangee.getNo() - 1][colonne.getNo() - 1];
    }

}
