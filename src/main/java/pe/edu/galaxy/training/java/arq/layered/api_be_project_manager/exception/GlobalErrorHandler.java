package pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationExpection(MethodArgumentNotValidException ex) {
        log.error("GlobalErrorHandler::validationExpection");
        List<Map<String,String>> errors= ex.getFieldErrors().stream().map(e->{
            return  Map.of(e.getField(), e.getDefaultMessage());
        }).toList();

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> internalError(Exception ex) {
        log.error("GlobalErrorHandler::internalError");
        log.error(ex.getMessage());
        return new ResponseEntity<>("Ocurrió un error interno en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
