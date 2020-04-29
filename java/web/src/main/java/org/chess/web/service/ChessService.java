package org.chess.web.service;

import com.google.common.base.Verify;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.chess.core.domain.*;
import org.chess.core.notation.NotationFEN;
import org.chess.core.service.CalculMouvementSimpleService;
import org.chess.web.dto.ChessBordMvtDTO;
import org.chess.web.dto.ListeMvtDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChessService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChessService.class);

    public ListeMvtDTO getListeMouvements(ChessBordMvtDTO chessBordMvtDTO){
        LOGGER.info("chessBordMvtDTO={}", chessBordMvtDTO);
        ListeMvtDTO listeMvtDTO=new ListeMvtDTO();
        if(StringUtils.isNotBlank(chessBordMvtDTO.getFen()) &&
                StringUtils.isNotBlank(chessBordMvtDTO.getMouvement())) {
            CalculMouvementSimpleService calculMouvementSimpleService = new CalculMouvementSimpleService();
            NotationFEN notationFEN = new NotationFEN();
            Partie partie = notationFEN.createPlateau(chessBordMvtDTO.getFen());
            ListeMouvements2 res = calculMouvementSimpleService.calcul(partie.getPlateau(), partie.getJoueurCourant());
            LOGGER.info("res={}", res);
            if(res!=null&&res.getMapMouvements()!=null){
                Position p=Position.getPosition(chessBordMvtDTO.getMouvement());
                LOGGER.info("p={}", p);
                Verify.verifyNotNull(p);
                PieceCouleurPosition key=null;
                List<IMouvement> listeMvt=null;
                for(var tmp:res.getMapMouvements().entrySet()){
                    if(tmp.getKey().getPosition().equals(p)){
                        key=tmp.getKey();
                        listeMvt=tmp.getValue();
                        break;
                    }
                }
                LOGGER.info("key={}",key);
                LOGGER.info("listeMvt={}",listeMvt);
                if(CollectionUtils.isNotEmpty(listeMvt)){
                    var liste=listeMvt.stream()
                            .map(x -> x.getPositionDestination())
                            .map(x -> x.toString())
                            .collect(Collectors.toList());
                    listeMvtDTO.setDeplacements(liste);
                }
            }
        }
        return listeMvtDTO;
    }

}
