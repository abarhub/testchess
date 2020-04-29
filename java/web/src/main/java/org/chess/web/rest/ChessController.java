package org.chess.web.rest;

import org.chess.web.dto.ChessBordMvtDTO;
import org.chess.web.dto.ListeMvtDTO;
import org.chess.web.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessController {

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


}
