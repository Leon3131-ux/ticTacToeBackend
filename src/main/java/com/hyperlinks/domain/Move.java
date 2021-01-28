package com.hyperlinks.domain;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Move extends AbstractEntity{

    @ManyToOne
    private Player player;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="game_id", nullable=false)
    private Game game;

    private int x;

    private int y;

    public Player getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
