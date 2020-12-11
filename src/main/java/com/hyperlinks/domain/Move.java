package com.hyperlinks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Move extends User{

    private User user;

    private int x;

    private int y;


}
