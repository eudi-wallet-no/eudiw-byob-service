package no.idporten.eudiw.byob.service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@RestControllerAdvice
public class BadRequestExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<String> handleBadRequestException(HttpClientErrorException.BadRequest badRequest) {
        return new ResponseEntity<>(badRequest.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
