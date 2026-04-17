package fr.epf.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class StockInsuffisantException extends RuntimeException {

    public StockInsuffisantException() {
    }

    public StockInsuffisantException(String message) {
        super(message);
    }

    public StockInsuffisantException(Throwable cause) {
        super(cause);
    }

    public StockInsuffisantException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockInsuffisantException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}