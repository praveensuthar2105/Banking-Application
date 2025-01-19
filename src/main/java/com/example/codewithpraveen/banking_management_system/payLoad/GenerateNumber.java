package com.example.codewithpraveen.banking_management_system.payLoad;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GenerateNumber {
	
	private static final Random random = new Random();
	private static final Set<Long> generatedNumbers = new HashSet<>();
	
	public static long generateUniqueNumber(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Number of digits must be greater than 0");
		}
		
		long min = (long) Math.pow(10, n - 1);
		long max = (long) Math.pow(10, n) - 1;
		
		long number;
		do {
			number = min + ((long) (random.nextDouble() * (max - min)));
		} while (generatedNumbers.contains(number));
		
		if (number < 0) {
			number = -number;
			generatedNumbers.add(number);
		} else {
			generatedNumbers.add(number);
		}
		
		
		return  number;
	}
	
	
	
	
}
