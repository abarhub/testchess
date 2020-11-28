package org.chess.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

public class StreamOutputGobbler implements Runnable {

    public static final Logger LOGGER = LoggerFactory.getLogger(StreamOutputGobbler.class);

    private BufferedWriter writer;
    private BlockingQueue<String> queue;

    public StreamOutputGobbler(OutputStream outputStream, BlockingQueue<String> queue) {
        this.writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        this.queue=queue;
    }

    @Override
    public void run() {
        LOGGER.info("debut");
        try {
            String elt;
            do {
                elt = queue.take();
                if(elt!=null) {
                    LOGGER.trace("write: {}",elt);
                    writer.append(elt);
                    writer.newLine();
                    writer.flush();
                }
            }while(elt!=null);
            LOGGER.info("fin");
        }catch(Exception e){
            LOGGER.error("Erreur", e);
        }
        LOGGER.info("fin2");
    }

}
