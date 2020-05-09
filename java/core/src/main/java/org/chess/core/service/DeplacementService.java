package org.chess.core.service;

import com.google.common.collect.ImmutableList;
import org.chess.core.domain.Decalage;
import org.chess.core.utils.DecalageTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeplacementService {

    private final List<Decalage> decalageCavalier= ImmutableList.copyOf(list(
            decalage(2,1),decalage(2,-1),
            decalage(-2,1),decalage(-2,-1),
            decalage(1,2),decalage(1,-2),
            decalage(-1,2),decalage(-1,-2)));

    private final List<Decalage> decalageInverseCavalier=ImmutableList.copyOf(decalageCavalier);

    private final List<Decalage> decalageDeplacementPionBlanc= ImmutableList.copyOf(list(
            decalage(-1,0)
    ));

    private final List<Decalage> decalageMangePionBlanc= ImmutableList.copyOf(list(
            decalage(-1,1), decalage(-1,-1)
    ));

    private final List<Decalage> decalageEnPassantPionBlanc= ImmutableList.copyOf(list(
            decalage(-1,1), decalage(-1,-1)
    ));

    private final List<Decalage> decalageDeplacementPionNoir= ImmutableList.copyOf(list(
            decalage(1,0)
    ));

    private final List<Decalage> decalageMangePionNoir= ImmutableList.copyOf(list(
            decalage(1,1), decalage(1,-1)
    ));

    private final List<Decalage> decalageEnPassantPionNoir= ImmutableList.copyOf(list(
            decalage(1,1), decalage(1,-1)
    ));

    private final List<Decalage> decalageFou=ImmutableList.copyOf(list(
            decalage(1,1), decalage(1,-1),
            decalage(-1,1), decalage(-1,-1)
    ));

    private final List<Decalage> decalageTour=ImmutableList.copyOf(list(
            decalage(1,0), decalage(0,1),
            decalage(-1,0), decalage(0,-1)
    ));

    private final List<Decalage> decalageReine=ImmutableList.copyOf(
            concat(decalageFou,decalageTour)
    );

    public List<Decalage> getDecalageCavalier(){
        return decalageCavalier;
    }

    public List<Decalage> getDecalageInverseCavalier() {
        return decalageInverseCavalier;
    }

    public List<Decalage> getDecalageDeplacementPionBlanc() {
        return decalageDeplacementPionBlanc;
    }

    public List<Decalage> getDecalageMangePionBlanc() {
        return decalageMangePionBlanc;
    }

    public List<Decalage> getDecalageEnPassantPionBlanc() {
        return decalageEnPassantPionBlanc;
    }

    public List<Decalage> getDecalageDeplacementPionNoir() {
        return decalageDeplacementPionNoir;
    }

    public List<Decalage> getDecalageMangePionNoir() {
        return decalageMangePionNoir;
    }

    public List<Decalage> getDecalageEnPassantPionNoir() {
        return decalageEnPassantPionNoir;
    }

    public List<Decalage> getDecalageFou() {
        return decalageFou;
    }

    public List<Decalage> getDecalageTour() {
        return decalageTour;
    }

    public List<Decalage> getDecalageReine() {
        return decalageReine;
    }

    private Decalage decalage(int rangee, int colonne){
        return DecalageTools.decalage(rangee,colonne);
    }

    private <T> List<T> list(T... liste){
        if(liste==null){
            return new ArrayList<>();
        } else {
            return Arrays.asList(liste);
        }
    }

    private <T> List<T> concat(List<T>... liste){
        List<T> list=new ArrayList<>();
        if(liste!=null){
            for(var tmp:liste){
                list.addAll(tmp);
            }
        }
        return list;
    }
}
