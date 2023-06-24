package invoicer.invoicer.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public AppUserNotFoundException(String message) {
        super(message);
    }
    public AppUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
