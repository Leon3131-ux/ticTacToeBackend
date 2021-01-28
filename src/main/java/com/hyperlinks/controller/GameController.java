package com.hyperlinks.controller;

import com.hyperlinks.converter.GameConverter;
import com.hyperlinks.converter.MoveConverter;
import com.hyperlinks.domain.Game;
import com.hyperlinks.domain.Move;
import com.hyperlinks.domain.Player;
import com.hyperlinks.dto.*;
import com.hyperlinks.service.GameService;
import com.hyperlinks.service.MoveService;
import com.hyperlinks.service.PlayerService;
import com.hyperlinks.validator.MoveValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameConverter gameConverter;
    private final MoveConverter moveConverter;
    private final GameService gameService;
    private final PlayerService playerService;
    private final MoveService moveService;
    private final SimpMessageSendingOperations simpleMessagingTemplate;
    private final MoveValidator moveValidator;

    @InitBinder("saveMoveDto")
    public void initSaveProductBinder(WebDataBinder binder) {
        binder.setValidator(moveValidator);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/createGame")
    public ResponseEntity createGame (Principal principal){
        Player player = playerService.getByUsernameOrThrowException(principal.getName());
        if(player.getGame() == null){
            Game game = gameService.createGame(player);
            player.setGame(game);
            game.setHost(player);
            game = gameService.save(game);
            playerService.save(player);
            return new ResponseEntity<>(gameConverter.toDto(game), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/joinGame/{inviteCode}")
    public ResponseEntity joinGame (@PathVariable("inviteCode") String inviteCode, Principal principal){
        Optional<Game> optionalGame = gameService.findByInviteCode(inviteCode);
        if(optionalGame.isPresent()){
            Game game = optionalGame.get();
            Player player = playerService.getByUsernameOrThrowException(principal.getName());
            List<Player> players = game.getPlayers();
            if(player.getGame() == null && players.size() == 1){
                player.setGame(game);
                playerService.save(player);
                simpleMessagingTemplate.convertAndSend("/join/" + game.getInviteCode(), new UsernameDto(player.getUsername()));
                return new ResponseEntity<>(gameConverter.toDto(game), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/startGame")
    public ResponseEntity startGame(Principal principal){
        Player player = playerService.getByUsernameOrThrowException(principal.getName());
        Game game = player.getGame();
        if(game != null && game.getHost().equals(player)){
            List<Player> players = game.getPlayers();
            if(players.size() == 2){
                game.setCurrentTurnPlayer(players.get(new Random().nextInt(players.size())));
                game.setStarted(true);
                gameService.save(game);
                simpleMessagingTemplate.convertAndSend("/launch/" + player.getGame().getInviteCode(), new LaunchGameDto("started"));
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/leaveGame")
    public ResponseEntity leaveGame(Principal principal){
        Player user = playerService.getByUsernameOrThrowException(principal.getName());
        if(user.getGame() != null){
            Game game = user.getGame();
            if(game.getHost().equals(user) || game.getStarted()){
                game.getPlayers().forEach(player -> {
                    player.setGame(null);
                    playerService.save(player);
                });
                gameService.delete(game);
            }else {
                user.setGame(null);
                playerService.save(user);
            }
            simpleMessagingTemplate.convertAndSend("/leave/" + game.getInviteCode(), new UsernameDto(user.getUsername()));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/activeGame")
    public ResponseEntity getActiveGame(Principal principal){
        Player player = playerService.getByUsernameOrThrowException(principal.getName());
        if(player.getGame() != null){
            return new ResponseEntity<>(gameConverter.toDto(player.getGame()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/saveMove")
    public ResponseEntity saveMove(@RequestBody @Validated SaveMoveDto saveMoveDto, Principal principal){
        Player player = playerService.getByUsernameOrThrowException(principal.getName());
        Game game = player.getGame();
        if(game != null && game.getCurrentTurnPlayer().equals(player)){
            if(moveService.validateMove(saveMoveDto, game)){
                Move move = moveConverter.toEntity(saveMoveDto, game, player);
                move = moveService.save(move);
                simpleMessagingTemplate.convertAndSend("/move/" + game.getInviteCode(), moveConverter.toDto(move));
                switchCurrentTurnPlayer(game);
                if(checkGameDone(game)){
                    game.getPlayers().forEach(currentPlayer -> {
                        currentPlayer.setGame(null);
                        playerService.save(player);
                    });
                    gameService.delete(game);
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private void switchCurrentTurnPlayer(Game game){
        List<Player> players = game.getPlayers();
        Optional<Player> oppositePlayer = players.stream().filter(user -> !user.equals(game.getCurrentTurnPlayer())).findFirst();
        oppositePlayer.ifPresent(game::setCurrentTurnPlayer);
        gameService.save(game);
        simpleMessagingTemplate.convertAndSend("/currentTurn/" + game.getInviteCode(), new UsernameDto(game.getCurrentTurnPlayer().getUsername()));
    }

    private boolean checkGameDone(Game game){
        Player winner = gameService.checkForWinner(game);
        if(winner == null && game.getMoves().size() == 9){
            simpleMessagingTemplate.convertAndSend("/win/" + game.getInviteCode(), new WinDto(null, true));
            return true;
        }else if (winner != null){
            simpleMessagingTemplate.convertAndSend("/win/" + game.getInviteCode(), new WinDto(winner.getUsername(), false));
            return true;
        }
        return false;
    }
}
