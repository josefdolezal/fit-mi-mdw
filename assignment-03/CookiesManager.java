package agency;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookiesManager {
	private SessionService sessionService;
	
	private static final String SESSION_ID_KEY = "SESSION_ID";
	
	public CookiesManager(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	
	public Session readSessionFromCookies(Cookie[] cookies) {
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(SESSION_ID_KEY)) {
				int sessionId = Integer.parseInt(cookie.getValue());
				Session session = sessionService.getSession(sessionId);
				
				if(session == null) {
					break;
				}
				
				return session;
			}
		}
		
		return sessionService.createSession();
	}
	
	public void storeSession(int id, HttpServletResponse response) {
		setCookie(SESSION_ID_KEY, Integer.toString(id), response);
	}
	
	public void destroySession(int id, HttpServletResponse response) {
		setCookie(SESSION_ID_KEY, null, response);
	}
	
	private void setCookie(String key, String value, HttpServletResponse response) {
		Cookie sessionCookie = new Cookie(key, value);
		response.addCookie(sessionCookie);
	}
}
