package rs.ac.bg.fon.is.hotel_reservation_nosql.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import rs.ac.bg.fon.is.hotel_reservation_nosql.model.Soba;

public interface SobaRepository extends MongoRepository<Soba, ObjectId> {
}
