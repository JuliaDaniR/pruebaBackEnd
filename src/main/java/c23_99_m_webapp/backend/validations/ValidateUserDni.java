package c23_99_m_webapp.backend.validations;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationUser;
import c23_99_m_webapp.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateUserDni implements ValidationUser<DataRegistrationUser> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validar(DataRegistrationUser dataRegistrationUser) throws MyException {
        if (userRepository.existsByDni(dataRegistrationUser.dni())) {
            throw new MyException("El dni ya está registrado.");
        }
    }
}
