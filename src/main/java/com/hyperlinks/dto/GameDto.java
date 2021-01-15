package com.hyperlinks.dto;

import com.hyperlinks.domain.User;
import lombok.Data;

@Data
public class GameDto {

    private String inviteCode;
    private String hostName;

}
