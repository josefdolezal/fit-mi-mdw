package proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weblogic.servlet.annotation.WLServlet;

/**
 * Servlet implementation class ProxyServlet
 */
@WLServlet(name="ProxyServlet", mapping = {"/proxy"})
public class ProxyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Service[] services;
       
    public ProxyServlet() {
        super();
        
        services = new Service[] {
        		new Service("http://147.32.233.18:8888/MI-MDW-LastMinute1/list"),
        		new Service("http://147.32.233.18:8888/MI-MDW-LastMinute2/list"),
        		new Service("http://147.32.233.18:8888/MI-MDW-LastMinute3/list")
        };
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        benchmarkServices();
    	// url
        String url = selectService();
        HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
        // HTTP method
        connection.setRequestMethod("GET");
        
        System.out.println("Selected service: " + url);
        
        copyRequestIntoConnection(request, response, connection);
    }
    
    private void copyRequestIntoConnection(HttpServletRequest request, HttpServletResponse response, HttpURLConnection connection) throws IOException {
    	// copy headers
    	Enumeration<String> originalHeaders = request.getHeaderNames();
        
        while(originalHeaders.hasMoreElements()) {
        	String headerName = originalHeaders.nextElement();
        	String headerValue = request.getHeader(headerName);
        	
        	connection.setRequestProperty(headerName, headerValue);
        }
        
        // copy body
        BufferedReader inputStream = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        ServletOutputStream sout = response.getOutputStream();
        while ((inputLine = inputStream.readLine()) != null) {
            sout.write(inputLine.getBytes());
        }
        // close
        inputStream.close();
        sout.flush();
    }
    
    private String selectService() {
    	return services[0].getUrl();
    }
    
    private void benchmarkServices() throws MalformedURLException, IOException {
    	for(int i = 0; i < services.length; ++i) {
    		 Service service = services[i];
    		 HttpURLConnection connection = (HttpURLConnection) (new URL(service.getUrl())).openConnection();
    		 
    		 connection.setRequestMethod("GET");
    		 
    		 int healthStatus = connection.getResponseCode() == 200 ? 1 : -1;
    		 
    		 service.setStatus(service.getStatus() + healthStatus);
    	}
    	
    	Arrays.sort(services, Collections.reverseOrder());
    	System.out.println(services.toString());
    }
}
