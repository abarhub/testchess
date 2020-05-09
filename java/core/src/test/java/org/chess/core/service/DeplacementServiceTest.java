package org.chess.core.service;

import org.apache.commons.collections4.CollectionUtils;
import org.chess.core.domain.Decalage;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeplacementServiceTest {

    private DeplacementService deplacementService=new DeplacementService();

    @Test
    void getDecalageCavalier() {
        // methode testée
        var liste=deplacementService.getDecalageCavalier();

        // vérification
        assertNotNull(liste);
        assertEquals(8,liste.size());
        assertTrue(toutesValeursDifferentes(liste));

        for(var vect:liste){
            assertTrue(Math.abs(vect.getColonne())==1||Math.abs(vect.getColonne())==2);
            assertTrue(Math.abs(vect.getRangee())==1||Math.abs(vect.getRangee())==2);
            assertTrue(Math.abs(vect.getRangee())!=Math.abs(vect.getColonne()));
            assertFalse(vect.getRangee()==0&&Math.abs(vect.getColonne())==0);
        }
    }

    @Test
    void getDecalageInverseCavalier() {
        // methode testée
        var liste=deplacementService.getDecalageCavalier();

        // vérification
        assertNotNull(liste);
        assertEquals(8,liste.size());
        assertTrue(toutesValeursDifferentes(liste));

        for(var vect:liste){
            assertTrue(Math.abs(vect.getColonne())==1||Math.abs(vect.getColonne())==2);
            assertTrue(Math.abs(vect.getRangee())==1||Math.abs(vect.getRangee())==2);
            assertTrue(Math.abs(vect.getRangee())!=Math.abs(vect.getColonne()));
            assertFalse(vect.getRangee()==0&&Math.abs(vect.getColonne())==0);
        }
    }

    @Test
    void getDecalageFou() {
        // methode testée
        var liste=deplacementService.getDecalageFou();

        // vérification
        assertNotNull(liste);
        assertEquals(4,liste.size());
        assertTrue(toutesValeursDifferentes(liste));

        for(var vect:liste){
            assertEquals(1, Math.abs(vect.getColonne()));
            assertEquals(1, Math.abs(vect.getRangee()));
            assertEquals(Math.abs(vect.getRangee()), Math.abs(vect.getColonne()));
            assertFalse(vect.getRangee()==0&&Math.abs(vect.getColonne())==0);
        }
    }

    @Test
    void getDecalageTour() {
        // methode testée
        var liste=deplacementService.getDecalageTour();

        // vérification
        assertNotNull(liste);
        assertEquals(4,liste.size());
        assertTrue(toutesValeursDifferentes(liste));

        for(var vect:liste){
            assertTrue(Math.abs(vect.getColonne())==1||Math.abs(vect.getColonne())==0);
            assertTrue(Math.abs(vect.getRangee())==1||Math.abs(vect.getRangee())==0);
            assertTrue(Math.abs(vect.getRangee())!=Math.abs(vect.getColonne()));
            assertFalse(vect.getRangee()==0&&Math.abs(vect.getColonne())==0);
        }
    }

    @Test
    void getDecalageReine() {
        // methode testée
        var liste=deplacementService.getDecalageReine();

        // vérification
        assertNotNull(liste);
        assertEquals(8,liste.size());
        assertTrue(toutesValeursDifferentes(liste));

        for(var vect:liste){
            assertTrue(Math.abs(vect.getColonne())==1||Math.abs(vect.getColonne())==0);
            assertTrue(Math.abs(vect.getRangee())==1||Math.abs(vect.getRangee())==0);
            assertFalse(vect.getRangee()==0&&Math.abs(vect.getColonne())==0);
        }
        assertTrue(liste.containsAll(deplacementService.getDecalageFou()));
        assertTrue(liste.containsAll(deplacementService.getDecalageTour()));
    }

    @Test
    void getDecalageRoi() {
        // methode testée
        var liste=deplacementService.getDecalageRoi();

        // vérification
        assertNotNull(liste);
        assertEquals(8,liste.size());
        assertTrue(toutesValeursDifferentes(liste));

        for(var vect:liste){
            assertTrue(Math.abs(vect.getColonne())==1||Math.abs(vect.getColonne())==0);
            assertTrue(Math.abs(vect.getRangee())==1||Math.abs(vect.getRangee())==0);
            assertFalse(vect.getRangee()==0&&Math.abs(vect.getColonne())==0);
        }
    }

    // methode testée

    private boolean toutesValeursDifferentes(List<Decalage> liste) {
        if(CollectionUtils.isEmpty(liste)){
            return true;
        } else {
            Set<Decalage> set=new HashSet<>();
            set.addAll(liste);
            return liste.size()==set.size();
        }
    }
}