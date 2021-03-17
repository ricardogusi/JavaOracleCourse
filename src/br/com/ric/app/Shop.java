package br.com.ric.app;

import java.math.BigDecimal;
import java.util.Comparator;

import br.com.ric.data.Product;
import br.com.ric.data.ProductManager;
import br.com.ric.data.Rating;

public class Shop {
	public static void main(String[] args) {
		
		
		ProductManager pm = new ProductManager("pt-BR");
		
		
		pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
//		pm.printProductReport(101);
		pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
		pm.reviewProduct(101, Rating.TWO_STAR, "Rather weak tea");
		pm.reviewProduct(101, Rating.FOUR_STAR, "Fine tea");
		pm.reviewProduct(101, Rating.FOUR_STAR, "Good tea");
		pm.reviewProduct(101, Rating.FIVE_STAR, "Nice hot cup of tea");
		pm.reviewProduct(101, Rating.THREE_STAR, "Just add some lemon");
		
//		pm.printProductReport(101);

//		pm.changeLocale("pt-BR");
		
		pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99),Rating.NOT_RATED);
		pm.reviewProduct(102, Rating.THREE_STAR, "Coffee was ok");
		pm.reviewProduct(102, Rating.ONE_STAR, "Where is the mikl?");
		pm.reviewProduct(102, Rating.FIVE_STAR, "It's perfect with ten spoons of sugar!");
//		pm.printProductReport(102);
		
		pm.createProduct(103, "Cake", BigDecimal.valueOf(5.99), Rating.NOT_RATED);
		pm.reviewProduct(103, Rating.FOUR_STAR, "Cake was yummy");
		pm.reviewProduct(103, Rating.TWO_STAR, "A bit bitter");
		 pm.reviewProduct(103, Rating.FIVE_STAR, "Delicious");
		pm.printProductReport(103);		
		
		Comparator<Product> ratingSorter = (p1,p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
					
		pm.printProducts(p -> p.getPrice().floatValue() < 2, ratingSorter);
		
		pm.getDiscounts().forEach((rating, discount) ->System.out.println(rating + "\t" + discount));
		
//		pm.printProducts((p1,p2) -> p2.getPrice().compareTo(p1.getPrice()));
	}

}
