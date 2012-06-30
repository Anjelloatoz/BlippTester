package anjelloatoz.blippAR;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;

public class CatalogItem {
	String type;
	String name;
	String image;
	String comments;
	String rating;
	String id;
	String date;
	ArrayList<CatalogItem> children = new ArrayList<CatalogItem>();
	
	CatalogItem(){
		
	}
	
	public void addChildItem(CatalogItem newItem){
		this.children.add(newItem);
	}
	CatalogItem(String type, String name, String image, String comments, String rating, String id, String date, ArrayList <CatalogItem> children){
		this.type = type;
		this.name = name;
		this.image = image;
		this.comments = comments;
		this.id = id;
		this.date = date;
		this.children = children;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public String getBlipps(){
		return ""+this.children.size();
	}
	
	public String getComments(){
		return this.comments;
	}
	
	public String getRating(){
		return this.rating;
	}
}
