package c23_99_m_webapp.backend.validations;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationUser;
import c23_99_m_webapp.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateUserUpdateEmail implements ValidationUser<DataRegistrationUser.DataUpdateUser> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validar(DataRegistrationUser.DataUpdateUser dataUpdateUser) throws MyException {
        if (!dataUpdateUser.email().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new MyException("El formato del email no es válido.");
        }

        if (userRepository.existsByEmail(dataUpdateUser.email())) {
            throw new MyException("El email ya está registrado.");
        }
    }
}
