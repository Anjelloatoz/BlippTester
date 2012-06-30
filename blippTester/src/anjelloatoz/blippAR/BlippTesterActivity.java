package anjelloatoz.blippAR;

/*
 * Anjello S. Wimalachandra
 * Anjelloatoz@gmail.com
 */

import android.app.Activity;
import android.app.ProgressDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.view.animation.AccelerateInterpolator;
import android.widget.ListView;
import android.content.Context;
import android.widget.TextView;
import java.util.ArrayList;
import android.widget.AdapterView.*;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.longevitysoft.android.xml.plist.*;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.Array;

public class BlippTesterActivity extends Activity {
	
    private ViewFlipper mFlipper;
    private int mCurrentLayoutState;
    private ArrayList<CatalogItem> feature_list = new ArrayList<CatalogItem>();
    private ArrayList<CatalogItem> cat_item_list = null;
    private ArrayList<CatalogItem> latest_item_list = null;
    private ArrayList<CatalogItem> popular_item_list = null;
    private ArrayList<CatalogItem> az_item_list = null;
    String catalog_XML = null;
    ArrayList<View> dynamic_view_list = new ArrayList<View>();
    Context context;
    TestList tlist;
    boolean XML_parsing = false;
	private ProgressDialog pd;

    static{
    	System.loadLibrary("blipp_native");
    }

    private native String invokeFirstNativeFunction();
    private native String invokeSecondNativeFunction();
    private native String invokeThirdNativeFunction();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("-----------OnCreate Called-----------");
        setContentView(R.layout.main);
        final LinearLayout rl = (LinearLayout)findViewById(R.id.main_menu_panel);
        final AnimatedPanel tp = (AnimatedPanel)findViewById(R.id.transparent_panel);
        context = getApplicationContext();
        cat_item_list = new ArrayList<CatalogItem>();
        
        Thread thread1 = new Thread(){
        	public void run(){
                PList plist =getPList("http://blippar.com/service/ar/getCatalogV2.php?sort=categories");
                
                try{
                	Array plist_array = (Array)plist.getRootElement();
                    cat_item_list = new ArrayList<CatalogItem>();
                    for(int x = 0; x < plist_array.size(); x++){
                    	Dict dict = (Dict)plist_array.get(x);
                    	if(dict.getConfiguration("type").getValue().equals("feature2")){
                    		featurePopulator(dict);
                    	}
                    	else if(dict.getConfiguration("type").getValue().equals("brand")){
                    		cat_item_list.add(catalogPopulator(dict));
                    	}
                    }
                }
                catch(Exception ex2){
                	System.out.println("Type Exception: "+ex2);
                }
                tlist.setNewItems(cat_item_list);
                tlist.setFeatures(feature_list);
                progressHandler.sendEmptyMessage(0);
        	}
        };
        pd = ProgressDialog.show(this, "Plase Wait", "Loading content..", true);
        thread1.start();

        mFlipper = (ViewFlipper) findViewById(R.id.flipper);
        mCurrentLayoutState = 0;
        ListView list_view = (ListView)findViewById(R.id.catalog_listview);
        tlist = new TestList(list_view, cat_item_list, this, ListCreator.MAIN_CATALOG);
        list_view.setOnItemClickListener(CategoriesListener);
        
        tp.setLayoutAnimEntry(tp, tp.getContext());

