package com.hyperlinks.service;

import com.hyperlinks.domain.Game;
import com.hyperlinks.domain.Move;
import com.hyperlinks.domain.Player;
import com.hyperlinks.repository.GameRepository;
import com.hyperlinks.util.InviteCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    @Value("${game.inviteCode.strength}")
    private Integer inviteCodeStrength;

    private final InviteCodeGenerator inviteCodeGenerator;
    private final GameRepository gameRepository;
    private final MoveService moveService;

    public Optional<Game> findByInviteCode(String inviteCode){
        return gameRepository.findByInviteCode(inviteCode);
    }

    public Game createGame(Player player){
        String inviteCode = inviteCodeGenerator.generateInviteCode(player.getUsername(), inviteCodeStrength);

        return new Game(inviteCode, player);
    }

    public Game save(Game game){
        return gameRepository.save(game);
    }

    public void delete(Game game){
        gameRepository.delete(game);
    }

    public Player checkForWinner(Game game){
        for(Player player : game.getPlayers()){
           if(checkRows(game, player) || checkColumns(game, player) || checkDiagonal(game, player) || checkAntiDiagonal(game, player)){
               return player;
           }
        }
        return null;
    }

    private boolean checkRows(Game game, Player player){
        for (int y = 0; y <= 2; y++){
            boolean playerOwnsRow = true;
            for (int x = 0; x <= 2; x++){
                Optional<Move> optionalMove = moveService.getByGameAndCoordinates(game, x, y);
                if(optionalMove.isPresent() && optionalMove.get().getPlayer().equals(player)){
                    continue;
                }
                playerOwnsRow = false;
            }
            if(playerOwnsRow) return true;
        }
        return false;
    }

    private boolean checkColumns(Game game, Player player){
        for (int x = 0; x <= 2; x++){
            boolean playerOwnsColumn = true;
            for (int y = 0; y <= 2; y++){
                Optional<Move> optionalMove = moveService.getByGameAndCoordinates(game, x, y);
                if(optionalMove.isPresent() && optionalMove.get().getPlayer().equals(player)){
                    continue;
                }
                playerOwnsColumn = false;
            }
            if(playerOwnsColumn) return true;
        }
        return false;
    }

    private boolean checkDiagonal(Game game, Player player){
        int x = 0;
        boolean playerOwnsDiagonal = true;
        for(int y = 0; y <= 2; y++){
            Optional<Move> optionalMove = moveService.getByGameAndCoordinates(game, x, y);
            x++;
            if(optionalMove.isPresent() && optionalMove.get().getPlayer().equals(player)){
                continue;
            }
            playerOwnsDiagonal = false;
        }
        return playerOwnsDiagonal;
    }

    private boolean checkAntiDiagonal(Game game, Player player){
        int x = 2;
        boolean playerOwnsAntiDiagonal = true;
        for(int y = 0; y <= 2; y++){
            Optional<Move> optionalMove = moveService.getByGameAndCoordinates(game, x, y);
            x--;
            if(optionalMove.isPresent() && optionalMove.get().getPlayer().equals(player)){
                continue;
            }
            playerOwnsAntiDiagonal = false;
        }
        return playerOwnsAntiDiagonal;
    }

}
