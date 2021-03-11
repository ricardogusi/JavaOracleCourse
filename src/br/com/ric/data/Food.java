package br.com.ric.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Food extends Product {

	
	private LocalDate bestBefore;
	
	public LocalDate getBestBefore() {
		return bestBefore;
	}

	public Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		super(id, name, price, rating);
		this.bestBefore = bestBefore;
	}

	@Override
	public String toString() {
		
		return super.toString() + ", " + bestBefore;
	}
	
	
	
	
}
