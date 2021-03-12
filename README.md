# testchess
test pour générer les déplacements aux échecs


C'est la classe org.chess.core.service.CalculMouvementSimpleService qui permet de générer la liste des déplacements.
La classe org.chess.core.notation.NotationFEN permet de parser un jeux en notation FEN.
La classe org.chess.core.utils.CalculPerft permet de lacluler le Perft pour une certaine profondeur.
Le projet est en Java 11.

```Java
NotationFEN notationFEN = new NotationFEN();
CalculMouvementSimpleService calculMouvementSimpleService=new CalculMouvementSimpleService();

String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
Partie partie = notationFEN.createPlateau(plateau);

ListeMouvements res = calculMouvementSimpleService.calcul(partie.getPlateau(), partie.getJoueurCourant());
```
