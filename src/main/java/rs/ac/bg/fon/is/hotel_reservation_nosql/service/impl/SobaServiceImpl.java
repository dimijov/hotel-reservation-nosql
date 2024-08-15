package rs.ac.bg.fon.is.hotel_reservation_nosql.service.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.is.hotel_reservation_nosql.dao.SobaRepository;
import rs.ac.bg.fon.is.hotel_reservation_nosql.dto.SobaDTO;
import rs.ac.bg.fon.is.hotel_reservation_nosql.model.Soba;
import rs.ac.bg.fon.is.hotel_reservation_nosql.service.SobaService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SobaServiceImpl implements SobaService {

    @Autowired
    private SobaRepository sobaRepository;

    @Override
    public List<SobaDTO> getAllAvailableRooms() {
        List<Soba> sobe = sobaRepository.findAll();
        // Ručno mapiranje liste objekata Soba u listu SobaDTO
        return sobe.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SobaDTO getRoomById(String id) {
        ObjectId _id = new ObjectId(id);
        Soba soba = sobaRepository.findById(_id)
                .orElseThrow(() -> new RuntimeException("Soba nije pronađena"));
        // Ručno mapiranje jednog objekta Soba u SobaDTO
        return mapToDTO(soba);
    }

    // Metoda za ručno mapiranje iz Soba modela u SobaDTO
    private SobaDTO mapToDTO(Soba soba) {
        SobaDTO sobaDTO = new SobaDTO();
        sobaDTO.setId(soba.getId().toString());
        sobaDTO.setNaziv(soba.getNaziv());
        sobaDTO.setKapacitet(soba.getKapacitet());
        sobaDTO.setOpis(soba.getOpis());
        sobaDTO.setCenaPoNoci(soba.getCenaPoNoci());
        sobaDTO.setSlikaUrl(soba.getSlikaUrl());
        return sobaDTO;
    }

    // Metoda za ručno mapiranje iz SobaDTO u Soba model
    private Soba mapToEntity(SobaDTO sobaDTO) {
        Soba soba = new Soba();
        if (sobaDTO.getId() != null) {
            soba.setId(new ObjectId(sobaDTO.getId()));
        }
        soba.setNaziv(sobaDTO.getNaziv());
        soba.setKapacitet(sobaDTO.getKapacitet());
        soba.setOpis(sobaDTO.getOpis());
        soba.setCenaPoNoci(sobaDTO.getCenaPoNoci());
        soba.setSlikaUrl(sobaDTO.getSlikaUrl());
        return soba;
    }
}
