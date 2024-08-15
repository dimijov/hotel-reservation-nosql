package rs.ac.bg.fon.is.hotel_reservation_nosql.dao;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import rs.ac.bg.fon.is.hotel_reservation_nosql.model.Gost;

public interface GostRepository extends MongoRepository<Gost, ObjectId> {
}
