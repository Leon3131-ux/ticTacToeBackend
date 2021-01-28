package com.hyperlinks.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorDetails extends ErrorDetails {
    private Long timestamp;
    private String message;
    private List<String> validationErrors;
    private List<String> details;

    public ValidationErrorDetails(Long timestamp,
                        String message,
                        List<String> validationErrors,
                        Class<? extends Exception> exceptionClass,
                        String details,
                        boolean sendExceptionClass){
        this.timestamp = timestamp;
        this.message = message;
        this.validationErrors = validationErrors;
        this.details = new ArrayList<>();
        if(sendExceptionClass){
            this.details.add(details);
            this.details.add(exceptionClass.toString());
        }else{
            this.details.add(details);
        }
    }
}
