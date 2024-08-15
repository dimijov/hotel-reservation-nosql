package rs.ac.bg.fon.is.hotel_reservation_nosql.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "gosti")
public class Gost {
    @Id
    private ObjectId id;


    @Field("ime")
    private String ime;

    @Field("prezime")
    private String prezime;

    @DBRef
    private Rezervacija rezervacija;
}