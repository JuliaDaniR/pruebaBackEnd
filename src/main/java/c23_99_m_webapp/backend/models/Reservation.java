package c23_99_m_webapp.backend.models;

import c23_99_m_webapp.backend.models.dtos.ReservationDto;
import c23_99_m_webapp.backend.models.enums.ReservationShiftStatus;
import c23_99_m_webapp.backend.models.enums.ReservationStatus;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer countElement;
    private LocalDate startDate;
    private String startHour;

//    @Enumerated(EnumType.STRING)
//    private ReservationShiftStatus reservationShiftStatus;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    private boolean deleted = false;
    
    public Reservation(ReservationDto reservationDto) {
        this.countElement = reservationDto.countElement();
        this.startDate = reservationDto.startDate();
        this.startHour = reservationDto.starHour();
    }

    public void handleReservationStatus() {
        switch (this.reservationStatus) {
//            case PENDING:
//                System.out.println("La reservación está pendiente.");
//                break;
            case CANCELLED:
                System.out.println("La reservación ha sido cancelada.");
                break;
            case CONFIRMED:
                System.out.println("La reservación ha sido confirmada.");
                break;
            case FINISHED:
                System.out.println("La reservación ha sido finalizada.");
                break;
//            case REJECTED:
//                System.out.println("La reservación ha sido rechazada.");
//                break;
            default:
                System.out.println("Estado de reservación desconocido.");
                break;
        }
    }
}