package rs.ac.bg.fon.is.hotel_reservation_nosql.service.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.is.hotel_reservation_nosql.dao.RezervacijaRepository;
import rs.ac.bg.fon.is.hotel_reservation_nosql.dao.SobaRepository;
import rs.ac.bg.fon.is.hotel_reservation_nosql.dto.GostDTO;
import rs.ac.bg.fon.is.hotel_reservation_nosql.dto.RezervacijaDTO;
import rs.ac.bg.fon.is.hotel_reservation_nosql.dto.SobaDTO;
import rs.ac.bg.fon.is.hotel_reservation_nosql.exception.BadRequestException;
import rs.ac.bg.fon.is.hotel_reservation_nosql.exception.NotFoundException;
import rs.ac.bg.fon.is.hotel_reservation_nosql.model.Gost;
import rs.ac.bg.fon.is.hotel_reservation_nosql.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation_nosql.model.Soba;
import rs.ac.bg.fon.is.hotel_reservation_nosql.service.RezervacijaService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RezervacijaServiceImpl implements RezervacijaService {

    @Autowired
    private RezervacijaRepository rezervacijaRepository;

    @Autowired
    private SobaRepository sobaRepository;

    @Override
    @Transactional
    public RezervacijaDTO createReservation(RezervacijaDTO rezervacijaDTO) {
        validateReservationDates(rezervacijaDTO);

        Soba soba = findSoba(rezervacijaDTO.getSoba().getId());
        checkCapacity(soba, rezervacijaDTO);
        checkAvailability(soba, rezervacijaDTO);

        Rezervacija rezervacija = mapDtoToEntity(rezervacijaDTO, soba);

        processPromoKod(rezervacija, soba);
        setReservationParameters(rezervacija, soba);

        rezervacija.setGosti(mapGuests(rezervacijaDTO.getGosti(), rezervacija));

        Rezervacija savedRezervacija = rezervacijaRepository.save(rezervacija);

        return mapEntityToDto(savedRezervacija, soba);
    }

    @Override
    @Transactional(readOnly = true)
    public RezervacijaDTO getReservationByEmailAndToken(String email, String token) {
        Rezervacija rezervacija = rezervacijaRepository.findByEmailAndToken(email, token)
                .orElseThrow(() -> new NotFoundException("Rezervacija nije pronađena"));

        Soba soba = sobaRepository.findById(rezervacija.getSobaId())
                .orElseThrow(() -> new NotFoundException("Soba nije pronađena"));

        return mapEntityToDto(rezervacija, soba);
    }

    @Override
    @Transactional
    public String cancelReservation(String id) {
        ObjectId _id = new ObjectId(id);
        Rezervacija rezervacija = rezervacijaRepository.findById(_id)
                .orElseThrow(() -> new NotFoundException("Rezervacija nije pronađena"));

        checkDates(rezervacija);

        rezervacijaRepository.delete(rezervacija);
        return "Rezervacija je uspešno otkazana";
    }

    private void validateReservationDates(RezervacijaDTO rezervacijaDTO) {
        if (rezervacijaDTO.getDatumPocetka().isAfter(rezervacijaDTO.getDatumZavrsetka())) {
            throw new BadRequestException("Datum početka rezervacije mora biti pre datuma završetka.");
        }
        if (rezervacijaDTO.getDatumPocetka().isBefore(LocalDate.now())) {
            throw new BadRequestException("Datum početka rezervacije mora biti u budućnosti.");
        }
    }

    private Soba findSoba(String id) {
        ObjectId _id = new ObjectId(id);
        return sobaRepository.findById(_id)
                .orElseThrow(() -> new NotFoundException("Soba nije pronađena"));
    }

    private void checkCapacity(Soba soba, RezervacijaDTO rezervacijaDTO) {
        if (rezervacijaDTO.getGosti().size() > soba.getKapacitet()) {
            throw new BadRequestException("Broj gostiju ne sme biti veći od kapaciteta sobe.");
        }
    }

    private void checkAvailability(Soba soba, RezervacijaDTO rezervacijaDTO) {
        boolean isAvailable = rezervacijaRepository.findBySobaIdAndDatumPocetkaLessThanEqualAndDatumZavrsetkaGreaterThanEqual(
                soba.getId(), rezervacijaDTO.getDatumZavrsetka(), rezervacijaDTO.getDatumPocetka()).isEmpty();
        if (!isAvailable) {
            throw new BadRequestException("Soba nije dostupna u zadatom periodu.");
        }
    }

    private void processPromoKod(Rezervacija rezervacija, Soba soba) {
        if (rezervacija.getPromoKod() != null && !rezervacija.getPromoKod().isEmpty()) {
            Rezervacija existingRezervacija = rezervacijaRepository.findByPromoKodAndAktivna(rezervacija.getPromoKod(), true);
            if (existingRezervacija != null) {
                if (!existingRezervacija.getEmail().equals(rezervacija.getEmail())) {
                    rezervacija.setUkupnaCena(calculateDiscountedPrice(soba.getCenaPoNoci(), existingRezervacija.getPopust()));
                    existingRezervacija.setAktivna(false);
                    rezervacijaRepository.save(existingRezervacija);
                    rezervacija.setAktivna(true);
                } else {
                    throw new BadRequestException("Promo kod ne može biti korišćen za isti email.");
                }
            } else {
                throw new BadRequestException("Promo kod nije aktivan ili ne postoji.");
            }
        } else {
            rezervacija.setPopust(0); // No discount if no promo code is provided
        }
    }

    private void setReservationParameters(Rezervacija rezervacija, Soba soba) {
        if (!rezervacija.isAktivna()) {
            rezervacija.setUkupnaCena(calculateDiscountedPrice(soba.getCenaPoNoci(), rezervacija.getPopust()));
            rezervacija.setAktivna(true);
        }
        rezervacija.setToken(UUID.randomUUID().toString());
        rezervacija.setPopust(generateRandomPopust());
        rezervacija.setPromoKod(generatePromoKod());
    }

    private List<Gost> mapGuests(List<GostDTO> gostDTOs, Rezervacija rezervacija) {
        return gostDTOs.stream()
                .map(dto -> {
                    Gost gost = new Gost();
                    gost.setIme(dto.getIme());
                    gost.setPrezime(dto.getPrezime());
                    gost.setRezervacija(rezervacija); // Link guest to reservation
                    return gost;
                })
                .collect(Collectors.toList());
    }

    private RezervacijaDTO mapEntityToDto(Rezervacija rezervacija, Soba soba) {
        RezervacijaDTO dto = new RezervacijaDTO();
        dto.setId(rezervacija.getId().toString());
        dto.setEmail(rezervacija.getEmail());
        dto.setDatumPocetka(rezervacija.getDatumPocetka());
        dto.setDatumZavrsetka(rezervacija.getDatumZavrsetka());
        dto.setPromoKod(rezervacija.getPromoKod());
        dto.setPopust(rezervacija.getPopust());
        dto.setUkupnaCena(rezervacija.getUkupnaCena());
        dto.setSoba(mapSobaToDto(soba));
        dto.setGosti(mapGostiToDto(rezervacija.getGosti()));
        return dto;
    }

    private Rezervacija mapDtoToEntity(RezervacijaDTO rezervacijaDTO, Soba soba) {
        Rezervacija rezervacija = new Rezervacija();
        if (rezervacijaDTO.getId() != null && !rezervacijaDTO.getId().isEmpty()) {
            rezervacija.setId(new ObjectId(rezervacijaDTO.getId()));
        } else {
            rezervacija.setId(new ObjectId()); // Create new ObjectId if not provided
        }
        rezervacija.setEmail(rezervacijaDTO.getEmail());
        rezervacija.setDatumPocetka(rezervacijaDTO.getDatumPocetka());
        rezervacija.setDatumZavrsetka(rezervacijaDTO.getDatumZavrsetka());
        rezervacija.setPromoKod(rezervacijaDTO.getPromoKod());
        rezervacija.setPopust(rezervacijaDTO.getPopust());
        rezervacija.setUkupnaCena(rezervacijaDTO.getUkupnaCena());
        rezervacija.setSobaId(soba.getId()); // Set Soba object in reservation
        return rezervacija;
    }

    private SobaDTO mapSobaToDto(Soba soba) {
        SobaDTO dto = new SobaDTO();
        dto.setId(soba.getId().toString());
        dto.setNaziv(soba.getNaziv());
        dto.setKapacitet(soba.getKapacitet());
        dto.setCenaPoNoci(soba.getCenaPoNoci());
        dto.setOpis(soba.getOpis());
        dto.setSlikaUrl(soba.getSlikaUrl());
        return dto;
    }

    private List<GostDTO> mapGostiToDto(List<Gost> gosti) {
        return gosti.stream()
                .map(gost -> {
                    GostDTO dto = new GostDTO();
                    dto.setIme(gost.getIme());
                    dto.setPrezime(gost.getPrezime());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private double calculateDiscountedPrice(double originalPrice, double discountPercentage) {
        return originalPrice - (originalPrice * (discountPercentage / 100));
    }

    private String generatePromoKod() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder promoKod = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            promoKod.append(characters.charAt(random.nextInt(characters.length())));
        }
        return promoKod.toString();
    }

    private double generateRandomPopust() {
        int[] popusti = {5, 10, 15, 20};
        return popusti[new Random().nextInt(popusti.length)];
    }

    private void checkDates(Rezervacija rezervacija) {
        if (rezervacija.getDatumZavrsetka().isBefore(LocalDate.now())) {
            throw new BadRequestException("Rezervacija ne može biti otkazana jer je datum završetka prošao.");
        }
    }
}
