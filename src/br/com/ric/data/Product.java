package br.com.ric.data;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.math.RoundingMode.HALF_UP;

import java.io.Serializable;

import static br.com.ric.data.Rating.*;

public abstract class Product implements Rateable <Product>, Serializable{

	public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);
	private int id; 
	private String name;
	private BigDecimal price;
	private Rating rating;
	
		
//	Product() {
//		this(0, null, BigDecimal.valueOf(0));
//	}
	
	Product(int id, String name, BigDecimal price, Rating rating) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.rating = rating;
	}
	
	Product(int id, String name, BigDecimal price) {
		this(id, name, price, NOT_RATED);
	}

	public int getId() {
		return id;
	}
	
//	public void setId(final int id) {
//		this.id = id;
//	}
	
	public String getName() {
		return name;
	}
	
//	public void setName(final String name) {
//		this.name = name;
//	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
//	public void setPrice(final BigDecimal price) {
//		this.price = price;
//	}
	
	public BigDecimal getDiscount() {
		return price.multiply(DISCOUNT_RATE).setScale(2, HALF_UP);
	}
	
	@Override
	public Rating getRating() { 
		return rating;
	}
	
//	public abstract Product applyRating(Rating newRating); 
		
	public LocalDate getBestBefore() {
		return LocalDate.now();
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", rating=" + rating.getStars() + " ]" + getBestBefore();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Product) {
			final Product other = (Product) obj;
			return this.id == other.id;
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
}
