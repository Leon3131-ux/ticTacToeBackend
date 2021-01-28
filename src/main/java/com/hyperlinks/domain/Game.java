package com.hyperlinks.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Game extends AbstractEntity{

    public Game(String inviteCode, Player host){
        this.inviteCode = inviteCode;
        this.host = host;
        this.currentTurnPlayer = null;
        this.moves = new ArrayList<>();
        this.players = new ArrayList<>();
        this.started = false;
    }
    @Column(nullable = false)
    private Boolean started;

    @Column(nullable = false, unique = true)
    private String inviteCode;

    @OneToOne
    private Player host;

    @OneToOne
    private Player currentTurnPlayer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    private List<Move> moves;

    @OneToMany(mappedBy = "game")
    private List<Player> players;


}
