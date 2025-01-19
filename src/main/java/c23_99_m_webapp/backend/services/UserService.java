package c23_99_m_webapp.backend.services;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.Institution;
import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.models.dtos.DataListUsers;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationUser;
import c23_99_m_webapp.backend.repositories.InstitutionRepository;
import c23_99_m_webapp.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    
    @Transactional
    public User registerUser(DataRegistrationUser dataUserRegistration) throws MyException {
        Institution institutionEncontrada = null;
        System.out.println(dataUserRegistration.institution_cue());
        try {
            valida(dataUserRegistration.password(), dataUserRegistration.password2());

            if (dataUserRegistration.institution_cue() != null) {
                institutionEncontrada = institutionRepository
                        .findByCue(dataUserRegistration.institution_cue())
                        .orElseThrow(() -> new MyException("Institución no encontrada con el CUE proporcionado."));
            }

            User user = new User(dataUserRegistration, institutionEncontrada);

            return userRepository.save(user);

        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Error al guardar el usuario: " + e.getMessage());
        }
    }

    public void valida(String password, String password2) throws MyException {
        if (!password.equals(password2)) {
            throw new MyException("Las contraseñas deben coincidir.");
        }
    }

    public Page<DataListUsers> listUsers(Pageable pagination) {
        return userRepository.findByActiveTrue(pagination).map(DataListUsers::new);
    }
    @Transactional
    public DataListUsers updateUser(DataRegistrationUser.DataUpdateUser dataUserUpdate) throws MyException {
        User user = userRepository.getReferenceByDni(dataUserUpdate.dni());
        valida(dataUserUpdate.password(), dataUserUpdate.password2());
        user.updateData(dataUserUpdate);
        return new DataListUsers(
                user.getDni(),
                user.getFullName(),
                user.getEmail(),
                user.getInstitution().getName());
    }
    @Transactional
    public User deactivateUser(String dni) {
        User user = userRepository.getReferenceByDni(dni);
        user.deactivateUser();
        return user;
    }

    @Transactional
    public User activateUser(String dni) {
        User user = userRepository.getReferenceByDni(dni);
        user.activateUser();
        return user;
    }

    public DataListUsers returnDataUserByDni(String dni) {
        User user = userRepository.getReferenceByDni(dni);
        return new DataListUsers(user.getDni(),user.getFullName(),user.getEmail(),user.getInstitution().getName());
    }

    public DataListUsers returnDataUserByName(String fullName) {
        User user = userRepository.getReferenceByFullName(fullName);
        return new DataListUsers(user.getDni(),user.getFullName(),user.getEmail(),user.getInstitution().getName());
    }

    public void deleteUser(String dni) {
        User user = userRepository.getReferenceByDni(dni);
        userRepository.delete(user);
    }
}
