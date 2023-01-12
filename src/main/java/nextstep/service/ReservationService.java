package nextstep.service;

import java.util.List;
import nextstep.domain.Reservation;
import nextstep.domain.ReservationInfo;
import nextstep.domain.Theme;
import nextstep.exceptions.ErrorCode;
import nextstep.exceptions.ReservationException;
import nextstep.repository.WebReservationDAO;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final WebReservationDAO webReservationDAO;
    private final Theme theme = new Theme("워너고홈", "병맛 어드벤처 회사 코믹물", 29_000);

    public ReservationService(WebReservationDAO webReservationDAO) {
        this.webReservationDAO = webReservationDAO;
    }

    public Long createReservation(Reservation reservation) {
        List<Reservation> reservationsByDateAndTime = webReservationDAO.findByDateAndTime(
                reservation.getDate(), reservation.getTime());
        if (reservationsByDateAndTime.size() > 0) {
            throw new ReservationException(ErrorCode.ALREADY_RESERVATION_EXISTS);
        }

        Reservation newReservation = new Reservation(reservation.getDate(), reservation.getTime(), reservation.getName(), theme);
        return webReservationDAO.insertWithKeyHolder(newReservation);
    }

    public ReservationInfo lookupReservation(Long id) {
        Reservation reservation = webReservationDAO.findById(id);
        return new ReservationInfo(reservation);
    }

    public ReservationInfo deleteReservation(Long id) {
        Reservation reservation = webReservationDAO.findById(id);
        webReservationDAO.delete(id);
        return new ReservationInfo(reservation);
    }
}