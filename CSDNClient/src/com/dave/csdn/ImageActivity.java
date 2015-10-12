package com.dave.csdn;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dave.util.FileUtil;
import com.dave.util.Http;
import com.polites.android.GestureImageView;

public class ImageActivity  extends Activity {

	private String URL;
	
	private GestureImageView imageView;
	private ProgressBar progressBar;
	
	private ImageView backBtn;
	private ImageView downLoadBtn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_page);

		Bundle bundle = getIntent().getExtras();
		URL = bundle.getString("url", "");
		System.out.println("URL" + URL);
		
		imageView = (GestureImageView) findViewById(R.id.image);
		progressBar = (ProgressBar) findViewById(R.id.loading);
		
		backBtn = (ImageView) findViewById(R.id.back);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		downLoadBtn = (ImageView) findViewById(R.id.download);
		downLoadBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imageView.setDrawingCacheEnabled(true);	//注意需先true，厚false
				if(FileUtil.writeSDcard(URL, imageView.getDrawingCache())){
					Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
				}
				imageView.setDrawingCacheEnabled(false);
			}
		});
		
		new MainTask().execute(URL);
	}
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_no, R.anim.push_right_out);
	}
	private class MainTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap temp = Http.HttpGetBmp(params[0]);
			return temp;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if(result == null){
				Toast.makeText(ImageActivity.this, "网络信号不佳", Toast.LENGTH_LONG).show();
			}else{
				imageView.setImageBitmap(result);
				progressBar.setVisibility(View.GONE);
			}
			super.onPostExecute(result);
		}
		
		
	}

	
}
