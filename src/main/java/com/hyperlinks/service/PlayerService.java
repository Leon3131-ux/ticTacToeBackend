package com.hyperlinks.service;

import com.hyperlinks.domain.Player;
import com.hyperlinks.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Player save(Player player) {return playerRepository.save(player);}

    public Player getByUsernameOrThrowException(String username) throws UsernameNotFoundException {
        return playerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