        Button left = (Button) findViewById(R.id.left);
        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Toast.makeText(getApplicationContext(), "Catalogue Button pressed "+invokeFirstNativeFunction(), Toast.LENGTH_SHORT).show();
            	switchLayoutStateTo(0);
            }
        });
        
        Button mid = (Button) findViewById(R.id.mid);
        mid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Toast.makeText(getApplicationContext(), "Blipp Button pressed: "+invokeSecondNativeFunction(), Toast.LENGTH_SHORT).show();
            	switchLayoutStateTo(1);
            }
        });
        
        Button right = (Button) findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	Toast.makeText(getApplicationContext(), "My Blipps Button pressed: "+invokeThirdNativeFunction(), Toast.LENGTH_SHORT).show();
            	switchLayoutStateTo(2);
            	tp.setLayoutAnimExit(tp, tp.getContext(), rl);
            }
        });
        
        Button add = (Button) findViewById(R.id.Button03);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	tp.addView(rl);
            	switchLayoutStateTo(0);
            	tp.setLayoutAnimEntry(tp, tp.getContext());
            }
        });
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	System.out.println("-----------OnStart Called-----------");
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	System.out.println("-----------OnResume Called-----------");
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	System.out.println("-----------OnPause Called-----------");
    }
    
    @Override
    public void onStop(){
    	super.onStop();
    	System.out.println("-----------OnStop Called-----------");
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	System.out.println("-----------OnDestroy Called-----------");
    }
    
    private Handler progressHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		pd.dismiss();
    	}
    };

    private void featurePopulator(Dict dict){
    	CatalogItem item1 = new CatalogItem();
    	item1.type = nullCheck(dict, "type1");
    	item1.id = nullCheck(dict, "id1");
    	item1.name = nullCheck(dict, "name1");
    	item1.image = nullCheck(dict, "image1");
    	item1.date = nullCheck(dict, "date1");
    	item1.rating = nullCheck(dict, "rating1");
    	item1.comments = nullCheck(dict, "comments1");
    	Array children = (Array)dict.getConfigurationArray("children1");
    	if(children != null){
    		for(int i = 0; i < children.size(); i++){
    			item1.addChildItem(catalogPopulator((Dict)children.get(i)));
    		}
    	}
    	feature_list.add(item1);
    	
    	CatalogItem item2 = new CatalogItem();
    	item2.type = nullCheck(dict, "type2");
    	item2.id = nullCheck(dict, "id2");
    	item2.name = nullCheck(dict, "name2");
    	item2.image = nullCheck(dict, "image2");
    	item2.date = nullCheck(dict, "date2");
    	item2.rating = nullCheck(dict, "rating2");
    	item2.comments = nullCheck(dict, "comments2");
    	children = (Array)dict.getConfigurationArray("children2");
    	if(children != null){
    		for(int i = 0; i < children.size(); i++){
    			item2.addChildItem(catalogPopulator((Dict)children.get(i)));
    		}
    	}
    	feature_list.add(item2);
    }
    
    private String nullCheck(Dict dict, String key){
    	String value = "";
    	try{
    		value = dict.getConfiguration(key).getValue();
    	}
    	catch(Exception ex){}
    	return value;
    }
    
    private CatalogItem catalogPopulator(Dict dict){
    	CatalogItem item = new CatalogItem();
    	item.type = dict.getConfiguration("type").getValue();
    	item.name = dict.getConfiguration("name").getValue();
    	item.image = dict.getConfiguration("image").getValue();
    	item.comments = dict.getConfiguration("comments").getValue();
    	item.rating = dict.getConfiguration("rating").getValue();
    	item.id = dict.getConfiguration("id").getValue();
    	item.date = dict.getConfiguration("date").getValue();
    	Array children = (Array)dict.getConfigurationArray("children");
    	
    	if(children == null){
    		return item;
    	}
    	else{
    		for(int i = 0; i < children.size(); i++){
    			item.addChildItem(catalogPopulator((Dict)children.get(i)));
    		}
    		return item;
    	}
    }
    
    public void newCatalogScreen(ArrayList<CatalogItem> new_list, String title){
    	View view = View.inflate(this, R.layout.catalogue, null);
    	TextView list_heading = (TextView)view.findViewById(R.id.listview_title);
    	list_heading.setText(title);
    	mFlipper.addView(view, mCurrentLayoutState+1);
    	dynamic_view_list.add(view);
    	switchLayoutStateTo(mCurrentLayoutState+1);
    	ListView lv = (ListView)view.findViewById(R.id.catalog_listview);
    	TestList lc = new TestList(lv, new_list, this.getApplicationContext(), ListCreator.PROD_CATALOG);
    	lv.setOnItemClickListener(ProductsListener);
    }
    
    public void setParentListener(View view, final ArrayList<CatalogItem> new_list, final String title){
    	view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	newCatalogScreen(new_list, title);
            }
        });
    }
    
    public void setChildListener(View view, final String id, final String title, final String imageUrl, final String rating){
    	view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	newCommentsScreen(id, title, imageUrl, rating);
            }
        });
    }
    
    private OnItemClickListener CategoriesListener = new OnItemClickListener(){
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		if(position<1 ||position>cat_item_list.size()){
    			return;
    		}
           	CatalogItem item = cat_item_list.get(position-1);
           	if(item.children != null){
           		newCatalogScreen(item.children, item.name);
           	}
        }
    };
    
    private OnItemClickListener ProductsListener = new OnItemClickListener(){
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    		newCommentsScreen(view.getTag()+"", ((TextView)view.findViewById(R.id.name)).getText().toString(), ((TextView)view.findViewById(R.id.name)).getTag().toString(), ((ImageView)view.findViewById(R.id.stars_image)).getTag().toString());
   		}
    };
    
    public void newCommentsScreen(final String id, String title, String imageUri, final String rating){
    	View view = View.inflate(this, R.layout.catalogue, null);
    	TextView list_heading = (TextView)view.findViewById(R.id.listview_title);
    	list_heading.setText(title);
    	ListView lv = (ListView)view.findViewById(R.id.catalog_listview);
    	final ArrayList<CommentsItem> comments_list = new ArrayList<CommentsItem>();
    	final CommentListCreator lc = new CommentListCreator(lv, comments_list, context, ListCreator.PROD_CATALOG, imageUri, rating);
    	
    	Thread comments_thread = new Thread(){
    		public void run(){
    			getCommentsList(id, comments_list);
    			lc.item_list = comments_list;
    			lc.refresh();
    		}
    	};
    	comments_thread.setPriority(1);
    	comments_thread.start();
    	
    	mFlipper.addView(view, mCurrentLayoutState+1);
    	dynamic_view_list.add(view);
    	switchLayoutStateTo(mCurrentLayoutState+1);
    }
    
    public ArrayList<CommentsItem> getCommentsList(String id, ArrayList<CommentsItem> comments_list){
        PList plist = getPList("http://blippar.com/service/ar/getComments.php?id="+id);
        try{
        	Array plist_array = (Array)plist.getRootElement();
        	for(int i = 0; i < plist_array.size(); i++){
        		Dict dict = (Dict)plist_array.get(i);
        		CommentsItem comment = new CommentsItem(dict.getConfiguration("username").getValue(), dict.getConfiguration("date").getValue(), dict.getConfiguration("comment").getValue(), dict.getConfiguration("rating").getValue());
        		comments_list.add(comment);
        	}
        }
        catch(Exception ex){
        	System.out.println("getCommentsList exception: "+ex);
        }
        return comments_list;
    }
    
    public void setCategoriesListener(View view){
    	view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            	tlist.setAllEnabled();
            	v.setEnabled(false);
            	tlist.setListType(TestList.MAIN_CATALOG);
            	tlist.list_view.setOnItemClickListener(CategoriesListener);
            	tlist.setNewItems(cat_item_list);
            }
        });
    }
    
    public void setLatestListener(View view){
    	view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            	if(XML_parsing){
            		return;
            	}
            	tlist.setAllEnabled();
            	v.setEnabled(false);
            	tlist.setListType(TestList.PROD_CATALOG);
            	if(latest_item_list == null){
            		Thread thread = new Thread(){
                		public void run(){
                			System.out.println("point 4");
                			PList latestlist = getPList("http://blippar.com/service/ar/getCatalogV2.php?sort=latest");
                    		Array plist_array = (Array)latestlist.getRootElement();
                            latest_item_list = new ArrayList<CatalogItem>();
                            for(int x = 0; x < plist_array.size(); x++){
                            	Dict dict = (Dict)plist_array.get(x);
                            	if(dict.getConfiguration("type").getValue().equals("product")){
                            		latest_item_list.add(catalogPopulator(dict));
                            	}
                            }
                            tlist.setNewItems(latest_item_list);
                            tlist.list_view.setOnItemClickListener(ProductsListener);
                		}
                	};
                	thread.start();
            	}
            	else{
            		tlist.setNewItems(latest_item_list);
            		tlist.list_view.setOnItemClickListener(ProductsListener);
            	}
            }
        });
    }
    
    public void setPopularListener(View view){
    	view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            	if(XML_parsing){
            		return;
            	}
            	System.out.println("Popular pressed");
            	tlist.setAllEnabled();
            	v.setEnabled(false);
            	tlist.setListType(TestList.PROD_CATALOG);
            	if(popular_item_list == null){
            		Thread thread = new Thread(){
            			public void run(){
            				PList latestlist = getPList("http://blippar.com/service/ar/getCatalogV2.php?sort=popular");
                    		Array plist_array = (Array)latestlist.getRootElement();
                    		popular_item_list = new ArrayList<CatalogItem>();
                            for(int x = 0; x < plist_array.size(); x++){
                            	Dict dict = (Dict)plist_array.get(x);
                            	if(dict.getConfiguration("type").getValue().equals("product")){
                            		popular_item_list.add(catalogPopulator(dict));
                            	}
                            }
                            tlist.setNewItems(popular_item_list);
                            tlist.list_view.setOnItemClickListener(ProductsListener);
            			}
            		};
            		thread.start();
            	}
            	else{
            		tlist.setNewItems(popular_item_list);
            		tlist.list_view.setOnItemClickListener(ProductsListener);
            	}
            }
        });
    }
    
    public void setAZListener(View view){
    	view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            	System.out.println("A -Z pressed");
            	if(XML_parsing){
            		return;
            	}
            	tlist.setAllEnabled();
            	v.setEnabled(false);
            	tlist.setListType(TestList.PROD_CATALOG);
            	if(az_item_list == null){
            		Thread thread = new Thread(){
            			public void run(){
            				PList latestlist = getPList("http://blippar.com/service/ar/getCatalogV2.php?sort=a-z");
                    		Array plist_array = (Array)latestlist.getRootElement();
                    		az_item_list = new ArrayList<CatalogItem>();
                            for(int x = 0; x < plist_array.size(); x++){
                            	Dict dict = (Dict)plist_array.get(x);
                            	if(dict.getConfiguration("type").getValue().equals("product")){
                            		az_item_list.add(catalogPopulator(dict));
                            	}
                            }
                            tlist.setNewItems(az_item_list);
                        	tlist.list_view.setOnItemClickListener(ProductsListener);
            			}
            		};
            		thread.start();
            	}
            	else{
            		tlist.setNewItems(az_item_list);
                	tlist.list_view.setOnItemClickListener(ProductsListener);
            	}
            }
        });
    }
    
    public PList getPList(String url){
    	try{
    		URL sourceUrl = new URL(url);

        	try{
        		XML_parsing = true;
        		InputStream input = sourceUrl.openStream();
        		InputStreamReader in = new InputStreamReader(input, "utf-8");
        		StringBuffer xml = new StringBuffer();
        		int c =0;
        		System.out.println("Before while");
                while( (c = in.read()) != -1){
                   xml.append((char)c);
                }
                System.out.println("After while");
                in.close();
                catalog_XML = xml.toString();
        	}
        	catch(Exception ex){
        		System.out.println("Exception reading sourceUrl: "+ex);
        		}
        	}
        catch (Exception e){
        	System.out.println("XML Pasing Excpetion = " + e);
       	}
        System.out.println("Parsing point 1");
        PListXMLParser parser = new PListXMLParser();
        System.out.println("Parsing point 2");
        PListXMLHandler handler = new PListXMLHandler();
        System.out.println("Parsing point 3");
        parser.setHandler(handler);
        System.out.println("Parsing point 4");
        parser.parse(catalog_XML);
        
        System.out.println("Parsing point 5");
        catalog_XML = null;
        System.out.println("Parsing point 6");
        PList plist =handler.getPlist();
        System.out.println("Parsing point 7");
        XML_parsing = false;
        return plist;
    }
    
    @Override
    public void onBackPressed() {
		if(mFlipper.indexOfChild(mFlipper.getCurrentView())>0){
			int view_index = mFlipper.indexOfChild(mFlipper.getCurrentView());
			System.out.println("Before removal the view list had: "+dynamic_view_list.size());
			dynamic_view_list.remove(mFlipper.getCurrentView());
			switchLayoutStateTo(view_index-1);
			
			mFlipper.removeViewAt(view_index);
			System.out.println("After removal the view list had: "+dynamic_view_list.size());
		}
		else{
			System.exit(0);
		}
    }
    
    public void switchLayoutStateTo(int switchTo){
    	while(mCurrentLayoutState != switchTo){
    		if(mCurrentLayoutState > switchTo){
    			mCurrentLayoutState--;
    			mFlipper.setInAnimation(inFromLeftAnimation());
    			mFlipper.setOutAnimation(outToRightAnimation());
    			mFlipper.showPrevious();
    			}
    		else{
    			mCurrentLayoutState++;
                mFlipper.setInAnimation(inFromRightAnimation());
                mFlipper.setOutAnimation(outToLeftAnimation());
                mFlipper.showNext();
                }
    		};
    	}
    
    protected Animation inFromRightAnimation(){
    	Animation inFromRight = new TranslateAnimation(
    			Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);

    	inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
        }
    
    protected Animation outToLeftAnimation(){
    	Animation outtoLeft = new TranslateAnimation(
    			Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
    	
    	outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
        }
    
    protected Animation inFromLeftAnimation(){
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);

        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
        }
    
    protected Animation outToRightAnimation(){
    	Animation outtoRight = new TranslateAnimation(
    			Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);

    	outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }
    
    private void p(String string){
    	System.out.println("Line number: "+string);
    }
}