package com.hyperlinks.controller;

import com.hyperlinks.converter.GameConverter;
import com.hyperlinks.domain.Game;
import com.hyperlinks.domain.GameData;
import com.hyperlinks.domain.User;
import com.hyperlinks.service.GameService;
import com.hyperlinks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameConverter gameConverter;
    private final GameService gameService;
    private final UserService userService;
    private final Map<Game, GameData> gameDataMap = new HashMap<>();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity createGame (Principal principal){
        User user = userService.getByUsernameOrThrowException(principal.getName());
        if(user.getGame() != null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Game game = gameService.createGame(user);
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
        return new ResponseEntity<>(gameConverter.toDto(game), HttpStatus.OK);
    }

}
