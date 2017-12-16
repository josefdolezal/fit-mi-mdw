package flights;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

@WebService
public class FlighManager {
	
	int lastId = 0;
	List<FlightBooking> bookings = new ArrayList<FlightBooking>();
	
	public String addBooking(String firstName, String lastName, String from, String to, Date dateFrom, Date dateTo) {
		Destination departure = new Destination(from, dateFrom);
		Destination arrival = new Destination(to, dateTo);
		Customer customer = new Customer(firstName, lastName);
		FlightBooking booking = new FlightBooking(++lastId, customer, departure, arrival);
		
		bookings.add(booking);

		return booking.toString();
	}
	
	public boolean updateBooking(int id, String firstName, String lastName) {
		for(int i = 0; i < bookings.size(); ++i) {
			FlightBooking booking = bookings.get(i);
			
			if(booking.id == id) {
				booking.customer.firstName = firstName;
				booking.customer.lastName = lastName;
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean removeBooking(int id) {
		for(int i = 0; i < bookings.size(); ++i) {
			FlightBooking booking = bookings.get(i);
			
			if(booking.id == id) {
				bookings.remove(i);
				return true;
			}
		}
		
		return false;
	}
	
	public List<String> allBookings() {
		List<String> formattedBookings = new ArrayList<String>();
		
		for(int i = 0; i < bookings.size(); ++i) {
			formattedBookings.add(bookings.get(i).toString());
		}
		
		return formattedBookings;
	}
}
