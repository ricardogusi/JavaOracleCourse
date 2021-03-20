package br.com.ric.data;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {

	private Map<Product, List<Review>> products = new HashMap<>();
	private ResourceFormatter formatter;

	private ResourceBundle config = ResourceBundle.getBundle("br.com.ric.data.config");
	private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
	private MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));

	private Path reportsFolder = Path.of(config.getString("reports.folder"));
	private Path dataFolder = Path.of(config.getString("data.folder"));
	private Path tempFolder = Path.of(config.getString("temp.folder"));

	private static Map<String, ResourceFormatter> formatters = Map.of("en-GB", new ResourceFormatter(Locale.UK),
			"en-US", new ResourceFormatter(Locale.US), "pt-BR",
			new ResourceFormatter(new Locale("pt", "BR")));

	private static final Logger logger = Logger.getLogger(ProductManager.class.getName());

	public ProductManager(Locale locale) {
		this(locale.toLanguageTag());

	}

	public ProductManager(String languageTag) {
		changeLocale(languageTag);
		loadAllData();
	}

	public void changeLocale(String languageTag) {
		formatter = formatters.getOrDefault(languageTag, formatters.get("pt-BR"));
	}

	public static Set<String> getSupportedLocales() {
		return formatters.keySet();
	}

	public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		Product product = new Food(id, name, price, rating, bestBefore);
		products.putIfAbsent(product, new ArrayList<>());
		return product;
	}

	public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
		Product product = new Drink(id, name, price, rating);
		products.putIfAbsent(product, new ArrayList<>());
		return product;
	}

	public Product reviewProduct(int id, Rating rating, String comments) {
		try {
			return reviewProduct(findProduct(id), rating, comments);
		} catch (ProductManagerException e) {
			logger.log(Level.INFO, e.getMessage());
		}
		return null;
	}

	public Product reviewProduct(Product product, Rating rating, String comments) {

		List<Review> reviews = products.get(product);
		products.remove(product, reviews);
		reviews.add(new Review(rating, comments));

		product = product.applyRating(Rateable.convert((int) Math.round(
				reviews.stream().mapToInt(r -> r.getRating().ordinal()).average().orElse(0))));
//		int sum = 0;
//		for (Review review : reviews) {
//			sum += review.getRating().ordinal();
//		}
//		product = product.applyRating(Rateable.convert(Math.round((float) sum / reviews.size())));
		products.put(product, reviews);
		return product;
	}

	public Product findProduct(int id) throws ProductManagerException {

		return products.keySet().stream().filter(p -> p.getId() == id).findFirst().orElseThrow(
				() -> new ProductManagerException("Product with id " + id + " not found.")); // get();
												// .orElseGet(()
												// ->
												// null);

//		Product result = null;
//		for (Product product : products.keySet()) {
//			if (product.getId() == id) {
//				result = product;
//				break;
//			}
//		}
//		return result;
	}

	public void printProductReport(int id) {
		try {
			printProductReport(findProduct(id));
		} catch (ProductManagerException e) {
			logger.log(Level.INFO, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error printing product report " + e.getMessage(), e);
		}
	}

	public void printProductReport(Product product) throws IOException {
		List<Review> reviews = products.get(product);
		Collections.sort(reviews);
		Path productFile = reportsFolder.resolve(
				MessageFormat.format(config.getString("report.file"), product.getId()));
		try (PrintWriter out = new PrintWriter(new OutputStreamWriter(
				Files.newOutputStream(productFile, StandardOpenOption.CREATE), "UTF-8"))) {
			out.append(formatter.formatProduct(product) + System.lineSeparator());

			if (reviews.isEmpty()) {
				out.append(formatter.getText("no.reviews") + System.lineSeparator());
			} else {
				out.append(reviews.stream().map(
						r -> formatter.formatReview(r) + System.lineSeparator())
						.collect(Collectors.joining()));
			}
		}
	}

	public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
		StringBuilder txt = new StringBuilder();
		products.keySet().stream().sorted(sorter).filter(filter)
				.forEach(p -> txt.append(formatter.formatProduct(p) + '\n'));
	}
	
	private void loadAllData () {
		try {
			products = Files.list(dataFolder)
					.filter(file -> file.getFileName().toString().startsWith("product"))
					.map(file -> loadProduct(file))
					.filter(product -> product != null)
					.collect(Collectors.toMap(product -> product, product -> loadReviews(product)));
		} catch (IOException e) {
			Logger.getLogger(ProductManager.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private Product loadProduct(Path file) {
		Product product = null;
		try {
			product = parseProduct(Files.lines(dataFolder.resolve(file), Charset.forName("UTF-8"))
					.findFirst().orElseThrow());
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error loading product " + e.getMessage());
		}
		return product;
	}

	private List<Review> loadReviews(Product product) {
		List<Review> reviews = null;
		Path file = dataFolder.resolve(
				MessageFormat.format(config.getString("reviews.data.file"), product.getId()));
		if (Files.notExists(file)) {
			reviews = new ArrayList<>();
		} else {
			try {
				reviews = Files.lines(file, Charset.forName("UTF-8"))
						.map(text -> parseReview(text))
						.filter(review -> review != null)
						.collect(Collectors.toList());
			} catch (IOException e) {
				logger.log(Level.WARNING, "Error loading reviews" + e.getMessage());
			}
		}
		return reviews;
	}

	private Review parseReview(String text) {
		Review review = null;
		try {
			Object[] values = reviewFormat.parse(text);
			review = new Review(Rateable.convert(Integer.parseInt((String) values[0])),
					(String) values[1]);
		} catch (ParseException | NumberFormatException e) {
			logger.log(Level.WARNING, "Error parsing review " + text);
		}
		return review;

	}

	private Product parseProduct(String text) {
		Product product = null;
		try {
			Object[] values = productFormat.parse(text);
			int id = Integer.parseInt((String) values[1]);
			String name = (String) values[2];
			BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
			Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));
			switch ((String) values[0]) {
			case "D":
				product = new Drink(id, name, price, rating);
				break;
			case "F":
				LocalDate bestBefore = LocalDate.parse((String) values[5]);
				product = new Food(id, name, price, rating, bestBefore);
			}
		} catch (ParseException | NumberFormatException | DateTimeParseException e) {
			logger.log(Level.WARNING, "Error parsing product " + text + " " + e.getMessage());

		}
		return product;
	}

	public Map<String, String> getDiscounts() {
		return products.keySet().stream().collect(Collectors.groupingBy(
				product -> product.getRating().getStars(),
				Collectors.collectingAndThen(Collectors.summingDouble(
						product -> product.getDiscount().doubleValue()),
						discount -> formatter.moneyFormat.format(discount))));
	}

	private static class ResourceFormatter {
		private Locale locale;
		private ResourceBundle resources;
		private DateTimeFormatter dateFormat;
		private NumberFormat moneyFormat;

		private ResourceFormatter(Locale locale) {
			super();
			this.locale = locale;
			resources = ResourceBundle.getBundle("br.com.ric.data.resources", locale);
			dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
			moneyFormat = NumberFormat.getCurrencyInstance(locale);
		}

		private String formatProduct(Product product) {
			return MessageFormat.format(resources.getString("product"), product.getName(),
					moneyFormat.format(product.getPrice()),
					product.getRating().getStars(),
					dateFormat.format(product.getBestBefore()));
		}

		private String formatReview(Review review) {
			return MessageFormat.format(resources.getString("review"), review.getRating().getStars(),
					review.getComments());
		}

		private String getText(String key) {
			return resources.getString(key);
		}
	}

}
