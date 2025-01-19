package c23_99_m_webapp.backend.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException{
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    public ResourceNotFoundException(String message) {super(message);}

    public HttpStatus getStatus(){
        return STATUS;
    }
}
