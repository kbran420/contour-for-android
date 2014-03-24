package com.j0h1games.contour;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.os.Build;
import android.provider.MediaStore;

public class ImageUpload extends Activity {
	
	private static final int RESULT_LOAD_IMAGE = 1;

	private ImageView ivImageUpload;
	private ScrollView svImageUpload;
	
	// Async Initialization for OpenCV Library Tools
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                	// initialization succeeded
                } break;
                
                default: {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
	
	private Toast toast;
	private GestureDetector gestureDetector;
	private Bitmap currentSelectedImage;
	private Bitmap edgeImage;

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_upload);
		
		svImageUpload = (ScrollView) findViewById(R.id.svUploadImage);
		
		ivImageUpload = (ImageView) findViewById(R.id.ivImageUpload);
		
		//TODO: Lösung für GestureDetection finden
		// z.B. Button und SeekBar über das Bild legen oder so, damit
		// nicht gescrolled werden muss
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			public void onLongPress(MotionEvent e) {
				// new intent to start gallery dialog to pick an image
				Intent intent = new Intent(
						Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, RESULT_LOAD_IMAGE);
		    }
		});
		
		ivImageUpload.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}			
		});
		
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		
		SeekBar sbImageUpload = (SeekBar) findViewById(R.id.sbImageUpload);
		sbImageUpload.setMax(100);
		
		sbImageUpload.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
		    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		    	if (progress == 0) {
		    		progress = 1;
		    	}
//		    	toast.setText("Value: "+ getConvertedValue(progress));
		    	toast.setText("Value: "+ progress);
		    	toast.show();
		    	
		    	Mat mat = new Mat();
		    	Bitmap tempBmp = currentSelectedImage.copy(Bitmap.Config.ARGB_8888, false);
		    	Utils.bitmapToMat(tempBmp, mat);
		    	
		    	Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
		    	
//		    	Imgproc.GaussianBlur(mat, mat, new Size(5, 5), 5);
		    	Imgproc.blur(mat, mat, new Size(3, 3));
		    	
		    	Mat edges = new Mat();
		    	Imgproc.Canny(mat, edges, progress, 2 * progress);
		    	
		    	edgeImage = Bitmap.createBitmap(currentSelectedImage.getWidth(),
		    			currentSelectedImage.getHeight(), currentSelectedImage.getConfig());
		    	
//		    	// storing currently selected image in Mat-object
//		    	Mat mat = new Mat();
//		    	Bitmap tempBmp = currentSelectedImage.copy(Bitmap.Config.ARGB_8888, false);
//		    	Utils.bitmapToMat(tempBmp, mat);
//		    	
//		    	// threshold calculation needs grayscale image
//		    	Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
//		    	
//		    	//TODO: perform gaussian Blur before canny edge detection
//		    	Imgproc.GaussianBlur(mat, mat, new Size(3, 3), getConvertedValue(progress));
//		    	
//		    	// calculate threshold for edge detection with OTSU-method
//		    	double high_thres = Imgproc.threshold(mat, mat, 0, 255, 
//		    			Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
//		    	
//		    	// create edge image using canny edge detection
//		    	Mat edges = new Mat();
//		    	Imgproc.Canny(mat, edges, 0.5 * high_thres, high_thres);
//		    	
//		    	edgeImage = Bitmap.createBitmap(currentSelectedImage.getWidth(),
//		    			currentSelectedImage.getHeight(), currentSelectedImage.getConfig());
		    	
		    	//TODO: dilate edge image
		    	
		    	Utils.matToBitmap(edges, edgeImage);
		    	
		    	ivImageUpload.setImageBitmap(edgeImage);		    	
		    }
		    
		    public void onStartTrackingTouch(SeekBar seekBar) {
		        // TODO Auto-generated method stub
		    }
		    
		    public void onStopTrackingTouch(SeekBar seekBar) {
		        // TODO Auto-generated method stub
		    }
		});
	}
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
	      
	     if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
	         Uri selectedImage = data.getData();
	         
	         // get path of image
	         String[] filePathColumn = { MediaStore.Images.Media.DATA };	 
	         Cursor cursor = getContentResolver().query(selectedImage,
	                 filePathColumn, null, null, null);
	         cursor.moveToFirst();	 
	         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	         String filePath  = cursor.getString(columnIndex);
	         cursor.close();
	         
	         // convert file path to bitmap
	         currentSelectedImage = BitmapFactory.decodeFile(filePath);
	         
	         // show selected image in ImageView
	         ivImageUpload.setImageBitmap(currentSelectedImage);
	     }
     }
	
	public float getConvertedValue(int intVal) {
	    return .25f * intVal;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_upload, menu);
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

}
