package com.hyperlinks.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Game extends AbstractEntity{

    @Column(nullable = false, unique = true)
    private String inviteCode;

    @OneToOne
    private User host;

}
