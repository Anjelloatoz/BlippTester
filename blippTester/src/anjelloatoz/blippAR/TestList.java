package anjelloatoz.blippAR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Button;
import android.app.ProgressDialog;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

public class TestList {
	
	Context context;
	ListView list_view;
	int list_type;
	ArrayList<CatalogItem> item_list;
	ArrayList<CatalogItem> feature_list = new ArrayList<CatalogItem>();
	ArrayList<View> feature_views = new ArrayList<View>();
	private MyCustomAdapter mAdapter;
	static final int MAIN_CATALOG = 0;
	static final int SUB_CATALOG = 1;
	static final int PROD_CATALOG = 2;
	ImageView image_array[];
	int feature_frame_id_tag = 100;
	
	FrameMainImageSetter image_setter;
	TableLayout table = null;
	LayoutParams layout_params = null;
	TextView empty_list_label;
	
	Button categories = null;
	Button popular = null;
	Button latest = null;
	Button az = null;

	public TestList(ListView list_view, ArrayList<CatalogItem> item_list, Context context, int list_type){
		this.list_view = list_view;
		this.item_list = item_list;
		this.context = context;
		
		this.list_type = list_type;
		image_array = new ImageView[item_list.size()];
		image_setter = new FrameMainImageSetter();
		this.empty_list_label = new TextView(context);
		mAdapter = new MyCustomAdapter();

		if(list_type == MAIN_CATALOG){
			empty_list_label.setText("Please wait, Loading..");
			this.list_view.addHeaderView(getHeader());
		}
		else if(list_type == PROD_CATALOG){
			listPopulator();
			empty_list_label.setText(" ");
		}
		empty_list_label.setGravity(Gravity.CENTER_HORIZONTAL);
		empty_list_label.setTextSize(20);
		empty_list_label.setTextColor(Color.WHITE);
		
		this.list_view.addFooterView(getFooter());
		this.list_view.setAdapter(mAdapter);
	}
	
	public void setListType(int new_type){
		this.list_type = new_type;
	}
	
	public void setAllEnabled(){
		categories.setEnabled(true);
		latest.setEnabled(true);
		popular.setEnabled(true);
		az.setEnabled(true);
	}
	
	public View getHeader(){
		View view = View.inflate(context, R.layout.catalog_top_row, null);
		categories = (Button)view.findViewById(R.id.categories_button);
		latest = (Button)view.findViewById(R.id.latest_button);
		popular = (Button)view.findViewById(R.id.popular_button);
		az = (Button)view.findViewById(R.id.az_button);

		((BlippTesterActivity)context).setCategoriesListener(categories);
		((BlippTesterActivity)context).setPopularListener(popular);
		((BlippTesterActivity)context).setLatestListener(latest);
		((BlippTesterActivity)context).setAZListener(az);
		table = (TableLayout)view.findViewById(R.id.feature_table);
		return view;
	}
	
	public View getFooter(){
		LinearLayout filler = new LinearLayout(context);
		filler.setOrientation(LinearLayout.VERTICAL);
		filler.addView(View.inflate(context, android.R.layout.simple_list_item_1, null));
		filler.addView(empty_list_label);
		filler.addView(View.inflate(context, android.R.layout.simple_list_item_1, null));
		return filler;
	}
	
	public void setNewItems(ArrayList<CatalogItem> new_items_list){
		this.item_list = new_items_list;
		image_array = new ImageView[item_list.size()];
		setNewAdapter.sendEmptyMessage(0);
	}

	public void setFeatures(ArrayList<CatalogItem> new_feature_list){
		this.feature_list = new_feature_list;
		setFeature();
	}
	
	private Handler setNewAdapter = new Handler(){
		public void handleMessage(Message msg){
			mAdapter = new MyCustomAdapter();
			list_view.setAdapter(mAdapter);
			populateAdapter.sendEmptyMessage(0);
		}
	};
	
	private Handler setCatalogText = new Handler(){
		public void handleMessage(Message msg){
			empty_list_label.setText(msg.obj.toString());
		}
	};
	
	private void setFeature(){
		int cell_count = (feature_list.size() % 3 == 0 ? 2 : 2);
		tableSetter.sendEmptyMessage(cell_count);
		for(int i = 0; i < feature_list.size(); i++){
			FrameLayout frame = (FrameLayout)View.inflate(context, R.layout.feature_frame, null);
		
			Message msg = tableSetter.obtainMessage();
			msg.obj = frame;
			msg.what = 0;
			msg.arg1 = i;
			tableSetter.sendMessage(msg);
		}
	}

