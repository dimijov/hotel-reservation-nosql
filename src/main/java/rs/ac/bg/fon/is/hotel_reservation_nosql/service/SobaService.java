package rs.ac.bg.fon.is.hotel_reservation_nosql.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.is.hotel_reservation_nosql.dto.SobaDTO;

import java.util.List;

@Repository
public interface SobaService {
    List<SobaDTO> getAllAvailableRooms();
    SobaDTO getRoomById(String id);
}
