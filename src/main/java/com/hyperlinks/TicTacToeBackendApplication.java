package com.hyperlinks;

import com.hyperlinks.domain.Player;
import com.hyperlinks.service.PlayerService;
import com.hyperlinks.util.InviteCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
public class TicTacToeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicTacToeBackendApplication.class, args);
    }

    private final PlayerService playerService;

    @Bean
    public InviteCodeGenerator inviteCodeGenerator() {return new InviteCodeGenerator();}

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {return new BCryptPasswordEncoder();}

    @PostConstruct
    private void initUsers(){
        this.playerService.save(new Player("admin", bCryptPasswordEncoder().encode("admin")));
        this.playerService.save(new Player("admin2", bCryptPasswordEncoder().encode("admin")));
    }

}
