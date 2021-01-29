package com.hyperlinks.validator;

import com.hyperlinks.domain.Player;
import com.hyperlinks.dto.SignUpDto;
import com.hyperlinks.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SignUpValidator implements Validator {

    private final PlayerService playerService;

    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpDto signUpDto = (SignUpDto) target;
        validateObject(signUpDto, errors);
    }

    private void validateObject(SignUpDto signUpDto, Errors errors){

        if(signUpDto.getUsername().isBlank() || signUpDto.getUsername().isEmpty()){
            errors.reject("signUpDto.username.empty");
        }

        if(signUpDto.getPassword().isBlank() || signUpDto.getPassword().isEmpty()){
            errors.reject("signUpDto.password.empty");
        }

        Optional<Player> userWithSameName = playerService.getByUsername(signUpDto.getUsername());
        if(userWithSameName.isPresent()){
            errors.reject("signUpDto.username.alreadyUsed");
        }

    }
}
