package com.hyperlinks;

import com.hyperlinks.util.InviteCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TicTacToeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicTacToeBackendApplication.class, args);
    }

    @Bean
    public InviteCodeGenerator inviteCodeGenerator() {return new InviteCodeGenerator();}

}
