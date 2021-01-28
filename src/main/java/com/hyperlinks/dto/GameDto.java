package com.hyperlinks.dto;

import lombok.Data;

import java.util.List;

@Data
public class GameDto {

    private String inviteCode;
    private String hostName;
    private List<String> players;
    private String currentTurnPlayer;
    private List<SendMoveDto> moves;
    private boolean started;

}
