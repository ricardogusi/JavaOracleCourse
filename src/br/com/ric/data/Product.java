package br.com.ric.data;

import java.math.BigDecimal;
import static java.math.RoundingMode.HALF_UP;
import static br.com.ric.data.Rating.*;

public  class Product {

	public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);
	private int id; 
	private String name;
	private BigDecimal price;
	private Rating rating;
	
		
	public Product() {
		this(0, null, BigDecimal.valueOf(0));
	}
	
	public Product(int id, String name, BigDecimal price, Rating rating) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.rating = rating;
	}
	
	public Product(int id, String name, BigDecimal price) {
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
	
	public Rating getRating() { 
		return rating;
	}
	
	public Product applyRating(Rating newRating) {
		return new Product(id, name, price, newRating);
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", rating=" + rating.getStars() + "]";
	}
	
	
	
	
}
