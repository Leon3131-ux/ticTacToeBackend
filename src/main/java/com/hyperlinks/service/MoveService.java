package com.hyperlinks.service;

import com.hyperlinks.domain.Game;
import com.hyperlinks.domain.Move;
import com.hyperlinks.dto.SaveMoveDto;
import com.hyperlinks.repository.MoveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoveService {

    private final MoveRepository moveRepository;

    public Move save(Move move){
        return moveRepository.save(move);
    }

    public List<Move> getByGame(Game game){
        return moveRepository.findAllByGame(game);
    }

    public Optional<Move> getByGameAndCoordinates(Game game, int x, int y){
        return moveRepository.findByGameAndXAndY(game, x, y);
    }

    public boolean validateMove(SaveMoveDto saveMoveDto, Game game){
        Optional<Move> optionalMove = getByGameAndCoordinates(game, saveMoveDto.getX(), saveMoveDto.getY());
        return optionalMove.isEmpty();
    }

}
