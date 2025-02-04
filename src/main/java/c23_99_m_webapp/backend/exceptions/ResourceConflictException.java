package c23_99_m_webapp.backend.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceConflictException extends RuntimeException{
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    public ResourceConflictException(String message) {super(message);}

    public HttpStatus getStatus(){
        return STATUS;
    }
}