package org.chess.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HistoriqueCoups {

    private List<Coup> listeCoups=new ArrayList<>();

    public Optional<Coup> getDernierCoup(){
        if(listeCoups.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(listeCoups.get(listeCoups.size()-1));
        }
    }
}
