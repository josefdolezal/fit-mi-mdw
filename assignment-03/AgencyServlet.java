package agency;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weblogic.servlet.annotation.WLServlet;

@WLServlet(name = "AgencyServlet", mapping = {"/booking"})
public class AgencyServlet extends HttpServlet {
	
	private SessionService sessionService = SessionService.getInstance();
	private CookiesManager cookiesManager = new CookiesManager(sessionService);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cookie[] cookies = (request.getCookies() != null) ? request.getCookies() : new Cookie[]{};
		Session session = cookiesManager.readSessionFromCookies(cookies);
		PrintWriter writer = response.getWriter();
		
		cookiesManager.storeSession(session.id, response);
		String message = processSession(session);
		
		writer.append(message);
	}
	
	private String processSession(Session session) {	
		switch(session.state) {
		case NEW:
			sessionService.updateSession(session.id, BookingState.WAITING_FOR_PAYMENT);
			return "Your booking is accepted. Your order ID: " + session.id + ". Awaiting payment..";
		case WAITING_FOR_PAYMENT:
			sessionService.updateSession(session.id, BookingState.COMPLETED);
			return "Thank you for payment, proceed to booking summary.";
		case COMPLETED:
			sessionService.destroySession(session.id);
			return "Your booking is completed, see you next time.";
		}
		
		return null;
	}
}
