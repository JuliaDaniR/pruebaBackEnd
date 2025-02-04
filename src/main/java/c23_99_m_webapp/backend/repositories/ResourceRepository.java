package c23_99_m_webapp.backend.repositories;

import c23_99_m_webapp.backend.models.Resource;
import c23_99_m_webapp.backend.models.enums.ResourceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> findAllByStatus(ResourceStatus status);
    Resource findByName(String name);
}
