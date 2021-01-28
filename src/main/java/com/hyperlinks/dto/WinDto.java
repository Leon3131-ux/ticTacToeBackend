package com.hyperlinks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WinDto {

    private String winner;
    private boolean draw;

}
