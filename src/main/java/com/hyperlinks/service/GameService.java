package com.hyperlinks.service;

import com.hyperlinks.domain.Game;
import com.hyperlinks.domain.GameData;
import com.hyperlinks.domain.Move;
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

    public boolean validMove(GameData gameData, int x, int y){
        if(x > 2 || x < 0 || y > 2 || y < 0){
            return false;
        }

        Optional<Move> optionalMove = gameData.getMove(x, y);
        return optionalMove.isEmpty();
    }

    public User checkGameWon(GameData gameData, List<User> players){
        for(User player : players){
            boolean gameWonByPlayer = false;
            if (gameData.getMove(1, 1).isPresent() && gameData.getMove(1, 1 ).get().getUser().equals(player)){
                if (gameData.getMove(0, 0).isPresent() && gameData.getMove(1, 1 ).get().getUser().equals(player)){
                    if (gameData.getMove(2, 2).isPresent() && gameData.getMove(1, 1 ).get().getUser().equals(player)){
                        gameWonByPlayer = true;
                    }
                }
                else if (gameData.getMove(0, 1).isPresent() && gameData.getMove(1, 1 ).get().getUser().equals(player)){
                    if (gameData.getMove(2, 1).isPresent() && gameData.getMove(1, 1 ).get().getUser().equals(player)){
                        gameWonByPlayer = true;
                    }
                }
                else if (gameData.getMove(1, 0).isPresent() && gameData.getMove(1, 1 ).get().getUser().equals(player)){
                    if (gameData.getMove(1, 2).isPresent() && gameData.getMove(1, 1 ).get().getUser().equals(player)){
                        gameWonByPlayer = true;
                    }
                }
                else if (gameData.getMove(2, 0).isPresent() && gameData.getMove(1, 1 ).get().getUser().equals(player)){
                    if (gameData.getMove(0, 2).isPresent() && gameData.getMove(1, 1 ).get().getUser().equals(player)){
                        gameWonByPlayer = true;
                    }
                }
            }
            if(gameWonByPlayer){
                return player;
            }
        }
        return null;
    }

}
