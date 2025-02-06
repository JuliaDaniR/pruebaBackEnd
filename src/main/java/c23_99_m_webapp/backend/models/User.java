package c23_99_m_webapp.backend.models;

import c23_99_m_webapp.backend.models.dtos.DataRegistrationUser;
import c23_99_m_webapp.backend.models.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    private String dni;
    private String fullName;
    private String email;
    private String password;
    private Boolean active;

    @ManyToOne
    private Institution institution;

    @Enumerated(EnumType.STRING)
    private Role role = Role.TEACHER;

    public User(DataRegistrationUser dataUserRegistration, Institution institution) {
        this.dni = dataUserRegistration.dni();
        this.fullName = dataUserRegistration.full_name();
        this.email = dataUserRegistration.email();
        this.password = new BCryptPasswordEncoder().encode(dataUserRegistration.password());
        this.active = true;
        this.role = dataUserRegistration.role();
        this.institution = institution;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        switch (role) {
            case ADMIN:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
            case TEACHER:
                authorities.add(new SimpleGrantedAuthority("ROLE_TEACHER"));
                break;
            case USER:
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                break;
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void updateData(DataRegistrationUser.DataUpdateUser dataUserUpdate) {
        if(dataUserUpdate.dni() != null ){
            this.dni = dataUserUpdate.dni();
        }
        if(dataUserUpdate.full_name() != null ){
            this.fullName = dataUserUpdate.full_name();
        }
        if(dataUserUpdate.email() != null ){
            this.email = dataUserUpdate.email();
        }
        if(dataUserUpdate.password() != null ){
            this.password = new BCryptPasswordEncoder().encode(dataUserUpdate.password());
        }
    }

    public void deactivateUser() {
        this.active = false;
    }

    public void activateUser() {
        this.active = true;
    }

    public boolean isActive() {
        return this.active;
    }
}
