package org.chess.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class StreamGobbler implements Runnable {

    public static final Logger LOGGER = LoggerFactory.getLogger(StreamGobbler.class);

    private InputStream inputStream;
    private Consumer<String> consumer;

    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        LOGGER.info("debut input ...");
        try {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(x-> {
                        LOGGER.trace("x:{}",x);
                        consumer.accept(x);
                    });
            LOGGER.info("fin input");
        }catch(Exception e){
            LOGGER.error("Erreur",e);
        }
        LOGGER.info("fin input2");
    }
}
