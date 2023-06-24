package invoicer.invoicer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class NotFoundExceptionHandler {

    @ExceptionHandler(value = {AppUserNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(AppUserNotFoundException e){
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }
}
