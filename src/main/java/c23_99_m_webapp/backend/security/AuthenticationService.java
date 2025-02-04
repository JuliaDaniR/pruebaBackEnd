package c23_99_m_webapp.backend.security;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        if (!user.isActive()) {
            try {
                throw new MyException("Usuario inactivo, contacte al administrador.");
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }
}
