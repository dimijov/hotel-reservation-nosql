package rs.ac.bg.fon.is.hotel_reservation_nosql.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "sobe")
public class Soba {

    @Id
    private ObjectId id;

    @Field("naziv")
    private String naziv;

    @Field("kapacitet")
    private int kapacitet;

    @Field("opis")
    private String opis;

    @Field("cena_po_noci")
    private double cenaPoNoci;

    @Field("slika_url")
    private String slikaUrl;
}

