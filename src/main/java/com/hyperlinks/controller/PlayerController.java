package com.hyperlinks.controller;

import com.hyperlinks.converter.PlayerConverter;
import com.hyperlinks.domain.Player;
import com.hyperlinks.dto.SignUpDto;
import com.hyperlinks.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerConverter playerConverter;


    @RequestMapping(method = RequestMethod.POST, value = "/api/signUp")
    public ResponseEntity singUp(@Validated @RequestBody SignUpDto signUpDto){
        Player player = playerConverter.toEntity(signUpDto);
        playerService.save(player);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
