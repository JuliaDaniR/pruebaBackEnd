package c23_99_m_webapp.backend.exceptions;

import org.springframework.http.HttpStatus;

public class BadCustomerRequestException extends RuntimeException{
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    public BadCustomerRequestException(String message) {super(message);}

    public HttpStatus getStatus(){
        return STATUS;
    }
}
