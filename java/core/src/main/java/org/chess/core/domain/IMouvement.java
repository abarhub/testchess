package org.chess.core.domain;

public interface IMouvement {

    Position getPositionDestination();

    boolean isAttaque();

}
