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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final EmailService emailService;

    @Transactional
    public User registerUser(DataRegistrationUser dataUserRegistration) throws MyException {
        Institution institutionEncontrada = null;
        System.out.println(dataUserRegistration.institution_cue());
        try {
            validPassword(dataUserRegistration.password(), dataUserRegistration.password2());

            if (dataUserRegistration.institution_cue() != null) {
                institutionEncontrada = institutionRepository
                        .findByCue(dataUserRegistration.institution_cue())
                        .orElseThrow(() -> new MyException("Institución no encontrada con el CUE proporcionado."));
            }

            if (validEmail(dataUserRegistration.email())) {
                throw new MyException("El email ya está registrado.");
            }

            User user = new User(dataUserRegistration, institutionEncontrada);

            Optional<User> userFind = userRepository.findByDni(user.getDni());

            if(userFind.isPresent()){
                throw new MyException("El dni ya corresponde a un usuario que está registrado.");
            }

            userRepository.save(user);

            if (institutionEncontrada != null & !userFind.isPresent()) {
                emailService.getEmailTeacher(
                        user.getEmail(),
                        user.getFullName(),
                        dataUserRegistration.password(),
                        institutionEncontrada.getName()
                );
            }
            return user;

        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Error al guardar el usuario: " + e.getMessage());
        }
    }

    public void validPassword(String password, String password2) throws MyException {
        if (!password.equals(password2)) {
            throw new MyException("Las contraseñas deben coincidir.");
        }
    }

    public boolean validEmail(String email) {
        User existingUser = userRepository.findByEmail(email);
        return existingUser != null;
    }

    public Page<DataListUsers> listUsers(Pageable pagination) {
        return userRepository.findByActiveTrue(pagination).map(DataListUsers::new);
    }
    @Transactional
    public DataListUsers updateUser(DataRegistrationUser.DataUpdateUser dataUserUpdate) throws MyException {
        User user = userRepository.getReferenceByDni(dataUserUpdate.dni());
        validPassword(dataUserUpdate.password(), dataUserUpdate.password2());
        user.updateData(dataUserUpdate);
        if (validEmail(dataUserUpdate.email())) {
            throw new MyException("El email ya está registrado.");
        }

        if (user != null) {
            emailService.getEmailTeacherUpdate(
                    user.getEmail(),
                    user.getFullName(),
                    user.getPassword(),
                    user.getInstitution().getName()
            );
        }
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

    public User getCurrentUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername());
    }
}
