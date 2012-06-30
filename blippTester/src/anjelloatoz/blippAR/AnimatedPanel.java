package anjelloatoz.blippAR;

/*
 * Anjello S. Wimalachandra
 * Anjelloatoz@gmail.com
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

public class AnimatedPanel extends LinearLayout {
	private Paint innerPaint, borderPaint;
	
	public AnimatedPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public AnimatedPanel(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		innerPaint = new Paint();
		innerPaint.setARGB(0, 255, 255, 255); //gray
		innerPaint.setAntiAlias(true);

		borderPaint = new Paint();
		borderPaint.setARGB(0, 255, 255, 255);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(2);
	}
	
	public void setInnerPaint(Paint innerPaint) {
		this.innerPaint = innerPaint;
	}

	public void setBorderPaint(Paint borderPaint) {
		this.borderPaint = borderPaint;
	}

    @Override
    protected void dispatchDraw(Canvas canvas) {
    	
    	RectF drawRect = new RectF();
    	drawRect.set(0,0, getMeasuredWidth(), getMeasuredHeight());
    	
    	canvas.drawRoundRect(drawRect, 5, 5, innerPaint);
		canvas.drawRoundRect(drawRect, 5, 5, borderPaint);
		
		super.dispatchDraw(canvas);
    }
    
	public void setLayoutAnimEntry(final ViewGroup panel, Context ctx){
		 
		  AnimationSet set = new AnimationSet(true);
		  Animation animation = new AlphaAnimation(0.0f, 1.0f);
		  animation.setDuration(100);
		  set.addAnimation(animation);
		 
		  animation = new TranslateAnimation(
		      Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		      Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
		  );
		  animation.setDuration(500);
		  set.addAnimation(animation);
		  
		  set.setAnimationListener(new AnimationListener(){
			  @Override
			    public void onAnimationEnd(Animation arg0) {
			        panel.clearAnimation();
			    }

			    @Override public void onAnimationRepeat(Animation arg0) {}
			    @Override public void onAnimationStart(Animation arg0) {}
		  });
		  LayoutAnimationController controller =
		      new LayoutAnimationController(set, 0.25f);
		  panel.setLayoutAnimation(controller);
		}
	
	public void setLayoutAnimExit(final ViewGroup panel, Context ctx, final LinearLayout rl){
		 System.out.println("Point 1");
		  AnimationSet set = new AnimationSet(true);
		 
		  Animation animation = new TranslateAnimation(
		      Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		      Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f
		  );
		  animation.setDuration(500);
		  set.addAnimation(animation);
		  
		  animation.setAnimationListener(new AnimationListener(){
			  @Override
			    public void onAnimationEnd(Animation arg0) {
			        panel.clearAnimation();
			        panel.removeView(rl);
			    }

			    @Override public void onAnimationRepeat(Animation arg0) {}
			    @Override public void onAnimationStart(Animation arg0) {}
		  });

		  panel.startAnimation(animation);
		}
}