	private Handler tableSetter = new Handler() {
		int cell_count = 0;
		TableRow row = null;
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			if(msg.what!=0){
				cell_count = msg.what;
				row = new TableRow(context);
				row.setGravity(Gravity.CENTER_HORIZONTAL);
				table.setColumnStretchable(0, true);
				table.setColumnStretchable(1, true);
				table.addView(row);
				
				return;
			}
			FrameLayout frame = (FrameLayout)msg.obj;
			if(feature_list.get(msg.arg1).children.isEmpty()){
				((BlippTesterActivity)context).setChildListener(frame, feature_list.get(msg.arg1).id, feature_list.get(msg.arg1).name, feature_list.get(msg.arg1).image, feature_list.get(msg.arg1).rating);
			}
			else{
				((BlippTesterActivity)context).setParentListener(frame, feature_list.get(msg.arg1).children, feature_list.get(msg.arg1).name);
			}
			if(row.getChildCount()< cell_count){
				row.addView(frame);
			}
			else{
				row = new TableRow(context);
				table.addView(row);
				row.addView(frame);
			}
			
			frame.getLayoutParams().height = 150;
			frame.getLayoutParams().width = 200;
			frame.invalidate();
			Message image_msg = featureImageHandler.obtainMessage();
			image_msg.obj = frame;
			image_msg.what = msg.arg1;
			featureImageHandler.sendMessage(image_msg);
		}
	};
	
	private Handler featureImageHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Drawable drawable = ImageFetcher.getDrawable(feature_list.get(msg.what).image);
			ImageView feature_image_view = new ImageView(context);
			feature_image_view.setScaleType(ImageView.ScaleType.FIT_CENTER);
			feature_image_view.setPadding(8, 8, 8, 8);
			feature_image_view.setImageDrawable(drawable);
			FrameLayout frame = (FrameLayout)msg.obj;
			frame.addView(feature_image_view, 1);
		}
	};
	
	private Handler populateAdapter = new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			listPopulator();
			mAdapter.notifyDataSetChanged();
			Message msg2 = setCatalogText.obtainMessage();
			if(item_list.size() == 0){
				msg2.obj = "No comments to display";
			}
			else{
				msg2.obj = " ";
			}
			setCatalogText.sendMessage(msg2);
		}
	};
	
	private void listPopulator(){
		for(int i = 0; i < item_list.size(); i++){
    		mAdapter.addItem(item_list.get(i));
		}
	}
	
	private View getRowView(){
		View view = null;
		if(list_type == TestList.MAIN_CATALOG){
			view = View.inflate(context, R.layout.catalog_list_item, null);
		}
		else if(list_type == TestList.PROD_CATALOG){
			view = View.inflate(context, R.layout.product_list_item, null);
		}
		view.setContentDescription("row_view");
		FrameLayout f = (FrameLayout)view.findViewById(R.id.left_cat_frame);
		f.setTag(feature_frame_id_tag++);		
		return view;
	}
	

	private class MyCustomAdapter extends BaseAdapter {
        private static final int TYPE_SEPARATOR = 1;
        private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
        private ArrayList items = new ArrayList();
        private TreeSet<Integer> first_items = new TreeSet<Integer>();

        public void addItem(final CatalogItem item) {
            items.add(item);
            notifyDataSetChanged();
        }
        
        public void addFirstItem(final String item){
    		items.add(item);
    		first_items.add(items.size()-1);
    		notifyDataSetChanged();
    	}

        @Override
        public int getItemViewType(int position) {
            return first_items.contains(position) ? 1 : 0;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public CatalogItem getItem(int position) {
            return (CatalogItem)items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
            	convertView = getRowView();
            }

            CatalogItem catalog_item = null;
            
            try{
            	catalog_item = (CatalogItem)items.get(position);
            }
            catch(Exception ex){
            	System.out.println("The item is not a CatalogItem");
            }
           	
            ImageView rating_image = (ImageView) convertView.findViewById(R.id.stars_image);
            Resources res = context.getResources();
            if (catalog_item != null) {
                TextView tt = (TextView) convertView.findViewById(R.id.name);
                TextView bt = (TextView) convertView.findViewById(R.id.date);
                TextView ct = (TextView) convertView.findViewById(R.id.blipps);
                TextView comments_view = (TextView) convertView.findViewById(R.id.comments_textview);
                final FrameLayout frame = (FrameLayout)convertView.findViewById(R.id.left_cat_frame);
                
                if (tt != null) {
                      tt.setText(catalog_item.getName());
                      tt.setTag(catalog_item.image);
                }
                if(bt != null){
                      bt.setText(catalog_item.getDate());
                }
                if(ct != null){
               		ct.setText(catalog_item.getBlipps()+" blipps");
                }
                if(comments_view != null){
                	comments_view.setText(catalog_item.getComments()+" comments");
                }
                if(frame != null){
                	if(frame.getChildCount() == 3){
                		frame.removeViewAt(1);
                	}
                	convertView.setTag(catalog_item.id);
                	
                	frame.setContentDescription(""+position);
                	image_setter.ImageSetter(frame, position);
                }
                if(rating_image != null){
                	rating_image.setTag(catalog_item.getRating());
                	if(catalog_item.getRating().equals("0")){
                		rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_0));
                	}
                	else if(catalog_item.getRating().equals("1")){
                		rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_1));
                	}
                	else if(catalog_item.getRating().equals("2")){
                		rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_2));
                	}
                	else if(catalog_item.getRating().equals("3")){
                		rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_3));
                	}
                	else if(catalog_item.getRating().equals("4")){
                		rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_4));
                	}
                	else if(catalog_item.getRating().equals("5")){
                		rating_image.setImageDrawable(res.getDrawable(R.drawable.stars_5));
                	}
                }
        }
            return convertView;
        }
    }

	class FrameMainImageSetter{
		int current_position;
		public void ImageSetter(FrameLayout frame, int position){
			current_position = position;
			System.out.println("ImageSetter called");
			int frame_id = Integer.parseInt(frame.getTag().toString());
			if(image_array[position]==null){
				Message msg = ImageDrawer.obtainMessage();
				msg.arg1 = position;
				msg.obj = frame;
				msg.what = frame_id;
				
				if(ImageDrawer.hasMessages(frame_id)){
					System.out.println("Ta Da!!!!!");
					ImageDrawer.removeMessages(frame_id);
					System.out.println("Old basturds killed!!!!!");
				}
				ImageDrawer.sendMessage(msg);
			}
			else{
				System.out.println("image_array position is not null");
				Message msg = messageHandler.obtainMessage();
				msg.obj = frame;
				msg.arg1 = position;
				msg.what = frame_id;
				if(messageHandler.hasMessages(frame_id)){
					System.out.println("!!!!FOUND SOME OLD Messages!!!!");
					messageHandler.removeMessages(frame_id);
					System.out.println("!!!!Old Messages Removed");
				}
				messageHandler.sendMessage(msg);
			}
		}
		
		private Handler ImageDrawer = new Handler(){
			public void handleMessage(Message msg){
				int position = msg.arg1;
				FrameLayout frame = (FrameLayout)msg.obj;
				
				Drawable drawable_image = ImageFetcher.getDrawable(item_list.get(position).image);
				ImageView image = new ImageView(context);
				image.setImageDrawable(drawable_image);
				image.setScaleType(ImageView.ScaleType.FIT_CENTER);
				image.setPadding(15, 15, 15, 15);
				image_array[position] = image;
				
				Message new_msg = messageHandler.obtainMessage();
				new_msg.obj = frame;
				new_msg.arg1 = position;
				new_msg.what = msg.what;
				if(messageHandler.hasMessages(msg.what)){
					System.out.println("!!!!FOUND SOME OLD Messages!!!!");
					messageHandler.removeMessages(msg.what);
					System.out.println("!!!!Old Messages Removed");
				}
				messageHandler.sendMessage(new_msg);
			}
		};
		
		public void setImage(final int position, final FrameLayout frame){
			Thread thread = new Thread(){
				public void run(){
					Drawable drawable_image = ImageFetcher.getDrawable(item_list.get(position).image);
					ImageView image = new ImageView(context);
					image.setImageDrawable(drawable_image);
					image.setScaleType(ImageView.ScaleType.FIT_CENTER);
					image.setPadding(15, 15, 15, 15);
					image_array[position] = image;
					
					int frame_id = Integer.parseInt(frame.getTag().toString());
					Message msg = messageHandler.obtainMessage();
					msg.obj = frame;
					msg.arg1 = position;
					msg.what = frame_id;
					if(messageHandler.hasMessages(frame_id)){
						System.out.println("!!!!FOUND SOME OLD Messages!!!!");
						messageHandler.removeMessages(frame_id);
						System.out.println("!!!!Old Messages Removed");
					}
					messageHandler.sendMessage(msg);
				}
			};
			thread.setPriority(1);
			thread.start();
		}
		private Handler messageHandler = new Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				System.out.println("**The recieved message is: "+msg.what);

				FrameLayout fram = (FrameLayout)msg.obj;
				int position = msg.arg1;
				ImageView image = image_array[position];
				
				try{
					if(image.getParent()!=null){
						FrameLayout fl = (FrameLayout)image.getParent();
						fl.removeView(image);
					}
					System.out.println("This frame already has: "+fram.getChildCount()+"children.");
					if(fram.getChildCount()==3){
						fram.removeViewAt(1);
					}
					fram.addView(image, 1);
				}
				catch(Exception ex){
					System.out.println("Exception caught in the testList: " + ex);
				}
			}
		};
	}
}
