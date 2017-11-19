package converter.client;

import converter.server.CurrencyConverterInterface;

import java.rmi.Naming;

import converter.server.Currency;

public class CurrencyConverterClient {
	public static void main(String[] args) throws Exception {
		CurrencyConverterInterface service =
				(CurrencyConverterInterface) Naming.lookup("//localhost/CurrencyConverter");
		
		System.out.println(service.convert(Currency.GBP, Currency.EUR, 10));
	}
}
