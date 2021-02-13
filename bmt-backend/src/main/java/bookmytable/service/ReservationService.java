package bookmytable.service;


import bookmytable.dao.ReservationRepository;
import bookmytable.model.Customer;
import bookmytable.model.Reservation;
import bookmytable.model.Restaurant;
import bookmytable.model.RestaurantTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Transactional
    public Reservation makeReservation(Time startTime, Time endTime, Date date, int groupSize, long id, RestaurantTable restaurantTable,
                                       Customer customer, Restaurant restaurant) {
        String error = "";

        if (startTime == null || endTime == null || date == null) {
            error += "Please select an available reservation time";
        }

        if (startTime.after(endTime)) {
            error += "Start time must be before end time";
        }

        if (restaurantTable == null) {
            error += "Please select an available table";
        }

        if (!checkAvailability(startTime, endTime, date, restaurantTable)) {
            error += "RestaurantTable is not available at that time";
        }

        if (groupSize < 0 || (restaurantTable != null && groupSize > restaurantTable.getCapacity())) {
            error += "Please enter acceptable group size";
        }

        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }


        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setDate(date);
        reservation.setGroupSize(groupSize);
        reservation.setId(id);
        reservation.setRestaurant(restaurant);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setTable(restaurantTable);
        reservationRepository.save(reservation);

        return reservation;

    }


    @Transactional
    public Reservation getReservationById(long id) {
        return reservationRepository.findReservationById(id);
    }

    @Transactional
    public List<Reservation> getReservationByTable(RestaurantTable restaurantTable) {
        return toList(reservationRepository.findReservationsByTable(restaurantTable));
    }

    @Transactional
    public List<Reservation> getReservationsByTableAndStartTime(RestaurantTable restaurantTable, Time startTime) {
        return toList(reservationRepository.findReservationsByTableAndStartTime(restaurantTable, startTime));
    }

    @Transactional
    public List<Reservation> getReservationsByTableAndStartTimeBetween(RestaurantTable restaurantTable, Time minStartTime, Time maxStartTime) {
        return toList(reservationRepository.findReservationsByTableAndStartTimeBetween(restaurantTable, minStartTime, maxStartTime));
    }

    @Transactional
    public List<Reservation> getReservationsByTableAndDate(RestaurantTable restaurantTable, Date date) {
        return toList(reservationRepository.findReservationsByTableAndDate(restaurantTable, date));
    }

    @Transactional
    public Reservation getReservationsByTableAndStartTimeAndDate(RestaurantTable restaurantTable, Time startTime, Date date) {
        return reservationRepository.findReservationsByTableAndStartTimeAndDate(restaurantTable, startTime, date);
    }

    @Transactional
    public List<Reservation> getReservationsByTableAndStartTimeBetweenAndDate(RestaurantTable restaurantTable, Time minStartTime, Time maxStartTime, Date date) {
        return toList(reservationRepository.findReservationsByTableAndStartTimeBetweenAndDate(restaurantTable, minStartTime, maxStartTime, date));
    }

    @Transactional
    public List<Reservation> getReservationsByRestaurant(Restaurant restaurant) {
        return reservationRepository.findReservationsByRestaurant(restaurant);
    }

    @Transactional
    public List<Reservation> getReservationsByRestaurantAndStartTime(Restaurant restaurant, Time startTime) {
        return toList(reservationRepository.findReservationsByRestaurantAndStartTime(restaurant, startTime));
    }

    @Transactional
    public List<Reservation> getReservationsByRestaurantAndStartTimeBetween(Restaurant restaurant, Time minStartTime, Time maxStartTime) {
        return toList(reservationRepository.findReservationsByRestaurantAndStartTimeBetween(restaurant, minStartTime, maxStartTime));
    }

    @Transactional
    public List<Reservation> getReservationsByRestaurantAndDate(Restaurant restaurant, Date date) {
        return toList(reservationRepository.findReservationsByRestaurantAndDate(restaurant, date));
    }

    @Transactional
    public List<Reservation> getReservationsByRestaurantAndStartTimeAndDate(Restaurant restaurant, Time startTime, Date date) {
        return toList(reservationRepository.findReservationsByRestaurantAndStartTimeAndDate(restaurant, startTime, date));
    }

    @Transactional
    public List<Reservation> getReservationsByRestaurantAndStartTimeBetweenAndDate(Restaurant restaurant, Time minStartTime, Time maxStartTime, Date date) {
        return toList(reservationRepository.findReservationsByRestaurantAndStartTimeBetweenAndDate(restaurant, minStartTime, maxStartTime, date));
    }

    @Transactional
    public List<Reservation> getReservationsByTableAndGroupSize(RestaurantTable restaurantTable, int groupSize) {
        return toList(reservationRepository.findReservationsByTableAndGroupSize(restaurantTable, groupSize));
    }

    @Transactional
    public List<Reservation> getReservationsByTableAndGroupSizeAndDate(RestaurantTable restaurantTable, int groupSize, Date date) {
        return toList(reservationRepository.findReservationsByTableAndGroupSizeAndDate(restaurantTable, groupSize, date));
    }

    @Transactional
    public List<Reservation> getReservationsByRestaurantAndGroupSize(Restaurant restaurant, int groupSize){
        return toList(reservationRepository.findReservationsByRestaurantAndGroupSize(restaurant,groupSize));
    }

    @Transactional
    public List<Reservation> findReservationsByRestaurantAndGroupSizeAndDate(Restaurant restaurant, int groupSize, Date date){
        return toList(reservationRepository.findReservationsByRestaurantAndGroupSizeAndDate(restaurant,groupSize,date));
    }

    private boolean checkAvailability(Time startTime, Time endTime, Date date, RestaurantTable restaurantTable) {
        for (Reservation r : restaurantTable.getReservations()) {
            if (r.getDate().compareTo(date) == 0 && (startTime.compareTo(r.getEndTime()) < 0 || endTime.compareTo(r.getStartTime()) > 0)) {
                return false;
            }

        }

        return true;
    }

    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

}
