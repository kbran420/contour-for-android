package com.j0h1games.contour;

import java.util.ArrayList;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class StartScreen extends Activity {
	
	private Paint paint;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(new DrawingView(this));
        
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        
        // TODO: variable stroke width with different resolution of devices?
        paint.setStrokeWidth(12);
    }

	// Async Initialization for OpenCV Library Tools
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                	// initialization succeeded
                }
                
                break;
                
                default: {
                    super.onManagerConnected(status);
                }
                
                break;
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback);
    }
    

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_start_screen, container, false);
            return rootView;
        }
    }

    public class DrawingView extends View {
    	
        public int width;
        public  int height;
        private Bitmap  mBitmap;
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;
        
        //TODO: ArrayList<Path> paths / undoPaths

        public DrawingView(Context c) {
	        super(c);
	        context = c;
	        mPath = new Path();
	        mBitmapPaint = new Paint(Paint.DITHER_FLAG);  
	        circlePaint = new Paint();
	        circlePath = new Path();
	        circlePaint.setAntiAlias(true);
	        circlePaint.setColor(Color.BLUE);
	        circlePaint.setStyle(Paint.Style.STROKE);
	        circlePaint.setStrokeJoin(Paint.Join.MITER);
	        circlePaint.setStrokeWidth(4f);
	        
	        this.setBackgroundResource(R.drawable.frogxml);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	        super.onSizeChanged(w, h, oldw, oldh);
	        
	        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
	        
	        mCanvas = new Canvas(mBitmap);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	
	        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
	
	        canvas.drawPath( mPath,  paint);
	
	        canvas.drawPath( circlePath,  circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
	        mPath.reset();
	        mPath.moveTo(x, y);
	        mX = x;
	        mY = y;
        }
        
        private void touch_move(float x, float y) {
	        float dx = Math.abs(x - mX);
	        float dy = Math.abs(y - mY);
	        
	        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
	             mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
	            mX = x;
	            mY = y;
	
	            circlePath.reset();
	            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
	        }
        }
        
        private void touch_up() {
	        mPath.lineTo(mX, mY);
	        
	        circlePath.reset();
	        // commit the path to our offscreen
	        mCanvas.drawPath(mPath,  paint);
	        // kill this so we don't double draw
	        mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
	        float x = event.getX();
	        float y = event.getY();
	
	        switch (event.getAction()) {
	            case MotionEvent.ACTION_DOWN:
	                touch_start(x, y);
	                invalidate();
	                break;
	            case MotionEvent.ACTION_MOVE:
	                touch_move(x, y);
	                invalidate();
	                break;
	            case MotionEvent.ACTION_UP:
	                touch_up();
	                invalidate();
	                break;
        }
	        return true;
        }
    }
}
