package com.hyperlinks.converter;

import com.hyperlinks.domain.Player;
import com.hyperlinks.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerConverter {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Player toEntity(SignUpDto signUpDto){
        return new Player(signUpDto.getUsername(), bCryptPasswordEncoder.encode(signUpDto.getPassword()));
    }

}
