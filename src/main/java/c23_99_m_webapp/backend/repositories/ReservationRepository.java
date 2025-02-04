package c23_99_m_webapp.backend.repositories;


import c23_99_m_webapp.backend.models.Reservation;
import c23_99_m_webapp.backend.models.Resource;
import c23_99_m_webapp.backend.models.enums.ReservationShiftStatus;
import c23_99_m_webapp.backend.models.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("SELECT r FROM Reservation r WHERE r.user.dni = :dni AND r.deleted = false")
    Page<Reservation> findReservationByUserDni(@Param("dni") String dni, Pageable pageable);


    @Query("SELECT r FROM Reservation r WHERE startDate = :startDate")
    Page<Reservation> findReservationByDate(@Param("startDate") LocalDate startDate, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE reservationStatus = :reservationStatus")
    Page<Reservation> findReservationByStatus(@Param("reservationStatus") ReservationStatus reservationStatus, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE reservationShiftStatus = :reservationShiftStatus")
    Page<Reservation> findReservationByShiftStatus(@Param("reservationShiftStatus") ReservationShiftStatus reservationShiftStatus, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE r.deleted = true")
    Page<Reservation> findAllDeleted(Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE r.deleted = false")
    Page<Reservation> findAllActive(Pageable pageable);

    boolean existsByResource(Resource resource);

    boolean existsByResourceIdAndStartDate(Long resourceId, LocalDate startDate);
    List<Reservation> findAllByStartDateAndResource(LocalDate startDate, Resource resource);
}
