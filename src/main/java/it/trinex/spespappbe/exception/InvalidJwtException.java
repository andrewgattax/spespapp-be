package it.trinex.spespappbe.exception;

import org.springframework.http.HttpStatus;

public class InvalidJwtException extends BaseException{
    public InvalidJwtException(String message) {
        super(
                message,
                HttpStatus.UNAUTHORIZED
        );
    }
}
