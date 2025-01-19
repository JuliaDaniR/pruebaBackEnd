package c23_99_m_webapp.backend.repositories;


import c23_99_m_webapp.backend.models.Reservation;
import c23_99_m_webapp.backend.models.Resource;
import c23_99_m_webapp.backend.models.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

//    //buscar reservacion por fecha
//    Reservation findReservationByStartDate(LocalDate startDate);
//
//    //buscar reservacion por status
//    @Query("SELECT r FROM Reservation r WHERE reservationStatus = :reservationStatus")
//    Reservation findReservationByStatus(@Param("reservationStatus")ReservationStatus reservationStatus);

    @Query("SELECT r FROM Reservation r WHERE r.deleted = true")
    List<Reservation> findAllDeleted();

    boolean existsByResource(Resource resource);
}
