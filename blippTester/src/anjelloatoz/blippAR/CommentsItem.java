package anjelloatoz.blippAR;

public class CommentsItem {
	String user_name = "";
	String date = "";
	String comment = "";
	String rating = "";
	
	CommentsItem(String user_name, String date, String comment, String rating){
		this.user_name = user_name;
		this.date = date;
		this.comment = comment;
		this.rating = rating;
	}
	
	public String getUserName(){
		return this.user_name;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public String getComment(){
		return this.comment;
	}
	
	public String getRating(){
		return this.rating;
	}
}
