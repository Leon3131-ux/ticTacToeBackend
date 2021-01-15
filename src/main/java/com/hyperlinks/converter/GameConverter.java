package com.hyperlinks.converter;

import com.hyperlinks.domain.Game;
import com.hyperlinks.dto.GameDto;
import org.springframework.stereotype.Component;

@Component
public class GameConverter {

    public GameDto toDto (Game game){
        GameDto gameDto = new GameDto();
        gameDto.setInviteCode(game.getInviteCode());
        gameDto.setHostName(game.getHost().getUsername());
        return gameDto;
    }

}
