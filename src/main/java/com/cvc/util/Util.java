package com.cvc.util;

import java.time.LocalDate;

public class Util {

	
	public static LocalDate convertStringToLocalDate(String data) {
		 return LocalDate.parse(data);
	}
	
}
