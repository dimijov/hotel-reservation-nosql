package rs.ac.bg.fon.is.hotel_reservation_nosql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.is.hotel_reservation_nosql.dto.SobaDTO;
import rs.ac.bg.fon.is.hotel_reservation_nosql.service.SobaService;

import java.util.List;

@RestController
@RequestMapping("/api/sobe")
@Validated
public class SobaController {

    @Autowired
    private SobaService sobaService;

    @GetMapping
    public ResponseEntity<List<SobaDTO>> getAllAvailableRooms() {
        return ResponseEntity.ok(sobaService.getAllAvailableRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SobaDTO> getRoomById(@PathVariable String id) {  // Promenjeno iz Long u String
        return ResponseEntity.ok(sobaService.getRoomById(id));
    }
}
