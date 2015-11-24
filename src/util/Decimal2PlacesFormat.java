package util;

import java.text.DecimalFormat;

public class Decimal2PlacesFormat {
	
	public static double formatDouble(double d){
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		double newD = Double.parseDouble(decimalFormat.format(d).replace(",", "."));
		return newD;
	}
	
}
