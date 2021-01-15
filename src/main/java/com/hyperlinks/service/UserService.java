package com.hyperlinks.service;

import com.hyperlinks.domain.Game;
import com.hyperlinks.domain.User;
import com.hyperlinks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {return userRepository.save(user);}

    public User getByUsernameOrThrowException(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public List<User> getByGame(Game game){
        return userRepository.findAllByGame(game);
    }
}
