package c23_99_m_webapp.backend.services;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.Reservation;
import c23_99_m_webapp.backend.models.Resource;
import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.models.dtos.DataAnswerReserveByDniUser;
import c23_99_m_webapp.backend.models.dtos.DataAnswerReservation;
import c23_99_m_webapp.backend.models.dtos.ReservationDto;
import c23_99_m_webapp.backend.models.enums.ReservationShiftStatus;
import c23_99_m_webapp.backend.models.enums.ReservationStatus;
import c23_99_m_webapp.backend.models.enums.ResourceStatus;
import c23_99_m_webapp.backend.models.enums.Role;
import c23_99_m_webapp.backend.repositories.ReservationRepository;
import c23_99_m_webapp.backend.repositories.ResourceRepository;
import c23_99_m_webapp.backend.repositories.UserRepository;
import c23_99_m_webapp.backend.validations.ValidateReservationByUserRole;
import c23_99_m_webapp.backend.validations.ValidateReservationPermissions;
import c23_99_m_webapp.backend.validations.ValidateReservationResourceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    UserService userService;

    @Autowired
    ValidateReservationResourceStatus validateReservationResourceStatus;

    @Autowired
    ValidateReservationByUserRole validateReservationByUserRole;

    @Autowired
    ValidateReservationPermissions validateReservationPermissions;

    public DataAnswerReservation createdReservation(ReservationDto reservationDto) throws MyException {
        User user = validateReservationByUserRole.validateByRole();
        Resource resource = validateReservationResourceStatus.validateByResourceStatus(reservationDto);

        Reservation reservation = new Reservation();
        reservation.setStartDate(reservationDto.startDate());
        reservation.setReservationShiftStatus(reservationDto.reservationShiftStatus());
        reservation.setSelectedTimeSlot(reservationDto.selectedTimeSlot());
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        reservation.setDeleted(false);
        reservation.setUser(user);

        resource.setStatus(ResourceStatus.IN_USE);
        resource = resourceRepository.save(resource);

        reservation.setResource(resource);
        reservation = reservationRepository.save(reservation);

        DataAnswerReservation data = new DataAnswerReservation(
                reservation.getUser().getEmail(),
                reservation.getStartDate(),
                reservation.getReservationShiftStatus(),
                reservation.getSelectedTimeSlot(),
                reservation.getResource().getName(),
                reservation.getReservationStatus()
        );
        return data;
    }

    public Page<ReservationDto> getReservations(Pageable pageable) throws MyException {
        try {
            User user = userService.getCurrentUser();
            Page<ReservationDto> reservations;

            if (user.getRole() == Role.ADMIN) {
                reservations = reservationRepository.findAllActive(pageable)
                        .map(this::convertToDto);

            } else if (user.getRole() == Role.TEACHER) {
                reservations = reservationRepository.findReservationByUserDni(user.getDni(),pageable)
                        .map(this::convertToDto);
            } else {
                reservations = Page.empty();
            }
            return reservations;
        } catch (Exception e) {
            throw new MyException("Error al obtener lista.");
        }
    }

    public ReservationDto findReservationById(Long id) throws MyException {

        User user = userService.getCurrentUser();
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow();

        validateReservationPermissions.validateAuthority(reservation); //validacion

        if(reservation.isDeleted() && reservation.getUser().equals(user)){
            throw new MyException("La reserva está eliminada.");
        }

        return convertToDto(reservation);
    }

    private ReservationDto convertToDto(Reservation reservation){

        return new ReservationDto(
                reservation.getStartDate(),
                reservation.getReservationShiftStatus(),
                reservation.getSelectedTimeSlot(),
                reservation.getResource().getId()
        );
    }

    //update para el usuario
    public Optional<ReservationDto> updateById(Long id, ReservationDto updatedReservationDto) throws MyException {

        if (id == null){
            throw new MyException("No se encuentra la reserva para actualizar");
        }
        return reservationRepository.findById(id).map(reservation -> {
            reservation.setStartDate(updatedReservationDto.startDate());
            reservation.setReservationShiftStatus(updatedReservationDto.reservationShiftStatus());
            reservation.setSelectedTimeSlot(updatedReservationDto.selectedTimeSlot());

            Resource resource = resourceRepository.findById(updatedReservationDto.resourceid()).orElseThrow();
            reservation.setResource(resource);
            resourceRepository.save(resource);

            Reservation updatedReservation = reservationRepository.save(reservation);
            return convertToDto(updatedReservation);
        });
    }

    //update para el admin
    public ReservationDto updateReservationStatusByAdmin(Long id, ReservationStatus newStatus) throws MyException {

        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        User user = userService.getCurrentUser();

        if (user.getRole() == Role.ADMIN){
            reservation.setReservationStatus(newStatus);
            reservationRepository.save(reservation);
            return convertToDto(reservation);
        }
        return convertToDto(validateReservationPermissions.validateAuthority(reservation));
    }

    //metodo borrado logico
    public void deleteReservationById(Long id) throws MyException {
        if (!reservationRepository.existsById(id)) {
            throw new MyException("No se encuentra la reserva.");
        }
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        reservation.setDeleted(true);
        Resource resource = reservation.getResource();
        resource.setStatus(ResourceStatus.AVAILABLE);
        reservationRepository.save(reservation);
        resourceRepository.save(resource);
    }

    public Page<ReservationDto> getDeletedReservations(Pageable pageable) throws MyException {
        try {
            Page<Reservation> deletedReservations = reservationRepository.findAllDeleted(pageable);
            return deletedReservations
                    .map(this::convertToDto);

        } catch (RuntimeException e) {
            throw new MyException("Error al traer la lista de reservas borradas.");
        }
    }

    public void restoreReservation(Long id) throws MyException {

        try {
            Reservation reservation = reservationRepository.findById(id).orElse(null);
            assert reservation != null;
            Resource resource = reservation.getResource();

            if (reservation.isDeleted() & resource.getStatus() == ResourceStatus.AVAILABLE) {
                reservation.setDeleted(false);
                resource.setStatus(ResourceStatus.IN_USE);
                reservationRepository.save(reservation);
                resourceRepository.save(resource);
            }

        } catch (RuntimeException e) {
            throw new MyException("Error al restaurar la reserva.");
        }
    }

    public Page<DataAnswerReservation> findByDate(LocalDate startDate, Pageable pageable) throws MyException{
        try {
            Page<Reservation> reservationPage = reservationRepository.findReservationByDate(startDate, pageable);
            return reservationPage.map(reservation -> {
                DataAnswerReservation data = new DataAnswerReservation(
                        reservation.getUser().getEmail(),
                        reservation.getStartDate(),
                        reservation.getReservationShiftStatus(),
                        reservation.getSelectedTimeSlot(),
                        reservation.getResource().getName(),
                        reservation.getReservationStatus()
                );
                return data;
            });
        } catch (RuntimeException e) {
            throw new MyException(e.getMessage());
        }
    }

    public Page<DataAnswerReservation> findByStatus(ReservationStatus status, Pageable pageable) throws MyException{

        try {
            Page<Reservation> reservations = reservationRepository.findReservationByStatus(status, pageable);
            return reservations.map(reservation -> {
                DataAnswerReservation dateReservation = new DataAnswerReservation(
                        reservation.getUser().getEmail(),
                        reservation.getStartDate(),
                        reservation.getReservationShiftStatus(),
                        reservation.getSelectedTimeSlot(),
                        reservation.getResource().getName(),
                        reservation.getReservationStatus()
                );
                return dateReservation;
            });
        } catch (Exception e) {
            throw new MyException("No se pudieron recuperar las reservas.");
        }
    }

    public Page<DataAnswerReservation> findByShiftStatus(ReservationShiftStatus reservationShiftStatus, Pageable pageable) throws MyException {
        //realizar validacion de turno (si hay 2 horarios iguales con el mismo recurso)
        try {
            Page<Reservation> reservations = reservationRepository.findReservationByShiftStatus(reservationShiftStatus, pageable);
            return reservations.map(reservation -> {

                DataAnswerReservation dataAnswerReservation = new DataAnswerReservation(
                        reservation.getUser().getEmail(),
                        reservation.getStartDate(),
                        reservation.getReservationShiftStatus(),
                        reservation.getSelectedTimeSlot(),
                        reservation.getResource().getName(),
                        reservation.getReservationStatus()
                );
                return dataAnswerReservation;
            });
        } catch (Exception e) {
            throw new MyException("No se pudieron recuperar las reservas.");
        }
    }

    public Page<DataAnswerReserveByDniUser> findByUserDni(String user, Pageable pageable) throws MyException {

        try {
            Page<Reservation> reservationByUserDni = reservationRepository.findReservationByUserDni(user, pageable);
            return reservationByUserDni.map(reservation -> {

                DataAnswerReserveByDniUser dataAnswerReserveByDniUser = new DataAnswerReserveByDniUser(

                        reservation.getId(),
                        reservation.getStartDate(),
                        reservation.getReservationShiftStatus(),
                        reservation.getSelectedTimeSlot(),
                        reservation.getResource().getId(),
                        reservation.getResource().getName(),
                        reservation.getReservationStatus()
                );
                return dataAnswerReserveByDniUser;
            });
        } catch (Exception e) {
            throw new MyException("No se pudo realizar la búsqueda.");
        }
    }

    //borrado fisico solo para admin
    public Reservation deleteReserve(Long id) throws MyException {
        User user = userService.getCurrentUser();
        Reservation reservation = reservationRepository.findById(id).orElseThrow();

        if (!reservationRepository.existsById(id)) {
            throw new MyException("No se encuentra la reserva.");
        }

        if(user.getRole() == Role.ADMIN){
            reservationRepository.deleteById(id);
            Resource resource = reservation.getResource();
            resource.setStatus(ResourceStatus.AVAILABLE);
            resourceRepository.save(resource);
        }
        validateReservationPermissions.validateAuthority(reservation);
        return reservation;
    }
}