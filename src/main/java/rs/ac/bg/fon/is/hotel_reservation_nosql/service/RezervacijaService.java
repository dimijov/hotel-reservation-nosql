package rs.ac.bg.fon.is.hotel_reservation_nosql.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.is.hotel_reservation_nosql.dto.RezervacijaDTO;

@Repository
public interface RezervacijaService {
    RezervacijaDTO createReservation(RezervacijaDTO rezervacijaDTO);
    String cancelReservation(String id);
    RezervacijaDTO getReservationByEmailAndToken(String email, String token);
}
