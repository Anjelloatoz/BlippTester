package anjelloatoz.blippAR;

public class CatalogueItem {
	private String item_name;
	private String item_date;
	private int item_blipps;
	
	public String getItemName(){
		return item_name;
	}
	
	public String getItemDate(){
		return item_date;
	}
	
	public int getItemBlipps(){
		return item_blipps;
	}
	
	public void setItemName(String name){
		item_name = name;
	}
	
	public void setItemDate(String date){
		item_date = date;
	}
	
	public void setItemBlipps(int blipps){
		item_blipps = blipps;
	}
}
