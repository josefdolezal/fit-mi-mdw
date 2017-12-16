package flights;

import java.util.Date;

public class Destination {
	public String identifier;
	public Date date;
	
	public Destination(String identifier, Date date) {
		this.identifier = identifier;
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "Destination: " + identifier + " at " + date.toString();
	}
}
