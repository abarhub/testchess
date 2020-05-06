package org.chess.core.testjs;

import org.chess.core.domain.Partie;
import org.chess.core.notation.NotationFEN;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Test1 {

    public static final Logger LOGGER = LoggerFactory.getLogger(Test1.class);

    @Test
    void test1() throws Exception {

        LOGGER.info("test1");

        ChessJsEngine chessJsEngine=new ChessJsEngine();

        NotationFEN notationFEN = new NotationFEN();

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Partie partie = notationFEN.createPlateau(plateau);

        List<JsonReponse> res = chessJsEngine.getMoves(partie);

        LOGGER.info("res={}", res);
    }

    @Test
    void test2() throws Exception {

        LOGGER.info("test2");

        ChessJsEngine chessJsEngine=new ChessJsEngine();

        NotationFEN notationFEN = new NotationFEN();

        String plateau = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Partie partie = notationFEN.createPlateau(plateau);

        List<JsonReponse> res = chessJsEngine.getMoves2(partie);

        LOGGER.info("res={}", res);
    }

    @Test
    void test3() throws Exception {

        LOGGER.info("test3");

        ChessJsEngine chessJsEngine=new ChessJsEngine();

        NotationFEN notationFEN = new NotationFEN();

        String plateau = "8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 1";
        Partie partie = notationFEN.createPlateau(plateau);

        List<JsonReponse> res = chessJsEngine.getMoves2(partie);

        int nb=0;
        if(res!=null){
            nb=res.size();
        }
        LOGGER.info("nb={}", nb);

        LOGGER.info("res={}", res);
    }

}
