package com.hyperlinks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Move{

    private User user;

    private int x;

    private int y;

    public User getUser() {
        return user;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
