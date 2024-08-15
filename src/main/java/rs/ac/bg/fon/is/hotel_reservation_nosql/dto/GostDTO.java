package rs.ac.bg.fon.is.hotel_reservation_nosql.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GostDTO {

    @NotBlank(message = "Ime je obavezno")
    private String ime;

    @NotBlank(message = "Prezime je obavezno")
    private String prezime;

    @JsonIgnore
    private RezervacijaDTO rezervacija;
}
