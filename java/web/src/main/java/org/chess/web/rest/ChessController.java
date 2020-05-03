package org.chess.web.rest;

import org.chess.web.dto.ChessBordMvtDTO;
import org.chess.web.dto.InitChessBoardDTO;
import org.chess.web.dto.ListeMvtDTO;
import org.chess.web.dto.MoveDTO;
import org.chess.web.service.ChessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChessController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChessController.class);

    @Autowired
    private ChessService chessService;

    @GetMapping("/api/greeting")
    public String test(){
        return "abc";
    }

    @PostMapping("/api/mvt")
    public ListeMvtDTO listeMouvements(@RequestBody ChessBordMvtDTO chessBordMvtDTO){
        return chessService.getListeMouvements(chessBordMvtDTO);
    }

    @PostMapping("/api/init")
    public InitChessBoardDTO init(){
        LOGGER.info("init");
        return chessService.init();
    }


    @PostMapping("/api/nextMove/{id}/{algorithme}")
    public MoveDTO nextMove(@PathVariable("id")int id,@PathVariable("algorithme") String algorithme){
        LOGGER.info("nextMove: id={}, algo={}", id, algorithme);
        return chessService.nextMove(id, algorithme);
    }

    @GetMapping("/api/algorithme")
    public List<String> getListeAlgorithmes(){
        return chessService.getListeAlgorithmes();
    }

}
