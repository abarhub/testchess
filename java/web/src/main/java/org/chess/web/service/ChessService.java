package org.chess.web.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.chess.core.domain.*;
import org.chess.core.notation.NotationFEN;
import org.chess.core.service.CalculMouvementSimpleService;
import org.chess.web.domain.AlgorithmeEnum;
import org.chess.web.domain.Game;
import org.chess.web.dto.ChessBordMvtDTO;
import org.chess.web.dto.InitChessBoardDTO;
import org.chess.web.dto.ListeMvtDTO;
import org.chess.web.dto.MoveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ChessService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChessService.class);

    private AtomicInteger compteur = new AtomicInteger(1);
    private Map<Integer, Game> games = new ConcurrentHashMap<>();
    private Random random = new Random(System.currentTimeMillis());

    public ListeMvtDTO getListeMouvements(ChessBordMvtDTO chessBordMvtDTO) {
        LOGGER.info("chessBordMvtDTO={}", chessBordMvtDTO);
        ListeMvtDTO listeMvtDTO = new ListeMvtDTO();
        if (StringUtils.isNotBlank(chessBordMvtDTO.getFen()) &&
                StringUtils.isNotBlank(chessBordMvtDTO.getMouvement())) {
            CalculMouvementSimpleService calculMouvementSimpleService = new CalculMouvementSimpleService();
            NotationFEN notationFEN = new NotationFEN();
            Partie partie = notationFEN.createPlateau(chessBordMvtDTO.getFen());
            ListeMouvements res = calculMouvementSimpleService.calcul(partie.getPlateau(), partie.getJoueurCourant());
            LOGGER.info("res={}", res);
            if (res != null && res.getMapMouvements() != null) {
                Position p = Position.getPosition(chessBordMvtDTO.getMouvement());
                LOGGER.info("p={}", p);
                Verify.verifyNotNull(p);
                PieceCouleurPosition key = null;
                List<IMouvement> listeMvt = null;
                for (var tmp : res.getMapMouvements().entrySet()) {
                    if (tmp.getKey().getPosition().equals(p)) {
                        key = tmp.getKey();
                        listeMvt = tmp.getValue();
                        break;
                    }
                }
                LOGGER.info("key={}", key);
                LOGGER.info("listeMvt={}", listeMvt);
                if (CollectionUtils.isNotEmpty(listeMvt)) {
                    var liste = listeMvt.stream()
                            .map(x -> x.getPositionDestination())
                            .map(x -> x.toString())
                            .collect(Collectors.toList());
                    listeMvtDTO.setDeplacements(liste);
                }
            }
        }
        return listeMvtDTO;
    }

    public InitChessBoardDTO init() {
        int id = compteur.getAndIncrement();
        NotationFEN notationFEN = new NotationFEN();
        Partie partie = notationFEN.createPlateau("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Game game = new Game(partie);
        this.games.put(id, game);
        InitChessBoardDTO initChessBoardDTO = new InitChessBoardDTO();
        initChessBoardDTO.setId(id);
        initChessBoardDTO.setJoueurCourant("" + game.getJoueur().getNomCourt());
        initChessBoardDTO.setFen(notationFEN.serialize(partie));
        return initChessBoardDTO;
    }

    public MoveDTO nextMove(int id, String algorithme) {
        Preconditions.checkState(id > 0);
        Preconditions.checkState(games.containsKey(id));
        AlgorithmeEnum algorithmeEnum = Enum.valueOf(AlgorithmeEnum.class, algorithme);
        MoveDTO moveDTO = new MoveDTO();
        Game game = games.get(id);
        Optional<IMouvement> mouvement = Optional.empty();
        switch (algorithmeEnum) {
            case SIMPLE:
                mouvement = getSimpleMouvement(game);
                break;
            case HAZARD:
                mouvement = getHazardMouvement(game);
                break;
            case HAZARD2:
                mouvement = getHazard2Mouvement(game);
                break;
            case HAZARD3:
                mouvement = getHazard3Mouvement(game);
                break;
            default:
                Verify.verify(false, "type d'algo non géré: %s", algorithmeEnum);
                break;
        }
        if (mouvement.isPresent()) {
            var mvt = mouvement.get();
            game.getPartie().mouvement(mvt);
            moveDTO.setPositionSource(mvt.getPositionSource().toString());
            moveDTO.setPositionDestination(mvt.getPositionDestination().toString());
            NotationFEN notationFEN = new NotationFEN();
            moveDTO.setFenResultat(notationFEN.serialize(game.getPartie()));
            moveDTO.setJoueurCourant("" + game.getJoueur().getNomCourt());
        }
        return moveDTO;
    }

    public List<String> getListeAlgorithmes() {
        var liste = new ArrayList<String>();
        for (AlgorithmeEnum algorithmeEnum : AlgorithmeEnum.values()) {
            liste.add(algorithmeEnum.name());
        }
        return liste;
    }

    public Optional<IMouvement> getSimpleMouvement(Game game) {
        CalculMouvementSimpleService calculMouvementSimpleService = new CalculMouvementSimpleService();
        Plateau plateau = game.getPartie().getPlateau();
        ConfigurationPartie configurationPartie = game.getConfigurationPartie();
        Couleur joueurCourant = game.getJoueur();
        var liste = calculMouvementSimpleService.calcul(plateau, joueurCourant, configurationPartie);
        if (liste != null && MapUtils.isNotEmpty(liste.getMapMouvements())) {
            var p = liste.getMapMouvements().keySet().iterator().next();
            var liste2 = liste.getMapMouvements().get(p);
            var mvt = liste2.get(0);
            return Optional.of(mvt);
        }
        return Optional.empty();
    }

    private Optional<IMouvement> getHazardMouvement(Game game) {
        CalculMouvementSimpleService calculMouvementSimpleService = new CalculMouvementSimpleService();
        Plateau plateau = game.getPartie().getPlateau();
        ConfigurationPartie configurationPartie = game.getConfigurationPartie();
        Couleur joueurCourant = game.getJoueur();
        var liste = calculMouvementSimpleService.calcul(plateau, joueurCourant, configurationPartie);
        if (liste != null && MapUtils.isNotEmpty(liste.getMapMouvements())) {
            List<IMouvement> liste3 = new ArrayList<>();
            for (var p2 : liste.getMapMouvements().entrySet()) {
                liste3.addAll(p2.getValue());
            }
            var no = random.nextInt(liste3.size());
            var mvt = liste3.get(no);
            return Optional.of(mvt);
        }
        return Optional.empty();
    }

    private Optional<IMouvement> getHazard2Mouvement(Game game) {
        CalculMouvementSimpleService calculMouvementSimpleService = new CalculMouvementSimpleService();
        Plateau plateau = game.getPartie().getPlateau();
        ConfigurationPartie configurationPartie = game.getConfigurationPartie();
        Couleur joueurCourant = game.getJoueur();
        var liste = calculMouvementSimpleService.calcul(plateau, joueurCourant, configurationPartie);
        if (liste != null && MapUtils.isNotEmpty(liste.getMapMouvements())) {
            List<IMouvement> liste3 = new ArrayList<>();
            List<IMouvement> listeAttaque;
            for (var p2 : liste.getMapMouvements().entrySet()) {
                liste3.addAll(p2.getValue());
            }
            listeAttaque = liste3.stream().filter(x -> x.isAttaque()).collect(Collectors.toList());
            if (!listeAttaque.isEmpty()) {
                var no = random.nextInt(listeAttaque.size());
                var mvt = listeAttaque.get(no);
                return Optional.of(mvt);
            } else {
                var no = random.nextInt(liste3.size());
                var mvt = liste3.get(no);
                return Optional.of(mvt);
            }
        }
        return Optional.empty();
    }

    private Optional<IMouvement> getHazard3Mouvement(Game game) {
        CalculMouvementSimpleService calculMouvementSimpleService = new CalculMouvementSimpleService();
        Plateau plateau = game.getPartie().getPlateau();
        ConfigurationPartie configurationPartie = game.getConfigurationPartie();
        Couleur joueurCourant = game.getJoueur();
        var liste = calculMouvementSimpleService.calcul(plateau, joueurCourant, configurationPartie);
        if (liste != null && MapUtils.isNotEmpty(liste.getMapMouvements())) {
            List<IMouvement> liste3 = new ArrayList<>();
            List<IMouvement> listeAttaque;
            for (var p2 : liste.getMapMouvements().entrySet()) {
                liste3.addAll(p2.getValue());
            }
            listeAttaque = liste3.stream().filter(x -> x.isAttaque()).collect(Collectors.toList());
            if (!listeAttaque.isEmpty()) {
                IMouvement meilleurMouvement = null;
                Piece meilleurPiece = null;
                for (var pos : listeAttaque) {
                    var piece = plateau.getCase(pos.getPositionDestination());
                    if (piece != null) {
                        if (meilleurMouvement == null || plusFort(meilleurPiece, piece.getPiece())) {
                            meilleurPiece = piece.getPiece();
                            meilleurMouvement = pos;
                        }
                    }
                }
                if (meilleurMouvement != null) {
                    return Optional.of(meilleurMouvement);
                } else {
                    var no = random.nextInt(listeAttaque.size());
                    var mvt = listeAttaque.get(no);
                    return Optional.of(mvt);
                }
            } else {
                var no = random.nextInt(liste3.size());
                var mvt = liste3.get(no);
                return Optional.of(mvt);
            }
        }
        return Optional.empty();
    }

    private boolean plusFort(Piece meilleurPiece, Piece pieceTestee) {
        return meilleurPiece.getValeur() < pieceTestee.getValeur();
    }
}
