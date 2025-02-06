package c23_99_m_webapp.backend.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends RuntimeException{
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    public DuplicateResourceException(String message) {super(message);}

    public HttpStatus getStatus(){
        return STATUS;
    }
}
