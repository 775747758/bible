package com.orange.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.orange.utils.MyProgressPopUpWindow;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.acts.MainActivity;
import com.example.acts.Main_Activity;
import com.example.acts.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.orange.biblefood.Activity_BibleFoodSelect;
import com.orange.church.Activity_MyChurch;
import com.orange.map.Activity_Map.MyOverlay;
import com.orange.test.Activity_BibleTestMain;
import com.orange.user.Activity_DetailUserInfo;
import com.orange.user.Activity_UserInfo;
import com.orange.view.MyViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_MapMain extends Activity {
	
	private SharedPreferences sp;
	private ImageView map_iv;
	private ImageView list_iv;
	private Editor editor;
	private PullToRefreshListView map_list;
	private FrontiaStorage mCloudStorage;
	private int myLongtitude;
	private int myLatitude;
	private GeoPoint myGeoPoint;
	private SharedPreferences spUser;
	private String name;
	private List<Map<String, String>> userList=new ArrayList<Map<String,String>>();
	private List<GeoPoint> geoList = new ArrayList<GeoPoint>();
	private AlertDialog dialog;
	private BMapManager mBMapMan;
	private MapView mMapView;
	private MapController mMapController;
	private LocationData locData;
	private MyOverlay mOverlay;
	private ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();
	private TabHost tabHost;
	private MyViewPager viewpager;
	private List<View> vlist=new ArrayList<View>();
	private MyPagerAdapter myPagerAdapter;
	private View mapView;
	private View listView;
	private String userName;
	private TextView personNum_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init(null);
		setContentView(R.layout.activity_mapmain);
		
		initBaiDuApi();
		
		map_iv = (ImageView) findViewById(R.id.map_iv);
		list_iv = (ImageView) findViewById(R.id.list_iv);
		
		viewpager = (MyViewPager) findViewById(R.id.viewpager);
		viewpager.setOffscreenPageLimit(2);
		sp = getSharedPreferences("map_config", Context.MODE_PRIVATE);
		spUser = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		userName=spUser.getString("userName", "");
		editor=sp.edit();
		if (sp.getBoolean("isList", false)) {
			map_iv.setImageResource(R.drawable.mode_map_default);
			list_iv.setImageResource(R.drawable.list_pressed);
		} else {
			map_iv.setImageResource(R.drawable.mode_map_pressed);
			list_iv.setImageResource(R.drawable.list_default);
		}
		
		
		map_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sp.getBoolean("isList", true)) {
					map_iv.setImageResource(R.drawable.mode_map_pressed);
					list_iv.setImageResource(R.drawable.list_default);
					editor.putBoolean("isList", false);
					editor.commit();
					viewpager.setCurrentItem(0, true);
				}
			}
		});

		list_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) { 
				if (!sp.getBoolean("isList", true)) {
					map_iv.setImageResource(R.drawable.mode_map_default);
					list_iv.setImageResource(R.drawable.list_pressed);
					editor.putBoolean("isList", true);
					editor.commit();
					viewpager.setCurrentItem(1, true);
				}
			}
		});
		
		mapView = LayoutInflater.from(this).inflate(
				R.layout.cell_mapvp, null);
		map_list = (PullToRefreshListView)LayoutInflater.from(this).inflate(
				R.layout.cell_maplist, null);
		
		map_list.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(Activity_MapMain.this, "已经是最后一个了！", Toast.LENGTH_SHORT).show();
			}
		});
		map_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*userName=intent.getStringExtra("userName");
				remoteName=intent.getStringExtra("name");
				//Log.i("测验Usename", userName);
				from=intent.getStringExtra("from");*/
				Intent intent = new Intent(Activity_MapMain.this, Activity_UserInfo.class);
				intent.putExtra("userName", userList.get(position).get("userName"));
				intent.putExtra("name", userList.get(position).get("name"));
				intent.putExtra("from", "Activity_MapMain");
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				
			}
		});
		initMap();
		
		//map_list=(ListView)listView.findViewById(R.id.map_list);
		map_list.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				findData();
			}
		});
		initBaiDuApi();
		
		vlist.add(mapView);
		vlist.add(map_list);
		myPagerAdapter=new MyPagerAdapter();
		viewpager.setAdapter(myPagerAdapter);
		if(sp.getBoolean("isList", false))
		{
			viewpager.setCurrentItem(1);
		}
		else
		{
			viewpager.setCurrentItem(0);
		}
		
		personNum_tv=(TextView)mapView.findViewById(R.id.personNum_tv);
		//viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
		findData();
		
		
	}
	
	public void initBaiDuApi()
	{
		
		
		boolean isInit = Frontia.init(this.getApplicationContext(), "GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(getApplicationContext(), "您的app key 错误", 3).show();
			return;
		}
		
		mCloudStorage = Frontia.getStorage();

		
	}
	
	public void findData() {
		mOverlay.removeAll();
		geoList.clear();
		userList.clear();
		mMapView.getOverlays().clear();
		dialog = new MyProgressPopUpWindow(
				Activity_MapMain.this, "正在加载中...").createADialog();
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "User");
		
		mItems = new ArrayList<OverlayItem>();
		mItems.addAll(mOverlay.getAllItem());
		mMapView.getOverlays().add(mOverlay);
		mMapView.refresh();

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				StringBuilder sb = new StringBuilder();
				int i=0;
				for (FrontiaData d : dataList) {
					if (d.get("userLatitude") != null && d.get("userLongtitude") != null) {
						if(userName.equals(d.get("userName").toString()))
						{
							myLongtitude=Integer.parseInt(d.get("userLongtitude").toString());
							myLatitude=Integer.parseInt(d.get("userLatitude").toString());
							myGeoPoint=new GeoPoint(myLongtitude, myLatitude);
							
						}
						else
						{
							
							if(d.get("tableType").equals("User"))
							{
								Map<String, String> map=new HashMap<String, String>();
								int longt = Integer.parseInt(d.get("userLongtitude").toString());
								int lat = Integer.parseInt(d.get("userLatitude").toString());
								GeoPoint p = new GeoPoint(longt, lat);
								geoList.add(p);
								map.put("name", d.get("name").toString());
								if(d.get("portraitUri")==null)
								{
									map.put("portraitUri", "");
								}
								else
								{
									map.put("portraitUri", d.get("portraitUri").toString());
								}
								map.put("userName", d.get("userName").toString());
								map.put("userLocation", d.get("userLocation").toString());
								map.put("userLongtitude", d.get("userLongtitude").toString());
								map.put("userLatitude", d.get("userLatitude").toString());
								userList.add(map);
								OverlayItem item = new OverlayItem(p, "覆盖物" + i, "");
								item.setMarker(getResources().getDrawable(R.drawable.map));
								mOverlay.addItem(item);
								mMapView.refresh();
							}
							
						}
						
					}
					i++;
				}
				personNum_tv.setText("共有"+userList.size()+"个用户");
				setMyLocation();
				getDistanceData();
				map_list.setAdapter(new MapListAdapter(userList, getApplicationContext()));
				dialog.dismiss();
				map_list.onRefreshComplete();
				
				/*myPagerAdapter=new MyPagerAdapter();
				viewpager.setAdapter(myPagerAdapter);*/
				
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				//map_list.onRefreshComplete();
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "查询失败！请检查网络！", Toast.LENGTH_SHORT).show();
			}
		});

	}
	
	public void getDistanceData()
	{
		for(int i=0;i<geoList.size();i++)
		{
			userList.get(i).put("distance", DistanceUtil.getDistance(myGeoPoint, geoList.get(i))+"");
		}
		sortData(userList);
	}
	
	private static List<Map<String, String>> sortData(List<Map<String, String>> list) {
		System.out.println("对WCM数据进行排序");
		if (list.size()>1) {
			Comparator<Map<String, String>> mapComprator = new Comparator<Map<String, String>>() {
				@Override
				public int compare(Map<String, String> o1,
						Map<String, String> o2) {
					// do compare.
					if (Double.valueOf(o1.get("distance").toString()) < Double
							.valueOf(o2.get("distance").toString())) {
						return 1;
					} else {
						return -1;
					}
				}
			};
			Collections.sort(list, mapComprator);
		} else {
			new Exception("排序没有取到数据");
		}
		return list;
	}
	
	public class MyOverlay extends ItemizedOverlay {

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index) {
			if(index==userList.size())
			{
				Toast.makeText(getApplicationContext(),"我的位置", 4).show();
			}
			else
			{
				if(userList.get(index).get("userLocation").contains("null")||userList.get(index).get("userLocation").equals(""))
				{
					Toast.makeText(getApplicationContext(), "昵称：" + userList.get(index).get("name") , 4).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "昵称：" + userList.get(index).get("name") + "\n地址：" +  userList.get(index).get("userLocation"), 4).show();
				}
				
			}
			
			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			return false;
		}

	}
	
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(View container, int position) {
		
			((ViewPager) container).addView(vlist.get(position));
			return vlist.get(position);
			
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
			// super.destroyItem(container, position, object);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return vlist.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}

	public void initMap()
	{
		mMapView = (MapView) mapView.findViewById(R.id.bmapView);
		mMapView.setBuiltInZoomControls(true);
		mMapController = mMapView.getController();
		 /**
	     * 是否启用俯视手势
	     * @param v
	     */
	     mMapController.setOverlookingGesturesEnabled(true);    	
		/**
	     * 是否启用旋转手势
	     * @param v
	     */
	     mMapController.setRotationGesturesEnabled(true);    	
		/**
	     * 是否启用双击放大
	     * @param v
	     */
	    mMapView.setDoubleClickZooming(true);
		 /**
         *  设置地图是否响应点击事件  .
         */
        mMapController.enableClick(true);
		mMapController.setZoom(5);// 设置地图zoom级别
		/**
         * 设置地图俯角
         */
		mMapController.setOverlooking(-30);
		locData = new LocationData();
		mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.icon_marka), mMapView);
	}
	
	public void setMyLocation()
	{
		OverlayItem item = new OverlayItem(myGeoPoint, "覆盖物", "");
		item.setMarker(getResources().getDrawable(R.drawable.map_mylocation));
		mOverlay.addItem(item);
		mMapView.refresh();
		mMapController.setCenter(myGeoPoint);
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_MapMain.this, MainActivity.class);
		intent.putExtra("from", 2);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
