package rs.ac.bg.fon.is.hotel_reservation_nosql.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
