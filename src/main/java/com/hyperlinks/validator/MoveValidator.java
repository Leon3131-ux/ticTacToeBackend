package com.hyperlinks.validator;

import com.hyperlinks.dto.SaveMoveDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MoveValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return SaveMoveDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SaveMoveDto saveMoveDto = (SaveMoveDto) target;
        validateObject(saveMoveDto, errors);
    }

    private void validateObject(SaveMoveDto saveMoveDto, Errors errors){
        if(saveMoveDto.getX() > 2 && saveMoveDto.getX() < 0){
            errors.reject("move.invalidCoordinates");
        }
    }
}
