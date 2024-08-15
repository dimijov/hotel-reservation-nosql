package rs.ac.bg.fon.is.hotel_reservation_nosql.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SobaDTO {

    private String id;

    @NotBlank(message = "Naziv je obavezan")
    private String naziv;

    @Min(value = 1, message = "Kapacitet mora biti najmanje 1")
    private int kapacitet;

    @NotBlank(message = "Opis je obavezan")
    private String opis;

    @Min(value = 0, message = "Cena po noci mora biti pozitivan broj")
    private double cenaPoNoci;

    @NotBlank(message = "URL slike je obavezan")
    private String slikaUrl;
}
