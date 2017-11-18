package agency;

public class Session {
	int id;
	BookingState state;
	
	public Session(int id, BookingState state) {
		this.id = id;
		this.state = state;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public BookingState getBookingState() {
		return state;
	}
	
	public void setBookingState(BookingState state) {
		this.state = state;
	}
}
