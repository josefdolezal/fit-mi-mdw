package converter.server;

public class CurrencyConverter {
	private static final Double GBP_TO_EUR_RATE = 1.12;
	private static final Double GBP_TO_USD_RATE = 1.32;
	private static final Double EUR_TO_USD_RATE = 1.18;
	
	public static Double convert(Currency from, Currency to, int amount) {
		switch(from) {
		case GBP:
			if(to == Currency.EUR) {
				return amount * GBP_TO_EUR_RATE;
			} else if(to == Currency.USD) {
				return amount * GBP_TO_USD_RATE;
			}
			break;
		case EUR:
			if(to == Currency.GBP) {
				return amount * (1.0/GBP_TO_EUR_RATE);
			} else if(to == Currency.USD) {
				return amount * EUR_TO_USD_RATE;
			}
			break;
		case USD:
			if(to == Currency.GBP) {
				return amount * (1.0/GBP_TO_USD_RATE);
			} else if(to == Currency.EUR) {
				return amount * (1.0/EUR_TO_USD_RATE);
			}
			break;
		}
		
		return (double) amount;
	}
}
