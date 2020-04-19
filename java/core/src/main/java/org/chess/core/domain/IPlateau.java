package org.chess.core.domain;

import java.util.stream.Stream;

public interface IPlateau {

    PieceCouleur getCase(Position position);

    Stream<PieceCouleurPosition> getStreamPosition();

    void move(Position positionSrc, Position positionDest);
}
