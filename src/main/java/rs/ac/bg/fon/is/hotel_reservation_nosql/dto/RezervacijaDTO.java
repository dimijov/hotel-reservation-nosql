package rs.ac.bg.fon.is.hotel_reservation_nosql.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class RezervacijaDTO {
    private String id;

    @Email(message = "Email mora biti validan")
    @NotEmpty(message = "Email je obavezan")
    private String email;

    @NotNull(message = "Datum pocetka je obavezan")
    @Future(message = "Datum pocetka mora biti u buducnosti")
    private LocalDate datumPocetka;

    @NotNull(message = "Datum zavrsetka je obavezan")
    @Future(message = "Datum zavrsetka mora biti u buducnosti")
    private LocalDate datumZavrsetka;

    private String promoKod;
    private double popust;
    private double ukupnaCena;
    private boolean aktivna;
    private String token;

    @NotNull(message = "Gosti su obavezni")
    private List<@Valid GostDTO> gosti;

    @NotNull(message = "Soba je obavezna")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SobaDTO soba;
}
