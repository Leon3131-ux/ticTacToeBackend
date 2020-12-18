package com.hyperlinks.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@AllArgsConstructor
public class GameData {

    private List<Move> moves;

    public Optional<Move> getMove(int x, int y){
        return moves.stream().filter(move -> move.getX() == x && move.getY() == y).findFirst();
    }

    public void addMove(Move move){
        moves.add(move);
    }
}
