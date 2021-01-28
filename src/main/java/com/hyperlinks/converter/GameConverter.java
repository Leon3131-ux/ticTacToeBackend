package com.hyperlinks.converter;

import com.hyperlinks.domain.Game;
import com.hyperlinks.domain.Move;
import com.hyperlinks.domain.Player;
import com.hyperlinks.dto.GameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GameConverter {

    private final MoveConverter moveConverter;

    public GameDto toDto (Game game){
        GameDto gameDto = new GameDto();
        gameDto.setInviteCode(game.getInviteCode());
        gameDto.setHostName(game.getHost().getUsername());
        gameDto.setPlayers(game.getPlayers().stream().map(Player::getUsername).collect(Collectors.toList()));
        if(game.getCurrentTurnPlayer() != null){
            gameDto.setCurrentTurnPlayer(game.getCurrentTurnPlayer().getUsername());
        }else {
            gameDto.setCurrentTurnPlayer(null);
        }
        gameDto.setStarted(game.getStarted());
        gameDto.setMoves(game.getMoves().stream().map(moveConverter::toDto).collect(Collectors.toList()));
        return gameDto;
    }

}
