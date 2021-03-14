package br.com.ric.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import br.com.ric.data.Drink;
import br.com.ric.data.Food;
import br.com.ric.data.Product;
import br.com.ric.data.ProductManager;
import br.com.ric.data.Rating;

public class Shop {
	public static void main(String[] args) {
		
		Locale BRASIL = new Locale("pt", "BR");
		
		
		ProductManager pm = new ProductManager(BRASIL);
		
		
		Product p1 = pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
		pm.printProductReport();
		p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cup of tea");
		p1 = pm.reviewProduct(p1, Rating.TWO_STAR, "Rather weak tea");
		p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Fine tea");
		p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Good tea");
		p1 = pm.reviewProduct(p1, Rating.FIVE_STAR, "Nice hot cup of tea");
		p1 = pm.reviewProduct(p1, Rating.THREE_STAR, "Just add some lemon");
		
		pm.printProductReport();
//		Product p2 = pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
//		Product p3 = pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//		Product p4 = pm.createProduct(105, "Cookie", BigDecimal.valueOf(3.99), Rating.THREE_STAR, LocalDate.now().plusDays(3));
//		Product p5 = p3.applyRating(Rating.THREE_STAR);
//		Product p6 = pm.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
//		Product p7 = pm.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//		Product p8 = p4.applyRating(Rating.FIVE_STAR);
//		Product p9 = p1.applyRating(Rating.TWO_STAR);
//		
//		System.out.println(p6.equals(p7));
////		System.out.println(p1.getId() + " " + p1.getName() + " " + p1.getPrice() + " " + p1.getDiscount()
////				+ " " + p1.getRating().getStars());
//		
//		System.out.println(p1);
//		System.out.println(p2);
//		System.out.println(p3);
//		System.out.println(p4);
//		System.out.println(p5);
//		System.out.println(p8);
//		System.out.println(p9);
		
		
		
		
	}

}
