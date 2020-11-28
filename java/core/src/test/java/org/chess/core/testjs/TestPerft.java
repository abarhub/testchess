package org.chess.core.testjs;

import org.chess.core.domain.ColonneEnum;
import org.chess.core.domain.Partie;
import org.chess.core.domain.Position;
import org.chess.core.domain.RangeeEnum;
import org.chess.core.notation.NotationFEN;
import org.chess.core.service.CalculMouvementSimpleService;
import org.chess.core.utils.CalculPerft;
import org.chess.core.utils.ResultatPerft;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;

public class TestPerft {

    public static final Logger LOGGER = LoggerFactory.getLogger(TestPerft.class);

    private CalculMouvementSimpleService calculMouvementSimpleService;

    private NotationFEN notationFEN = new NotationFEN();

    @BeforeEach
    public void setUp() {
        calculMouvementSimpleService = new CalculMouvementSimpleService();
    }

    @Test
    void test1() throws Exception {

        String fen="";
        int depth=0;
        int test=1;
        Position positionSrc=null;
        Position positionDest=null;

        test=1;
        test=2;
//        test=3;

        if(test==1) {
            fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
            depth = 6;
        } else if(test==2){
            fen = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -";
            depth = 2;
//            positionSrc=new Position(RangeeEnum.RANGEE1, ColonneEnum.COLONNEE);
//            positionDest=new Position(RangeeEnum.RANGEE1, ColonneEnum.COLONNEC);
        } else if(test==3){
            fen="r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/2KR3R b Kq - 0 1";
            depth=1;
        }

        Partie partie = notationFEN.createPlateau(fen);

        CalculPerft calculPerft = new CalculPerft();
        ResultatPerft perftJava=null;
        Instant debut=null;
        Instant fin=null;
//        try(PrintWriter out=new PrintWriter(Files.newBufferedWriter(Paths.get("D:\\temp\\tmp3/test.txt"), StandardOpenOption.TRUNCATE_EXISTING))) {
            debut = Instant.now();
//            perftJava = calculPerft.calculPerft2(partie, depth, true, out, positionSrc, positionDest);
        perftJava = calculPerft.calculPerft2(partie, depth, false, null, positionSrc, positionDest);
            fin = Instant.now();
//            out.flush();
//        }


        LOGGER.info("perft({}): {}", depth, perftJava.getPerft());
        LOGGER.info("duree: {}", Duration.between(debut, fin));

    }

}
