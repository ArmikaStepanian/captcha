package com.stepanian.captcha.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectLengthException extends Exception {

    public IncorrectLengthException(String s) {
        super(s);
    }
}
