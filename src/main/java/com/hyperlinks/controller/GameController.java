package com.hyperlinks.controller;

import com.hyperlinks.converter.GameConverter;
import com.hyperlinks.domain.Game;
import com.hyperlinks.domain.GameData;
import com.hyperlinks.domain.Move;
import com.hyperlinks.domain.User;
import com.hyperlinks.dto.ReceiveMoveDto;
import com.hyperlinks.dto.SendMoveDto;
import com.hyperlinks.service.GameService;
import com.hyperlinks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameConverter gameConverter;
    private final GameService gameService;
    private final UserService userService;
    private final SimpMessageSendingOperations simpleMessagingTemplate;
    private final Map<Game, GameData> gameDataMap = new HashMap<>();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity createGame (Principal principal){
        User user = userService.getByUsernameOrThrowException(principal.getName());
        if(user.getGame() != null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Game game = gameService.createGame(user);
        gameDataMap.put(game, new GameData());
        user.setGame(game);
        userService.save(user);

        return new ResponseEntity(gameConverter.toDto(game), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity joinGame (@PathVariable("inviteCode") String inviteCode, Principal principal){
        Optional<Game> optionalGame = gameService.findByInviteCode(inviteCode);
        if(optionalGame.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Game game = optionalGame.get();
        User user = userService.getByUsernameOrThrowException(principal.getName());
        if(user.getGame() != null || gameService.getAmountOfPlayers(game) != 1){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        user.setGame(game);
        userService.save(user);
        return new ResponseEntity<>(gameConverter.toDto(game), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity startGame(@PathVariable("inviteCode") String inviteCode, Principal principal){
        Optional<Game> optionalGame = gameService.findByInviteCode(inviteCode);
        if(optionalGame.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Game game = optionalGame.get();
        User user = userService.getByUsernameOrThrowException(principal.getName());
        if(!game.getHost().equals(user) || gameService.getAmountOfPlayers(game) != 2){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        launchGameOverWebsocket(game);
        return new ResponseEntity<>(gameConverter.toDto(game), HttpStatus.OK);
    }

    public void launchGameOverWebsocket(Game game){
        List<User> players = gameService.getPlayers(game);
        for (User player : players){
            simpleMessagingTemplate.convertAndSendToUser(player.getUsername(),"/launch/" + game.getInviteCode(), "Game Started");
        }
    }

    @MessageMapping(value = "/send/{inviteCode}")
    public void receiveMove(@DestinationVariable("inviteCode") String inviteCode, ReceiveMoveDto receiveMoveDto, Principal principal){
        Optional<Game> optionalGame = gameService.findByInviteCode(inviteCode);
        if(optionalGame.isPresent()){
            Game game = optionalGame.get();
            User user = userService.getByUsernameOrThrowException(principal.getName());

            if(gameService.getPlayers(game).contains(user)){
                int x = receiveMoveDto.getX();
                int y = receiveMoveDto.getY();
                if(gameService.validMove(gameDataMap.get(game), x, y)){
                    GameData gameData = gameDataMap.get(game);
                    gameData.addMove(new Move(user, x, y));
                    simpleMessagingTemplate.convertAndSend("/move/" + inviteCode, new SendMoveDto(x, y, user.getUsername()));
                }
            }
        }
    }
}
