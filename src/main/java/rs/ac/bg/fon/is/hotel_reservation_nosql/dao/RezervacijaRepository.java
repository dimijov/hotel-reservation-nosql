package rs.ac.bg.fon.is.hotel_reservation_nosql.dao;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import rs.ac.bg.fon.is.hotel_reservation_nosql.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation_nosql.model.Soba;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RezervacijaRepository extends MongoRepository<Rezervacija, ObjectId> {
    List<Rezervacija> findBySobaIdAndDatumPocetkaLessThanEqualAndDatumZavrsetkaGreaterThanEqual(ObjectId sobaId, LocalDate datumZavrsetka, LocalDate datumPocetka);
    Optional<Rezervacija> findByEmailAndToken(String email, String token);
    Rezervacija findByPromoKodAndAktivna(String promoKod, boolean aktivna);
}
