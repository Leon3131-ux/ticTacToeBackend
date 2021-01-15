package com.hyperlinks.service;

import com.hyperlinks.domain.Game;
import com.hyperlinks.domain.User;
import com.hyperlinks.repository.GameRepository;
import com.hyperlinks.util.InviteCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    @Value("${game.inviteCode.strength}")
    private Integer inviteCodeStrength;

    private final InviteCodeGenerator inviteCodeGenerator;
    private final GameRepository gameRepository;
    private final UserService userService;

    public Optional<Game> findByInviteCode(String inviteCode){
        return gameRepository.findByInviteCode(inviteCode);
    }

    public int getAmountOfPlayers(Game game){
        return userService.getByGame(game).size();
    }

    public List<User> getPlayers(Game game){
        return userService.getByGame(game);
    }

    public Game createGame(User user){
        String inviteCode = inviteCodeGenerator.generateInviteCode(user.getUsername(), inviteCodeStrength);

        Game game = new Game(inviteCode, user);

        return gameRepository.save(game);
    }

}