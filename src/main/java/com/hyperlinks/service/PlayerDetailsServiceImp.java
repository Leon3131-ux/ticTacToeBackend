package com.hyperlinks.service;

import com.hyperlinks.domain.Player;
import com.hyperlinks.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayerDetailsServiceImp implements UserDetailsService {

    private final PlayerRepository playerRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Player> optionalUser = playerRepository.findByUsername(username);
        if(optionalUser.isPresent()){
            Player player = optionalUser.get();
            return new org.springframework.security.core.userdetails.User(player.getUsername(), player.getPassword(), new ArrayList<>());
        }else{
            throw new UsernameNotFoundException(username);
        }

    }

}
