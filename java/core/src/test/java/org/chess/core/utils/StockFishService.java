package org.chess.core.utils;


import com.google.common.collect.Lists;
import org.chess.core.testjs.Test1;
import org.chess.core.utils.StreamGobbler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class StockFishService {

    public static final Logger LOGGER = LoggerFactory.getLogger(StockFishService.class);


    public long getPerft(String fen, int depth) throws IOException, InterruptedException {
        List<String> liste=new ArrayList<>();
        List<String> listeErreur=new ArrayList<>();
        String cmd="D:/temp/stockfish/stockfish-11-win/Windows/stockfish_20011801_x64.exe";
        BlockingQueue<String> queue=new LinkedBlockingQueue<>();

        //queue.add("position fen "+fen);
        //queue.add("go perft "+depth);
        //queue.add("quit");

        final List<String> listeCommandes= Lists.newArrayList(
                "position fen "+fen,
                "go perft "+depth,
                "quit"
        );

        int res=exec(cmd, x-> {

            LOGGER.info("output={}",x);
            if(liste.isEmpty()){
                try {
                    Thread.sleep(5*1000L);
                    LOGGER.info("envoie des commandes...");
                    for(String s:listeCommandes){
                        queue.put(s);
                    }
                    //queue.put("position fen "+fen);
                    //queue.put("go perft "+depth);
                    LOGGER.info("envoie des commandes ok");
                } catch (Exception e) {
                    LOGGER.error("error",e);
                }
            }
            liste.add(x);
            }, listeErreur::add, queue);

        LOGGER.info("res={}",res);
        LOGGER.info("liste={}",liste);

        if(res!=0){
            throw new RuntimeException("Erreur pendant l'execution du programme");
        }

        for(String s:liste){
            if(s!=null&&s.startsWith("Nodes searched: ")){
                String s2=s.substring(16).trim();
                long n=Long.parseLong(s2);
                return n;
            }
        }

        return 0;
    }

    private int exec(String cmd, Consumer<String> consumer, Consumer<String> consumerError, BlockingQueue<String> queue) throws IOException, InterruptedException {

        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");

        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", cmd);
        } else {
            builder.command("sh", "-c", cmd);
        }
        builder.directory(new File(System.getProperty("user.home")));
        LOGGER.info("start...");
        Process process = builder.start();

        LOGGER.info("output...");
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), consumer);
        Executors.newSingleThreadExecutor().submit(streamGobbler);

        LOGGER.info("error...");
        StreamGobbler streamGobbler2 =
                new StreamGobbler(process.getErrorStream(), consumerError);
        Executors.newSingleThreadExecutor().submit(streamGobbler2);

        LOGGER.info("input...");
        StreamOutputGobbler streamOutputGobbler=new StreamOutputGobbler(process.getOutputStream(), queue);
        Executors.newSingleThreadExecutor().submit(streamOutputGobbler);

        LOGGER.info("waitFor...");
        return process.waitFor();
    }
}
