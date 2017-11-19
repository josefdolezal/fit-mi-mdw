package converter.server;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class CurrencyConverterServer extends UnicastRemoteObject implements CurrencyConverterInterface {

	protected CurrencyConverterServer() throws RemoteException {
		super();
	}
	
	public static void main(String[] args) {
		try {
			if(System.getSecurityManager() == null) {
				RMISecurityManager manager = new RMISecurityManager();
				System.setSecurityManager(manager);
			}
			
			LocateRegistry.createRegistry(8000);
			
			CurrencyConverterServer server = new CurrencyConverterServer();
			
			Naming.rebind("//0.0.0.0/CurrencyConverter", server);
			
			System.out.println("Server up and running!");
		} catch (Exception e) {
			System.out.println("Error: " + e.getLocalizedMessage());
		}
	}

	@Override
	public Double convert(Currency from, Currency to, int amount) throws RemoteException {
		return CurrencyConverter.convert(from, to, amount);
	}


}
