package anjelloatoz.blippAR;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public abstract class ImageFetcher {

	public static Drawable getDrawable(String imageUrl){
		System.out.println("URL: "+imageUrl);
    	InputStream is = fetch(imageUrl);
    	Bitmap bitmap = BitmapFactory.decodeStream(is);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
		System.out.println("Returning from ImageFetcher");
        return drawable;
    }
	
	private static InputStream fetch(String urlString){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlString);
        HttpResponse response = null;
        InputStream ii = null;
        try{
        	 response = httpClient.execute(request);
        	 ii = response.getEntity().getContent();
        }
        catch(Exception e){
        	System.out.println("ImageFetcher Exception: "+e);
        }
        return ii;
    }
}
