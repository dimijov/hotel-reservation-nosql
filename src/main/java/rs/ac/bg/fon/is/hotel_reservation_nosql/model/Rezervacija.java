package rs.ac.bg.fon.is.hotel_reservation_nosql.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "rezervacije")
public class Rezervacija {
    @Id
    private ObjectId id;

    @Field("email")
    private String email;

    @Field("datum_pocetka")
    private LocalDate datumPocetka;

    @Field("datum_zavrsetka")
    private LocalDate datumZavrsetka;

    @Field("promo_kod")
    private String promoKod;

    @Field("token")
    private String token;

    @Field("soba_id")
    private ObjectId sobaId;

    @Field("gosti")
    private List<Gost> gosti;

    @Field("ukupna_cena")
    private double ukupnaCena;

    @Field("aktivna")
    private boolean aktivna;

    @Field("popust")
    private double popust;
}

