package converter.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CurrencyConverterInterface extends Remote {
	public Double convert(Currency from, Currency to, int amount) throws RemoteException;
}
