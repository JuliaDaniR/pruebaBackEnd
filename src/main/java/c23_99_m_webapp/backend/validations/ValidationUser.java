package c23_99_m_webapp.backend.validations;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationUser;

public interface ValidationUser <T>{
    void validar(T data) throws MyException;
}
