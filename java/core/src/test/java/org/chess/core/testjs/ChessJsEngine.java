package org.chess.core.testjs;

import com.google.common.base.Verify;
import com.google.gson.*;
import org.chess.core.domain.Couleur;
import org.chess.core.domain.Partie;
import org.chess.core.domain.Piece;
import org.chess.core.domain.Position;
import org.chess.core.notation.NotationFEN;
import org.chess.core.utils.PositionTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ChessJsEngine {

    public static final Logger LOGGER = LoggerFactory.getLogger(Test1.class);

    public List<JsonReponse> getMoves(Partie partie) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8000/chess1"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // print response headers
        HttpHeaders headers = response.headers();
        headers.map().forEach((k, v) -> LOGGER.info(k + ":" + v));

        // print status code
        LOGGER.info("{}",response.statusCode());

        // print response body
        LOGGER.info("{}",response.body());

        JsonArray objet = JsonParser.parseString(response.body()).getAsJsonArray();

        LOGGER.info("{}",objet);

        List<JsonReponse> liste=new ArrayList<>();

        if(objet!=null&&!objet.isJsonNull()&&objet.size()>0){
            for(int i=0;i<objet.size();i++){
                JsonObject tmp = objet.get(i).getAsJsonObject();
                JsonReponse jsonReponse=new JsonReponse();
                liste.add(jsonReponse);
                if(tmp.has("color")){
                    String color=tmp.get("color").getAsString();
                    if(color!=null){
                        if(color.equals("w")) {
                            jsonReponse.setColor(Couleur.Blanc);
                        } else if(color.equals("b")) {
                            jsonReponse.setColor(Couleur.Noir);
                        }
                    }
                }
                if(tmp.has("from")){
                    String pos=tmp.get("from").getAsString();
                    if(pos!=null&&!pos.trim().isEmpty()) {
                        Position from=Position.getPosition(pos);
                        jsonReponse.setPositionSource(from);
                    }
                }
                if(tmp.has("to")){
                    String pos=tmp.get("to").getAsString();
                    if(pos!=null&&!pos.trim().isEmpty()) {
                        Position to=Position.getPosition(pos);
                        jsonReponse.setPositionDestination(to);
                    }
                }
                if(tmp.has("flags")){
                    String flags=tmp.get("flags").getAsString();
                    jsonReponse.setFlag(flags);
                }
                if(tmp.has("piece")){
                    String piece=tmp.get("piece").getAsString();
                    if(piece!=null&&!piece.trim().isEmpty()&&piece.length()==1) {
                        Piece p=Piece.getValue(piece.toUpperCase().charAt(0));
                        jsonReponse.setPiece(p);
                    }
                }
                if(tmp.has("san")){
                    String san=tmp.get("san").getAsString();
                    jsonReponse.setSan(san);
                }
            }
        }

        return liste;
    }

    public List<JsonReponse> getMoves2(Partie partie) throws IOException, InterruptedException {


        NotationFEN notationFEN = new NotationFEN();

        JsonRequest jsonRequest=new JsonRequest();

        String fen=notationFEN.serialize(partie);

        jsonRequest.setFen(fen);

        HttpClient httpClient = HttpClient.newBuilder().build();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json=gson.toJson(jsonRequest);

        //LOGGER.info("json={}",json);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:8000/chess2"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .setHeader("Content-Type", "application/json")
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // print response headers
        //HttpHeaders headers = response.headers();
        //headers.map().forEach((k, v) -> LOGGER.info(k + ":" + v));

        // print status code
        //LOGGER.info("{}",response.statusCode());

        // print response body
//        LOGGER.trace("{}",response.body());

        JsonArray objet = JsonParser.parseString(response.body()).getAsJsonArray();

//        LOGGER.info("{}",objet);
        
        List<JsonReponse> liste=new ArrayList<>();

        if(objet!=null&&!objet.isJsonNull()&&objet.size()>0){
            for(int i=0;i<objet.size();i++){
                JsonObject tmp = objet.get(i).getAsJsonObject();
                JsonReponse jsonReponse=new JsonReponse();
                liste.add(jsonReponse);
                if(tmp.has("color")){
                    String color=tmp.get("color").getAsString();
                    if(color!=null){
                        if(color.equals("w")) {
                            jsonReponse.setColor(Couleur.Blanc);
                        } else if(color.equals("b")) {
                            jsonReponse.setColor(Couleur.Noir);
                        }
                    }
                }
                if(tmp.has("from")){
                    String pos=tmp.get("from").getAsString();
                    if(pos!=null&&!pos.trim().isEmpty()) {
                        Position from=Position.getPosition(pos);
                        jsonReponse.setPositionSource(from);
                    }
                }
                if(tmp.has("to")){
                    String pos=tmp.get("to").getAsString();
                    if(pos!=null&&!pos.trim().isEmpty()) {
                        Position to=Position.getPosition(pos);
                        jsonReponse.setPositionDestination(to);
                    }
                }
                if(tmp.has("flags")){
                    String flags=tmp.get("flags").getAsString();
                    jsonReponse.setFlag(flags);
                }
                if(tmp.has("piece")){
                    String piece=tmp.get("piece").getAsString();
                    if(piece!=null&&!piece.trim().isEmpty()&&piece.length()==1) {
                        Piece p=Piece.getValueAnglais(piece.toUpperCase().charAt(0));
                        Verify.verifyNotNull(p, "p="+piece);
                        jsonReponse.setPiece(p);
                    }
                }
                if(tmp.has("san")){
                    String san=tmp.get("san").getAsString();
                    jsonReponse.setSan(san);
                }
            }
        }

        return liste;
    }

    public long calculPerft(Partie partie, int depth) throws IOException, InterruptedException {

        NotationFEN notationFEN = new NotationFEN();

        JsonRequest jsonRequest=new JsonRequest();

        String fen=notationFEN.serialize(partie);

        jsonRequest.setFen(fen);

        jsonRequest.setNb(depth);

        HttpClient httpClient = HttpClient.newBuilder().build();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json=gson.toJson(jsonRequest);

        LOGGER.info("json={}",json);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:8000/chess3"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .setHeader("Content-Type", "application/json")
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // print response headers
        //HttpHeaders headers = response.headers();
        //headers.map().forEach((k, v) -> LOGGER.info(k + ":" + v));

        // print status code
        LOGGER.info("{}",response.statusCode());

        // print response body
        LOGGER.info("{}",response.body());

        BigInteger bigInteger = JsonParser.parseString(response.body()).getAsBigInteger();

        return bigInteger.longValueExact();
    }
}
