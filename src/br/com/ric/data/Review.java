package br.com.ric.data;

public class Review {
	private Rating rating;
	private String comments;
	
	
	public Review(Rating rating, String comments) {
		super();
		this.rating = rating;
		this.comments = comments;
	}


	public Rating getRating() {
		return rating;
	}


	public String getComments() {
		return comments;
	}


	@Override
	public String toString() {
		return "Review { " + "rating" + rating + ", comments= " +comments + "}"; 
	}
	
	
	
	
	
	
}