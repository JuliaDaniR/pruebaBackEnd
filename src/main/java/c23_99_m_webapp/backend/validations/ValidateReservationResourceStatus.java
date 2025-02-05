package c23_99_m_webapp.backend.validations;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.Reservation;
import c23_99_m_webapp.backend.models.Resource;
import c23_99_m_webapp.backend.models.dtos.ReservationDto;
import c23_99_m_webapp.backend.models.enums.ResourceStatus;
import c23_99_m_webapp.backend.repositories.ReservationRepository;
import c23_99_m_webapp.backend.repositories.ResourceRepository;
import c23_99_m_webapp.backend.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateReservationResourceStatus {

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    ResourceService resourceService;

    @Autowired
    ReservationRepository reservationRepository;

    public Resource validateByResourceStatus(ReservationDto reservationDto) throws MyException {

        Reservation reservation = new Reservation(reservationDto);
        Resource resource = resourceRepository.findById(reservationDto.resourceId()).orElseThrow();

//        boolean isResourceBooked = reservationRepository.existsByResourceIdAndStartDate(resource.getId(), reservation.getStartDate());
        resourceService.validateResourceAvailability(resource.getId(), reservationDto);
//        if (isResourceBooked) {
//            throw new MyException("El recurso ya está reservado para la fecha seleccionada.");
//        }
        return resource;
    }
}
