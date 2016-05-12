package com.cordova.plugin.baidulocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/** 
 * 使用百度Android定位 SDK5 
 * @version Create：2015.2.2  
 */
public class BaiduLocation4Cordova extends CordovaPlugin implements BDLocationListener {

	private static final int LOCATION_REQUEST_CODE = 677;
	private LocationClient mLocationClient = null;
	private CallbackContext callbackContext;
	private JSONArray args;

		@Override
		public boolean execute(String action, JSONArray args,
				CallbackContext callbackContext) throws JSONException {
			this.callbackContext=callbackContext;
			this.args=args;
			try {
				if(action.equals("startLocation")){	
					checkPermission();
					return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				callbackContext.error(buildErrorRuselut(-1,"发起定位失败"));
			}
			return false;
		}
		private void startLocation(){
			try {
					mLocationClient = new LocationClient(cordova.getActivity().getApplicationContext());     //声明LocationClient类
					mLocationClient.registerLocationListener( this );    //注册监听函数

					LocationClientOption option = new LocationClientOption();
					option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
					option.setCoorType(args.getString(0));//返回的定位结果是百度经纬度,默认值gcj02
					//option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
					option.setIsNeedAddress(args.getBoolean(1));//返回的定位结果包含地址信息
					option.setNeedDeviceDirect(false);//返回的定位结果包含手机机头的方向
					mLocationClient.setLocOption(option);
					mLocationClient.start();
					mLocationClient.requestLocation();
					PluginResult result=new PluginResult(PluginResult.Status.NO_RESULT);
					result.setKeepCallback(true);
					callbackContext.sendPluginResult(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				callbackContext.error(buildErrorRuselut(-1,"发起定位失败"));
			}
		}
		private void checkPermission(){
			if (!cordova.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
					||!cordova.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
				cordova.requestPermissions(this, LOCATION_REQUEST_CODE,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}
				);
			}else {
				startLocation();
			}
		}
		private void goToSettings() {
			AlertDialog.Builder builder=new AlertDialog.Builder(cordova.getActivity());
			builder.setMessage("没有定位权限是否去开启?");
			builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + cordova.getActivity().getPackageName()));
					myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
					myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					cordova.getActivity().startActivity(myAppSettings);
				}
			});
			builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					callbackContext.error("user refuse");
				}
			});
			builder.create().show();
		}
		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			if (arg0!=null) {
				int type=arg0.getLocType();
				if(type==61||type==161||type==65){
					double la=arg0.getLatitude();
					double lo=arg0.getLongitude();
					try {
						JSONObject object=new JSONObject("{latitude:"+la+",longitude:"+lo+",address:"+arg0.getAddrStr()+"}");
						callbackContext.success(object);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						callbackContext.error(buildErrorRuselut(-1,"定位结果异常"));
					}
				}else { 
					callbackContext.error(buildErrorRuselut(type,"百度定位发送错误"));
				} 
			}else {
				callbackContext.error(buildErrorRuselut(-1,"定位结果为空"));
			} 
			mLocationClient.stop();
			mLocationClient.unRegisterLocationListener(this);
		}

	@Override
	public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
		for(int r:grantResults){
			if(r == PackageManager.PERMISSION_DENIED)
			{
				cordova.getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						goToSettings();
					}
				});
				callbackContext.error("user refuse");
				return;
			}
		}
		switch(requestCode){
			case LOCATION_REQUEST_CODE:
				startLocation();
				break;
		}
	}

	private JSONObject buildErrorRuselut(int code , String msg){
		JSONObject object=new JSONObject();
		try {
			object.put("code",code);
			object.put("msg",msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}

