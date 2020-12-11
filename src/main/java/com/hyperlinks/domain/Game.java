package com.hyperlinks.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game extends AbstractEntity{

    @Column(nullable = false, unique = true)
    private String inviteCode;

}
