package com.orange.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.Ground;
import com.baidu.mapapi.map.GroundOverlay;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.acts.R;
import com.orange.login.Activity_Login;
import com.orange.read.Activity_SelectVolume;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Map extends Activity {
	private boolean isRequest = false;// �Ƿ��ֶ���������λ
	private boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	private MyLocationOverlay myLocationOverlay = null;
	MapController mMapController = null;
	LocationClient mLocClient;
	LocationData locData = null;
	double longtitude;
	double latitude;
	BMapManager mBMapMan = null;
	MapView mMapView = null;

	private ArrayList<OverlayItem> mItems = null;
	private MyOverlay mOverlay = null;
	private OverlayItem mCurItem = null;

	private OverlayItem Myitem = null;

	private FrontiaStorage mCloudStorage;

	private View viewCache = null;
	private PopupOverlay pop = null;
	private TextView popupText = null;// ����view
	private List<GeoPoint> geoList = new ArrayList<GeoPoint>();
	private List<String> usersName = new ArrayList<String>();
	private List<String> myLocation = new ArrayList<String>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init(null);
		setContentView(R.layout.activity_map);

		mCloudStorage = Frontia.getStorage();

		boolean isInit = Frontia.init(this.getApplicationContext(), "GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(getApplicationContext(), "����app key ����", 3).show();
			return;
		}

		SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		int Mylongtitude = sp.getInt("userLongtitude", 0);
		int Mylatitude = sp.getInt("userLatitude", 0);
		Log.i("Mylongtitude", Mylongtitude + "");
		Log.i("Mylatitude", Mylatitude + "");

		// GeoPoint myPoint = new GeoPoint ((int)(mLat5*1E6),(int)(mLon5*1E6));
		GeoPoint myPoint = new GeoPoint(Mylongtitude, Mylatitude);
		Myitem = new OverlayItem(myPoint, "�ҵ�λ��", "");
		Myitem.setMarker(getResources().getDrawable(R.drawable.icon_geo));

		mMapView = (MapView) findViewById(R.id.bmapView);

		mMapView.setBuiltInZoomControls(true);
		mMapController = mMapView.getController();
		mMapController.setZoom(5);// ���õ�ͼzoom����
		// mMapController.setCenter(myPoint);
		
		locData = new LocationData();

		// ��λ��ʼ��
		// mLocClient = new LocationClient(getApplicationContext());
		// locData = new LocationData();
		// mLocClient.registerLocationListener(new MyLocationListenner());
		// LocationClientOption option = new LocationClientOption();
		// option.setLocationMode(LocationMode.Hight_Accuracy);
		// option.setOpenGps(true);// ��gps
		// option.setCoorType("bd09ll"); // ������������
		// option.setScanSpan(500);
		// mLocClient.setLocOption(option);
		// mLocClient.start();

		// myLocationOverlay = new MyLocationOverlay(mMapView);

		// myLocationOverlay.setData(locData);
		// myLocationOverlay.setMarker(getResources().getDrawable(R.drawable.map));

		/*
		 * mMapView.getOverlays().add(myLocationOverlay);
		 * //mMapView.setSatellite(true);// ��ͨ��ͼ mMapView.refresh();
		 */
		/**
		 * �����Զ���overlay
		 */
		mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.icon_marka), mMapView);
		findData();
		// createPaopao();
		// initOverlay();
	}

	/*
	 * public class MyLocationListenner implements BDLocationListener {
	 * 
	 * @Override public void onReceiveLocation(BDLocation arg0) {
	 * System.out.print("����������");
	 * 
	 * if (arg0 != null) { // ����ǰλ��ת���ɵ�������� GeoPoint pt = new GeoPoint( (int)
	 * (arg0.getLatitude() * 1000000), (int) (arg0.getLongitude() * 1000000));
	 * // ����ǰλ������Ϊ��ͼ������ mMapController.setCenter(pt); }
	 * 
	 * }
	 * 
	 * @Override public void onReceivePoi(BDLocation arg0) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * }
	 */
	public void initOverlay() {

		/*
	         *//**
		 * ׼��overlay ����
		 */
		/*
		 * 
		 * GeoPoint p1 = new GeoPoint ((int)(mLat1*1E6),(int)(mLon1*1E6));
		 * OverlayItem item1 = new OverlayItem(p1,"������1","");
		 *//**
		 * ����overlayͼ�꣬�粻���ã���ʹ�ô���ItemizedOverlayʱ��Ĭ��ͼ��.
		 */
		/*
		 * item1.setMarker(getResources().getDrawable(R.drawable.icon_marka));
		 * 
		 * GeoPoint p2 = new GeoPoint ((int)(mLat2*1E6),(int)(mLon2*1E6));
		 * OverlayItem item2 = new OverlayItem(p2,"������2","");
		 * item2.setMarker(getResources().getDrawable(R.drawable.icon_markb));
		 * 
		 * GeoPoint p3 = new GeoPoint ((int)(mLat3*1E6),(int)(mLon3*1E6));
		 * OverlayItem item3 = new OverlayItem(p3,"������3","");
		 * item3.setMarker(getResources().getDrawable(R.drawable.icon_markc));
		 * 
		 * GeoPoint p4 = new GeoPoint ((int)(mLat4*1E6),(int)(mLon4*1E6));
		 * OverlayItem item4 = new OverlayItem(p4,"������4","");
		 * item4.setMarker(getResources().getDrawable(R.drawable.icon_gcoding));
		 *//**
		 * ��item ��ӵ�overlay�� ע�⣺ ͬһ��itmeֻ��addһ��
		 */
		/*
		 * mOverlay.addItem(Myitem); mOverlay.addItem(item1);
		 * mOverlay.addItem(item2); mOverlay.addItem(item3);
		 * mOverlay.addItem(item4);
		 */
		/**
		 * ��������item���Ա�overlay��reset���������
		 */
		mItems = new ArrayList<OverlayItem>();
		mItems.addAll(mOverlay.getAllItem());

		// ��ʼ�� ground ͼ��
		// mGroundOverlay = new GroundOverlay(mMapView);
		/*
		 * GeoPoint leftBottom = new GeoPoint((int) (mLat5 * 1E6), (int) (mLon5
		 * * 1E6)); GeoPoint rightTop = new GeoPoint((int) (mLat6 * 1E6), (int)
		 * (mLon6 * 1E6));
		 */
		// Drawable d = getResources().getDrawable(R.drawable.ground_overlay);
		// Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
		// mGround = new Ground(bitmap, leftBottom, rightTop);

		/**
		 * ��overlay �����MapView��
		 */
		mMapView.getOverlays().add(mOverlay);
		// mMapView.getOverlays().add(mGroundOverlay);
		// mGroundOverlay.addGround(mGround);
		/**
		 * ˢ�µ�ͼ
		 */
		mMapView.refresh();
		/*
		 * mResetBtn.setEnabled(false); mClearBtn.setEnabled(true);
		 */
		/**
		 * ���ͼ����Զ���View.
		 */
		/*
		 * viewCache = getLayoutInflater().inflate(R.layout.custom_text_view,
		 * null); popupInfo = (View) viewCache.findViewById(R.id.popinfo);
		 * popupLeft = (View) viewCache.findViewById(R.id.popleft); popupRight =
		 * (View) viewCache.findViewById(R.id.popright); popupText =(TextView)
		 * viewCache.findViewById(R.id.textcache);
		 * 
		 * button = new Button(this);
		 * button.setBackgroundResource(R.drawable.popup);
		 */

		/**
		 * ����һ��popupoverlay
		 */
		/*
		 * PopupClickListener popListener = new PopupClickListener(){
		 * 
		 * @Override public void onClickedPopup(int index) { if ( index == 0){
		 * //����itemλ�� pop.hidePop(); GeoPoint p = new
		 * GeoPoint(mCurItem.getPoint().getLatitudeE6()+5000,
		 * mCurItem.getPoint().getLongitudeE6()+5000); mCurItem.setGeoPoint(p);
		 * mOverlay.updateItem(mCurItem); mMapView.refresh(); } else if(index ==
		 * 2){ //����ͼ��
		 * mCurItem.setMarker(getResources().getDrawable(R.drawable.nav_turn_via_1
		 * )); mOverlay.updateItem(mCurItem); mMapView.refresh(); } } }; pop =
		 * new PopupOverlay(mMapView,popListener);
		 */

	}

	public class MyOverlay extends ItemizedOverlay {

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index) {
			/*
			 * OverlayItem item = getItem(index); mCurItem = item ;
			 * popupText.setText("����һ��ϵͳ�ؼ�"); GeoPoint pt = new
			 * GeoPoint((geoList.get(index).getLatitudeE6() ),
			 * geoList.get(index).getLongitudeE6()); // �����Զ���View
			 * pop.showPopup(popupText, pt, 32);
			 */
			Toast.makeText(getApplicationContext(), "�ǳƣ�" + usersName.get(index) + "\n��ַ��" + myLocation.get(index), 4).show();
			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			/*
			 * popupText.setText("����һ��ϵͳ�ؼ�"); pop.showPopup(popupText, pt, 32);
			 */

			return false;
		}

	}

	public void findData() {
		mItems = new ArrayList<OverlayItem>();
		mItems.addAll(mOverlay.getAllItem());
		mMapView.getOverlays().add(mOverlay);
		mMapView.refresh();
		// mOverlay.addItem(Myitem);
		// FrontiaQuery���кܶ��ѯ����������Գ��Զ��ֲ�ѯ�������൱��sql����е�where
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "User");

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				int i = 0;
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {
					if (d.get("userLatitude") != null && d.get("userLongtitude") != null) {
						int longt = Integer.parseInt(d.get("userLongtitude").toString());
						int lat = Integer.parseInt(d.get("userLatitude").toString());
						GeoPoint p = new GeoPoint(longt, lat);
						OverlayItem item = new OverlayItem(p, "������" + i, "");
						item.setMarker(getResources().getDrawable(R.drawable.map));
						geoList.add(p);
						usersName.add((String) d.get("name"));
						myLocation.add((String) d.get("userLocation"));
						mOverlay.addItem(item);
						mMapView.refresh();
					}
					i++;
				}
			}

			@Override
			public void onFailure(int errCode, String errMsg) {

			}
		});

	}

	/*
	 * public void createPaopao(){ viewCache =
	 * getLayoutInflater().inflate(R.layout.custom_text_view, null); popupText
	 * =(TextView) viewCache.findViewById(R.id.textcache); //���ݵ����Ӧ�ص�
	 * PopupClickListener popListener = new PopupClickListener(){
	 * 
	 * @Override public void onClickedPopup(int index) { Log.v("click",
	 * "clickapoapo"); } }; pop = new PopupOverlay(mMapView,popListener); //
	 * MyLocationMapView.pop = pop; }
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent2 = new Intent(Activity_Map.this, Activity_SelectVolume.class);
			intent2.putExtra("from", "1");
			startActivity(intent2);
			finish();
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
		return isFirstLoc;
	}

}