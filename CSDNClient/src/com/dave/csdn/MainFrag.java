package com.dave.csdn;

import java.util.List;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;
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
import com.dave.util.DataUtil;
import com.dave.util.Http;
import com.dave.util.PageUtil;
import com.dave.util.URLUtil;
import com.dave.view.NewsListAdapter;

public class MainFrag extends Fragment implements IXListViewRefreshListener, IXListViewLoadMore{
	private XListView newsListView;
	private NewsListAdapter adapter;
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
		newsListView.NotRefreshAtBegin();
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
		adapter = new NewsListAdapter(getActivity()); 
		PageUtil.setPageStart();
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
				System.out.println(arg2);
			}
		});
	}
	private class MainTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			String temp = Http.httpGet(params[0]);
			if(temp == null){
				return -1;
			}
			
			List<NewsItem> list = DataUtil.getNewsItemList(temp);
			if(list.size() == 0){
				return 0;
			}
			if(params[1].equals("refresh")){
				adapter.setList(list);
				return 1;
			} else {
				adapter.addList(list);
				return 2;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch(result){
			case -1:
				Toast.makeText(getActivity(), "网络信号不佳", Toast.LENGTH_LONG).show();
				break;
			case 0:
				Toast.makeText(getActivity(), "最后一页", Toast.LENGTH_LONG).show();
			//	newsListView.stopLoadMore();
				break;
			case 1:
			//	newsListView.stopRefresh("111");
				break;
			case 2:
//				newsListView.stopRefresh("111");
//				newsListView.stopLoadMore();
//				newsListView.setRefreshTime("2012");
				//adapter.notifyDataSetChanged();
				PageUtil.addPage();
				break;
			}
			onLoad();
			adapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
		
		
	}
	private void onLoad() {
		newsListView.stopRefresh("111");
		newsListView.stopLoadMore();
		newsListView.setRefreshTime("2012");
	}
	@Override
	public void onLoadMore() {
		System.out.println("loadmore");
		new MainTask().execute(URLUtil.getNewsListURL(PageUtil.getCurrentPage()), "load");
	}
	@Override
	public void onRefresh() {
		System.out.println("refresh");
		new MainTask().execute(URLUtil.getRefreshNewsURl(), "refresh");
	}

}
