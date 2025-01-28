package c23_99_m_webapp.backend.services;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.Institution;
import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.models.dtos.DataListUsers;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationUser;
import c23_99_m_webapp.backend.models.enums.Role;
import c23_99_m_webapp.backend.repositories.InstitutionRepository;
import c23_99_m_webapp.backend.repositories.UserRepository;
import c23_99_m_webapp.backend.validations.ValidationUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final EmailService emailService;
    private final List<ValidationUser<DataRegistrationUser>> validations;
    private final List<ValidationUser<DataRegistrationUser.DataUpdateUser>> validationsUpdate;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Transactional
    public User registerUser(DataRegistrationUser dataUserRegistration, Institution institution) throws MyException {
        Institution institutionEncontrada = null;

        if (institution.getCue() != null) {
            institutionEncontrada = institutionRepository
                    .findByCue(institution.getCue())
                    .orElseThrow(() -> new MyException("Institución no encontrada con el CUE proporcionado."));
        }

        User userAutenticado = null;
        try {
            userAutenticado = getCurrentUser();
        } catch (MyException e) {

        }
        User user = new User(dataUserRegistration,
                (userAutenticado != null) ? userAutenticado.getInstitution() : institutionEncontrada);

        for (ValidationUser<DataRegistrationUser> v : validations) {
            v.validar(dataUserRegistration);
        }
        userRepository.save(user);

        if (institutionEncontrada != null && user.getRole().equals(Role.TEACHER)) {
            emailService.getEmailTeacher(user.getEmail(), user.getFullName(), dataUserRegistration.password(), institutionEncontrada.getName());
        }
        return user;
    }
    
    @Transactional
    public DataListUsers updateUser(DataRegistrationUser.DataUpdateUser dataUserUpdate) throws MyException {
        User user = userRepository.findByDni(dataUserUpdate.dni())
                .orElseThrow(() -> new MyException("Usuario no encontrado."));

        for (ValidationUser<DataRegistrationUser.DataUpdateUser> v : validationsUpdate) {
            v.validar(dataUserUpdate);
        }

        validateEmailUpdate(dataUserUpdate.email(), user.getEmail());

        user.updateData(dataUserUpdate);
        userRepository.save(user);

        return convertToDataListUsers(user);
    }

    public Page<DataListUsers> listUsers(Pageable pagination) {
        return userRepository.findByActiveTrue(pagination).map(DataListUsers::new);
    }

    @Transactional
    public void deactivateUser(String dni) {
        User user = userRepository.getReferenceByDni(dni);
        user.deactivateUser();
    }

    @Transactional
    public void activateUser(String dni) {
        User user = userRepository.getReferenceByDni(dni);
        user.activateUser();
    }

    public DataListUsers returnDataUserByDni(String dni) {
        User user = userRepository.getReferenceByDni(dni);
        return convertToDataListUsers(user);
    }

    public DataListUsers returnDataUserByName(String fullName) {
        User user = userRepository.getReferenceByFullName(fullName);
        return convertToDataListUsers(user);
    }

    public void deleteUser(String dni) {
        User user = userRepository.getReferenceByDni(dni);
        userRepository.delete(user);
    }

    public User getCurrentUser() throws MyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new MyException("Usuario no autenticado.");
        }

        return userRepository.findByEmail(userDetails.getUsername());
    }

    private DataListUsers convertToDataListUsers(User user) {
        return new DataListUsers(user.getDni(), user.getFullName(), user.getEmail(), user.getInstitution().getCue());
    }

    private void validateEmailUpdate(String newEmail, String oldEmail) throws MyException {
        if (!Objects.equals(newEmail, oldEmail) && userRepository.existsByEmail(newEmail)) {
            throw new MyException("El email ya está registrado.");
        }
    }
}
