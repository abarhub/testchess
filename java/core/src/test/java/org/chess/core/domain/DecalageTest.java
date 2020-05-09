package org.chess.core.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DecalageTest {

    @Test
    void getRangee() {
        int rangeeRef = 1;
        int colonneRef = 2;
        Decalage decalage = new Decalage(rangeeRef, colonneRef);

        // methode testée
        int rangee = decalage.getRangee();

        // vérifications
        assertEquals(rangeeRef, rangee);
    }

    @Test
    void getRangee2() {
        int rangeeRef = 6;
        int colonneRef = 4;
        Decalage decalage = new Decalage(rangeeRef, colonneRef);

        // methode testée
        int rangee = decalage.getRangee();

        // vérifications
        assertEquals(rangeeRef, rangee);
    }

    @Test
    void getRangee3() {
        int rangeeRef = -2;
        int colonneRef = -1;
        Decalage decalage = new Decalage(rangeeRef, colonneRef);

        // methode testée
        int rangee = decalage.getRangee();

        // vérifications
        assertEquals(rangeeRef, rangee);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, -3, -1, 0})
    void getRangee4(int rangeeRef) {
        int colonneRef = 2;
        Decalage decalage = new Decalage(rangeeRef, colonneRef);

        // methode testée
        int rangee = decalage.getRangee();

        // vérifications
        assertEquals(rangeeRef, rangee);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, -3, -1, 0, -5})
    void getColonne(int colonneRef) {
        int rangeeRef = 3;
        Decalage decalage = new Decalage(rangeeRef, colonneRef);

        // methode testée
        int colonne = decalage.getColonne();

        // vérifications
        assertEquals(colonneRef, colonne);
    }

    @ParameterizedTest
    @CsvSource({"1,2,3,4,false", "1,1,2,2,false", "0,0,1,0,false",
                "1,1,1,1,true","1,2,1,2,true","1,2,2,1,false",
            "0,0,0,0,true","0,2,0,2,true","-1,3,-1,3,true",
            "-1,5,4,3,false"
    })
    void testEquals(int rangee1, int colonne1, int range2, int colonne2, boolean isEqualsRef) {
        Decalage decalage = new Decalage(rangee1, colonne1);
        Decalage decalage2 = new Decalage(range2, colonne2);

        // methode testée
        boolean isEquals = decalage.equals(decalage2);

        // vérifications
        assertEquals(isEqualsRef,isEquals);
    }
}