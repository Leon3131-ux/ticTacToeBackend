package com.hyperlinks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMoveDto {

    private int x;
    private int y;
    private String username;

}
