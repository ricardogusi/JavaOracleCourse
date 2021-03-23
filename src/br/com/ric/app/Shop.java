package br.com.ric.app;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.ric.data.Product;
import br.com.ric.data.ProductManager;
import br.com.ric.data.Rating;

public class Shop {
	public static void main(String[] args) {
		
		ProductManager pm = ProductManager.getInstance();
		AtomicInteger clientCount = new AtomicInteger(0);
		
		Callable<String> cliente = () -> {	
			String clientId = "Client " + clientCount.incrementAndGet();
			String threadName = Thread.currentThread().getName();
			int productId = ThreadLocalRandom.current().nextInt(63) +101;
			String languageTag = ProductManager.getSupportedLocales()
							.stream()
							.skip(ThreadLocalRandom.current().nextInt(2))
							.findFirst().get();
			StringBuilder log = new StringBuilder();
			log.append(clientId +" "+ threadName + "\n-\tstart of log\t-\n");
			log.append(pm.getDiscounts(languageTag)
						.entrySet()
						.stream()
						.map(entry -> entry.getKey() + "\t" + entry.getValue())
						.collect(Collectors.joining("\n")));
			log.append("\n-\tend of log\t-\n");
			Product product = pm.reviewProduct(productId, Rating.FOUR_STAR, "Yet another review");
			log.append((product != null)
						? "\nProduct " +productId+ " reviewed\n"
						: "\nProduct " +productId+ " not reviewed");
			pm.printProductReport(productId, languageTag, clientId);
			log.append(clientId+ " generated report for " +productId+" product");
			return log.toString();
		};
		
		
//		Object client = null;
		List<Object> clients = Stream.generate(() -> client).limit(5).collect(Collectors.toList());
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		try {
//			
			List<Future<String>> results = executorService.invokeAll(client)	;	
			executorService.shutdown();
			results.stream().forEach(result -> {
				try {
					System.out.println(result.get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, e);
		}
		
	}

}

		
//		pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
//		pm.parseProduct("D,101,Tea,1.99,0,2021-03-18");
//		pm.printProductReport(101);
//		
//		pm.parseReview("101,4,Nice");
//		pm.parseReview("101,2,Rather weak tea");
//		pm.parseReview("101,3,Fine tea");
//		pm.dumpData();
//		pm.restoreData();
////		
//		pm.printProductReport(107);
//		pm.printProductReport(108);
//		pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
//		pm.reviewProduct(101, Rating.TWO_STAR, "Rather weak tea");
//		pm.reviewProduct(101, Rating.FOUR_STAR, "Fine tea");
//		pm.reviewProduct(101, Rating.FOUR_STAR, "Good tea");
//		pm.reviewProduct(101, Rating.FIVE_STAR, "Nice hot cup of tea");
//		pm.reviewProduct(101, Rating.THREE_STAR, "Just add some lemon");
		
//		pm.printProductReport(101);

//		pm.changeLocale("pt-BR");
		
//		pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99),Rating.NOT_RATED);
//		pm.reviewProduct(102, Rating.THREE_STAR, "Coffee was ok");
//		pm.reviewProduct(102, Rating.ONE_STAR, "Where is the mikl?");
//		pm.reviewProduct(102, Rating.FIVE_STAR, "It's perfect with ten spoons of sugar!");
//		pm.printProductReport(102);
//		
//		pm.createProduct(103, "Cake", BigDecimal.valueOf(5.99), Rating.NOT_RATED);
//		pm.reviewProduct(103, Rating.FOUR_STAR, "Cake was yummy");
//		pm.reviewProduct(103, Rating.TWO_STAR, "A bit bitter");
//		 pm.reviewProduct(103, Rating.FIVE_STAR, "Delicious");
//		pm.printProductReport(103);		
//		
//		Comparator<Product> ratingSorter = (p1,p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
//					
//		pm.printProducts(p -> p.getPrice().floatValue() < 2, ratingSorter);
//		
//		pm.getDiscounts().forEach((rating, discount) ->System.out.println(rating + "\t" + discount));
		
//		pm.printProducts((p1,p2) -> p2.getPrice().compareTo(p1.getPrice()));
