package fr.epf.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StatutInvalideException extends RuntimeException {

    public StatutInvalideException() {
    }

    public StatutInvalideException(String arg0) {
        super(arg0);
    }

    public StatutInvalideException(Throwable arg0) {
        super(arg0);
    }

    public StatutInvalideException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public StatutInvalideException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}