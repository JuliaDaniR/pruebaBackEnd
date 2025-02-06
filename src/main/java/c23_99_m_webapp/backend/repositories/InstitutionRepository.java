package c23_99_m_webapp.backend.repositories;

import c23_99_m_webapp.backend.models.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    Optional<Institution> findByCue(String cue);

    Optional<Institution> findByAddress(String address);

    Optional<Institution> findByEmail(String email);

    Optional<Institution> findByName(String name);

    Optional<Institution> findByPhone(String phone);

    Optional<Institution> findByWebsite(String website);

    void deleteByCue(String cue);

    boolean existsByCue(String cue);
}