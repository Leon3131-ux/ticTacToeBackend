package com.hyperlinks.converter;

import com.hyperlinks.domain.Game;
import com.hyperlinks.domain.Move;
import com.hyperlinks.domain.Player;
import com.hyperlinks.dto.SaveMoveDto;
import com.hyperlinks.dto.SendMoveDto;
import org.springframework.stereotype.Component;

@Component
public class MoveConverter {

    public Move toEntity (SaveMoveDto saveMoveDto, Game game, Player player){
        return new Move(player, game, saveMoveDto.getX(), saveMoveDto.getY());
    }

    public SendMoveDto toDto(Move move){
        return new SendMoveDto(move.getX(), move.getY(), move.getPlayer().getUsername());
    }

}
