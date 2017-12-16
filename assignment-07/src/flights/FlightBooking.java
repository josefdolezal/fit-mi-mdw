package flights;

public class FlightBooking {
	public int id;
	public Customer customer;
	public Destination departure;
	public Destination arrival;
	
	public FlightBooking(int id, Customer customer, Destination departure, Destination arrival) {
		this.id = id;
		this.customer = customer;
		this.departure = departure;
		this.arrival = arrival;
	}
	
	@Override
	public String toString() {
		return "Booking (" + id + "):\n  "
				+ customer.toString()
				+ "\n  " + "From " + departure.toString()
				+ "\n  " + "To " + arrival.toString();
	}
}
