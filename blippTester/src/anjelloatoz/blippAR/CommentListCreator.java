package anjelloatoz.blippAR;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class CommentListCreator {
	CatalogAdapter catalog_adapter;
	ListView list_view;
	ArrayList<CommentsItem> item_list;
	String rating;
	Context context;
	View top_view;
	View other_views;
	static int MAIN_CATALOG = 0;
	static int SUB_CATALOG = 1;
	static int PROD_CATALOG = 2;
	int list_type;
	String imageUrl = "";
	ArrayList<View> feature_views = new ArrayList<View>();
	FrameLayout main_image_frame = null;
	ImageView main_image = null;
	TextView number_of_comments = null;
	TextView empty_list_label;
	
	public CommentListCreator(ListView list_view, ArrayList<CommentsItem> item_list, Context context, int list_type, String imageUri, String rating){
		System.out.println("CommentListCreator called: "+list_type);
		System.out.println("Item list size: "+item_list.size());
		this.list_view = list_view;
		this.item_list = item_list;
		this.rating = rating;
		this.context = context;
		this.list_type = list_type;
		this.imageUrl = imageUri;
		catalog_adapter = new CatalogAdapter();
		catalog_adapter.addFirstItem("top_row");
		empty_list_label = new TextView(context);
		empty_list_label.setText("Please wait, Loading..");
		empty_list_label.setTextSize(20);
		empty_list_label.setTextColor(Color.WHITE);
		empty_list_label.setGravity(Gravity.CENTER_HORIZONTAL);
		this.list_view.addFooterView(getFooter());
		list_view.setAdapter(catalog_adapter);
	}
	
	public View getFooter(){
		LinearLayout filler = new LinearLayout(context);
		filler.setOrientation(LinearLayout.VERTICAL);
		filler.addView(View.inflate(context, android.R.layout.simple_list_item_1, null));
		filler.addView(empty_list_label);
		filler.addView(View.inflate(context, android.R.layout.simple_list_item_1, null));
		return filler;
	}
	
	public void refresh(){
		setTop.sendEmptyMessage(0);
		notifyDatasetChangeCaller.sendEmptyMessage(0);
		Message msg = setCatalogText.obtainMessage();
		if(item_list.size() == 0){
			msg.obj = "No comments to display";
		}
		else{
			msg.obj = " ";
		}
		setCatalogText.sendMessage(msg);
	}
	
	private Handler setCatalogText = new Handler(){
		public void handleMessage(Message msg){
			empty_list_label.setText(msg.obj.toString());
		}
	};
	
	private Handler notifyDatasetChangeCaller = new Handler() {
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			listPopulator();
			catalog_adapter.notifyDataSetChanged();
		}
	};
	
	private Handler setTop = new Handler() {
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			number_of_comments.setText(item_list.size()+" comments");
		}
	};
	
	private void listPopulator(){
		for(int i = 0; i < item_list.size(); i++){
			catalog_adapter.addItem(item_list.get(i));
		}
	}
	
	private View getTopView(){
		View view = View.inflate(context, R.layout.comments_top, null);
		view.setContentDescription("top_view");
		main_image_frame = (FrameLayout)view.findViewById(R.id.cat_top_left_frame);
		number_of_comments = (TextView)view.findViewById(R.id.number_of_comments);
		number_of_comments.setText(item_list.size()+" comments");
		ImageView rating_image = (ImageView)view.findViewById(R.id.stars_image);
		Resources res = context.getResources();
		if(rating.equals("0")){
			rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_0));
		}
		else if(rating.equals("1")){
			rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_1));
		}
		else if(rating.equals("2")){
			rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_2));
		}
		else if(rating.equals("3")){
			rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_3));
		}
		else if(rating.equals("4")){
			rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_4));
		}
		else if(rating.equals("1")){
			rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_5));
		}
		Thread thread = new Thread(){
        	public void run(){
        		Drawable drawable = ImageFetcher.getDrawable(imageUrl);
        		main_image = new ImageView(context);
                main_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                main_image.setPadding(8, 8, 8, 8);
        		main_image.setImageDrawable(drawable);
        		messageHandler.sendEmptyMessage(0);
        	}
        };
        if(main_image == null){
        	thread.setPriority(1);
        	thread.start();
        }
        else{
        	messageHandler.sendEmptyMessage(0);
        }
		
		return view;
	}
	
	private View getRowView(){
		View view = View.inflate(context, R.layout.comments_item, null);
		view.setContentDescription("row_view");
		return view;
	}
	
	private Handler messageHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(main_image.getParent() != null){
				((FrameLayout)main_image.getParent()).removeView(main_image);
			}
			main_image_frame.addView(main_image, 1);
		}
	};
	
	public ArrayList<View> getFeatureViews(){
		return feature_views;
	}
	
	public CatalogAdapter getAdapter(){
		return catalog_adapter;
	}

	private class CatalogAdapter extends BaseAdapter{		
		private ArrayList items = new ArrayList();
		private TreeSet<Integer> first_items = new TreeSet<Integer>();
		LayoutInflater vi;

		public CatalogAdapter(){
            vi  = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
		
		@Override
        public int getCount() {
            return items.size();
        }
		
		@Override
        public long getItemId(int position) {
            return position;
        }
		
		@Override
        public int getItemViewType(int position) {
            return first_items.contains(position) ? 1 : 0;
        }
		
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
			System.out.println("Getview position: "+position);
                if(convertView == null){
                	if(position == 0){
                		System.out.println("Calling topView");
                    	convertView = getTopView();
                    }
                    else{
                    	System.out.println("Calling rowView");
                    	convertView = getRowView();
                    }
                }
                else{
                	if(position == 0){
                		if(!convertView.getContentDescription().equals("top_view")){
                			convertView = getTopView();
                		}                			
                	}
                	else{
                		if(!convertView.getContentDescription().equals("row_view")){
                			convertView = getRowView();
                		}
                	}
                }
                CommentsItem comments_item = null;
                try{
                	comments_item = (CommentsItem)items.get(position);
                }
                catch(Exception ex){
                	System.out.println("The item is not a CommentsItem");
                }

                ImageView rating_image = (ImageView) convertView.findViewById(R.id.stars_image);
                Resources res = context.getResources();
                
                	if (comments_item != null) {
                		TextView number_of_comments = (TextView) convertView.findViewById(R.id.number_of_comments);
                        TextView tt = (TextView) convertView.findViewById(R.id.comment_name);
                        TextView bt = (TextView) convertView.findViewById(R.id.comment_date);
                        TextView ct = (TextView) convertView.findViewById(R.id.comment_text);
                        TextView comments_view = (TextView) convertView.findViewById(R.id.comments_textview);
                        FrameLayout frame = (FrameLayout)convertView.findViewById(R.id.left_cat_frame);
                        
                        if(number_of_comments != null){
                        	number_of_comments.setText(item_list.size()+" comments");
                        }
                        if (tt != null) {
                              tt.setText(comments_item.getUserName());                            }
                        if(bt != null){
                              bt.setText(comments_item.getDate());
                        }
                        if(ct != null){
                       		ct.setText(comments_item.getComment());
                        }
                        if(rating_image != null){
                        	if(comments_item.getRating().equals("0")){
                        		rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_0));
                        	}
                        	else if(comments_item.getRating().equals("1")){
                        		rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_1));
                        	}
                        	else if(comments_item.getRating().equals("2")){
                        		rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_2));
                        	}
                        	else if(comments_item.getRating().equals("3")){
                        		rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_3));
                        	}
                        	else if(comments_item.getRating().equals("4")){
                        		rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_4));
                        	}
                        	else if(comments_item.getRating().equals("5")){
                        		rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_5));
                        	}
                        }
                }
                return convertView;
		}
		
		@Override
        public Object getItem(int position) {
            return items.get(position);
        }
		
		public void addFirstItem(String item){
    		items.add(item);
    		first_items.add(items.size()-1);
    		notifyDataSetChanged();
    	}
    	
    	public void addItem(CommentsItem item){
    		items.add(item);
    		notifyDataSetChanged();
    	}
	}
}
