package com.dave.csdn;

import java.util.List;

import com.dave.entity.NewsItem;
import com.dave.util.Constants;
import com.dave.util.DataUtil;
import com.dave.util.Http;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class LoginActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		
		//new MainTask().execute("http://passport.csdn.net/ajax/accounthandler.ashx?t=log&u=a15345189498&p=abc123&remember=0&f=http%3A%2F%2Fmy.csdn.net&rand=0.9216923983119159");
		new MainTask().execute("https://passport.csdn.net/account/login");
	}
	
	private class MainTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
		//	List<NewsItem> list = db.query(newsType);
			
			Http.getCookie(params[0]);
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			
			
			super.onPostExecute(result);
		}
		
		
	}
	
}
