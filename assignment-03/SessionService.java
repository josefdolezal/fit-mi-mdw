package agency;

import java.util.HashMap;
import java.util.Random;

public class SessionService {
	
	private static SessionService instance;
	private Random random = new Random();
	private HashMap<Integer, Session> storage = new HashMap<>();
	
	private SessionService() { }
	
	public static SessionService getInstance() {
		if(instance == null) {
			instance = new SessionService();
		}
		
		return instance;
	}
	
	public Session createSession() {
		int id = random.nextInt();
		Session session = new Session(id, BookingState.NEW);
		
		storage.put(id, session);
		
		return session;
	}
	
	public void updateSession(int id, BookingState state) {
		Session session = storage.get(id);
		
		if(session == null) {
			return;
		}
		
		session.setBookingState(state);
	}
	
	public Session getSession(int id) {
		return storage.get(id);
	}
	
	public void destroySession(int id) {
		storage.remove(id);
	}
}
