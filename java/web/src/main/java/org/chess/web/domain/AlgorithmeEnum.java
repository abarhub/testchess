package org.chess.web.domain;

public enum AlgorithmeEnum {
    SIMPLE,// selection du premier déplacement
    HAZARD,// selection d'un déplacement au hazard
    HAZARD2,// selection d'un deplacement au hazard avec selection d'une attaque si possible
    HAZARD3,// selection d'un deplacement au hazard avec selection d'une attaque si possible
}
