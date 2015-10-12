package com.dave.csdn;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.dave.entity.NewsItem;
import com.dave.entity.Page;
import com.dave.util.Constants;
import com.dave.util.DB;
import com.dave.util.DataUtil;
import com.dave.util.Http;
import com.dave.util.URLUtil;
import com.dave.view.NewsListYiDongAdapter;

@SuppressLint("ValidFragment")
public class NewsFrag extends Fragment implements IXListViewRefreshListener, IXListViewLoadMore{
	private XListView newsListView;
	private NewsListYiDongAdapter adapter;
	
	private boolean isLoad = false;
	private int newsType = 1;
	private Page page;
	
	private DB db;
	private String refreshDate = "";
	
	
	public NewsFrag(int newsTyle){
		this.newsType = newsTyle;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		init();
		
	//	newsListView.startRefresh();
		//newsListView.NotRefreshAtBegin();
		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initComponent();
		if(isLoad == false){
			isLoad = true;
			refreshDate = getDate();
			newsListView.setRefreshTime(refreshDate);
			//加载数据库中的数据
			List<NewsItem> list = db.query(newsType);
			adapter.setList(list);
			adapter.notifyDataSetChanged();
			
			newsListView.startRefresh();
			//newsListView.NotRefreshAtBegin();
		}else{
			newsListView.NotRefreshAtBegin();
		}
		
		System.out.println("onActivityCreate");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("onCreateView");
		return inflater.inflate(R.layout.activity_main, null);
	}

	private void init(){
		db = new DB(getActivity());
		adapter = new NewsListYiDongAdapter(getActivity()); 
//		PageUtil.setPageStart();
		page = new Page();
		page.setPageStart();
	}
	private void initComponent(){
		newsListView = (XListView) getView().findViewById(R.id.newsListView);
		newsListView.setAdapter(adapter);
		newsListView.setPullRefreshEnable(this);
		newsListView.setPullLoadEnable(this);
		newsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				NewsItem item = (NewsItem)adapter.getItem(arg2-1);
				Intent i = new Intent();
				i.setClass(getActivity(), NewsContentActivity.class);
				i.putExtra("newsLink", item.getLink());
				startActivity(i);
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_no);
				System.out.println(arg2);
			}
		});
	}
	private class MainTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
		//	List<NewsItem> list = db.query(newsType);
			
			String temp = Http.httpGet(params[0]);
			if(temp == null){
				return Constants.DEF_RESULT_CODE.ERROR;
			}
			
			List<NewsItem> list = DataUtil.getNewsItemListYiDong(newsType, temp);
			if(list.size() == 0){
				return Constants.DEF_RESULT_CODE.NO_DATA;
			}
			if(params[1].equals("refresh")){
				adapter.setList(list);
				return Constants.DEF_RESULT_CODE.REFRESH;
			} else {
				adapter.addList(list);
				return Constants.DEF_RESULT_CODE.LOAD;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			adapter.notifyDataSetChanged();
			switch(result){
			case Constants.DEF_RESULT_CODE.ERROR:
				Toast.makeText(getActivity(), "网络信号不佳", Toast.LENGTH_LONG).show();
				newsListView.stopRefresh(getDate());
				newsListView.stopLoadMore();
				break;
			case Constants.DEF_RESULT_CODE.NO_DATA:
				Toast.makeText(getActivity(), "最后一页" + newsType, Toast.LENGTH_LONG).show();
				newsListView.stopLoadMore();
				break;
			case Constants.DEF_RESULT_CODE.REFRESH:
				newsListView.stopRefresh(getDate());
				//保存到数据库
				db.delete(newsType);
				db.insert(adapter.getList());
				System.out.println("insert");
				break;
			case Constants.DEF_RESULT_CODE.LOAD:
				newsListView.stopLoadMore();
				page.addPage();
				break;
			}
			
			super.onPostExecute(result);
		}
		
		
	}
	@Override
	public void onLoadMore() {
		System.out.println("loadmore");
		new MainTask().execute(URLUtil.getNewsListURL(newsType, page.getCurrentPage()), "load");
	}
	@Override
	public void onRefresh() {
		System.out.println("refresh");
		page.setPageStart();
		new MainTask().execute(URLUtil.getRefreshNewsListURL(newsType), "refresh");
	}
	public String getDate(){
		SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);  
	    return sdf.format(new java.util.Date()); 
	}

}
