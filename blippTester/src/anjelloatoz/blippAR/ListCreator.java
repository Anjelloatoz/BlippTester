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
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.FrameLayout;

public class ListCreator {
	CatalogAdapter catalog_adapter;
	ListView list_view;
	ArrayList<CatalogItem> item_list;
	Context context;
	View top_view;
	View other_views;
	static int MAIN_CATALOG = 0;
	static int SUB_CATALOG = 1;
	static int PROD_CATALOG = 2;
	int list_type;
	ArrayList<String> feature_imagelist = null;
	ArrayList<View> feature_views = new ArrayList<View>();
	
	public ListCreator(/*ListView list_view,*/ ArrayList<CatalogItem> item_list, Context context, int list_type, ArrayList<String> feature_imagelist){
		System.out.println("ListView called: "+list_type);
		this.list_view = list_view;
		this.item_list = item_list;
		this.context = context;
		this.list_type = list_type;
		this.feature_imagelist = feature_imagelist;
		
		p("50");
		catalog_adapter = new CatalogAdapter();
		p("52");
		System.out.println("The length of the list is: "+item_list.size());
		for(int i = 0; i < item_list.size(); i++){
			if(i == 0){
        		catalog_adapter.addItem(item_list.get(i));
        	}
        	else{
        		catalog_adapter.addItem(item_list.get(i));
        	}
		}
		list_view.setAdapter(catalog_adapter);
	}
	
	private void viewCreator(){
		if(list_type == this.MAIN_CATALOG){
			top_view = View.inflate(context, R.layout.catalog_top_row, null);
			top_view.setContentDescription("top_view");
			other_views = View.inflate(context, R.layout.catalog_list_item, null);
			other_views.setContentDescription("other_view");
			FrameLayout feature_frame1 = (FrameLayout)top_view.findViewById(R.id.cat_top_left_frame);
			
			Drawable feature1 = getDrawable(this.feature_imagelist.get(0));
            ImageView main_image = new ImageView(context);
            main_image.setImageDrawable(feature1);
            main_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            main_image.setPadding(8, 8, 8, 8);
            
            feature_frame1.addView(main_image, 1);
            feature_views.add(feature_frame1);
            
            FrameLayout feature_frame2 = (FrameLayout)top_view.findViewById(R.id.feature_frame);
			
			Drawable feature2 = getDrawable(this.feature_imagelist.get(1));
            ImageView main_image2 = new ImageView(context);
            main_image2.setImageDrawable(feature2);
            main_image2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            main_image2.setPadding(8, 8, 8, 8);
            
            feature_frame2.addView(main_image2, 1);
            feature_views.add(feature_frame2);
		}
		else if(list_type == this.PROD_CATALOG){
			top_view = View.inflate(context, R.layout.product_list_item, null);
			top_view.setContentDescription("top_view");
			other_views = View.inflate(context, R.layout.product_list_item, null);
			other_views.setContentDescription("other_views");
		}	
	}
	
	public ArrayList<View> getFeatureViews(){
		return feature_views;
	}
	
	public CatalogAdapter getAdapter(){
		return catalog_adapter;
	}
	
	private Drawable getDrawable(String uri){
    	InputStream is = fetch(uri);
    	Bitmap bitmap = BitmapFactory.decodeStream(is);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }
	
	private InputStream fetch(String urlString){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlString);
        HttpResponse response = null;
        InputStream ii = null;
        try{
        	 response = httpClient.execute(request);
        	 ii = response.getEntity().getContent();
        }
        catch(Exception e){
        	System.out.println("EXCEPTION:::::::::: "+e);
        }
        return ii;
    }
	
	private class CatalogAdapter extends BaseAdapter{	
		
		private ArrayList<CatalogItem> items = new ArrayList<CatalogItem>();
		private TreeSet<Integer> first_items = new TreeSet<Integer>();
		LayoutInflater vi;
		private LayoutInflater mInflater;

		public CatalogAdapter(){
//            vi  = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mInflater  = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            p("134");
            }
		
		private void p(String string){
			//System.out.println("CatalogAdapter Line: "+string);
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
			System.out.println("getView: " + position + " " + convertView);
			p("158");
                View v = convertView;
                p("a");
                int type = getItemViewType(position); System.out.println("Item view type is: "+type);
                p("b");
                viewCreator();                
                p("162");
                
                switch (type){
                case 0:
                	v = other_views;
                	break;
                	
                case 1:
                	v = top_view;
                	break;
                }
                CatalogItem catalog_item = items.get(position);

                p("175");
                ImageView rating_image = (ImageView) v.findViewById(R.id.stars_image);
                Resources res = context.getResources();
                Drawable d1 = res.getDrawable(R.drawable.cell_product_left_image);
                p("179");
                ImageView background = new ImageView(context);
                background.setImageDrawable(d1);
                background.setScaleType(ImageView.ScaleType.FIT_CENTER);

                p("180");
                Drawable d2 = getDrawable(catalog_item.image);
                ImageView main_image = new ImageView(context);
                main_image.setImageDrawable(d2);
                main_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                main_image.setPadding(8, 8, 8, 8);
                p("190");
                
                Drawable d3 = res.getDrawable(R.drawable.cell_product_left_image_gloss);
                ImageView gloss = new ImageView(context);
                gloss.setImageDrawable(d3);
                gloss.setScaleType(ImageView.ScaleType.FIT_CENTER);
                p("196");
                
                	if (catalog_item != null) {
                		p("199");
                        TextView tt = (TextView) v.findViewById(R.id.name);
                        TextView bt = (TextView) v.findViewById(R.id.date);
                        TextView ct = (TextView) v.findViewById(R.id.blipps);
                        TextView comments_view = (TextView) v.findViewById(R.id.comments_textview);
                        FrameLayout frame = (FrameLayout)v.findViewById(R.id.left_cat_frame);
                        p("205");
                        
                        if (tt != null) {
                              tt.setText(catalog_item.getName());                            }
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
                        	frame.addView(background);
                        	frame.addView(main_image);
                        	frame.addView(gloss);
                        	frame.setTag(catalog_item.id);
                        }
                        p("224");
                        if(rating_image != null){
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
                        	p("244");
                        }
                }
                return v;
		}
		
		@Override
        public CatalogItem getItem(int position) {
            return items.get(position);
        }
		
		public void addFirstItem(CatalogItem item){
    		items.add(item);
    		first_items.add(items.size()-1);
    		notifyDataSetChanged();
    	}
    	
    	public void addItem(CatalogItem item){
    		items.add(item);
    		notifyDataSetChanged();
    	}
	}
	
	
	
	public static class ViewHolder {
        public TextView textView;
    }
	
	private void p(String string){
		System.out.println("ListCreator Line: "+string);
	}
}
