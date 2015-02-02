package com.cordova.plugin.baidulocation;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin; 
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient; 
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
 

/** 
 * 使用百度Android定位 SDK5 
 * @version Create：2015.2.2  
 */
public class BaiduLocation4Cordova extends CordovaPlugin implements BDLocationListener {
	
	private LocationClient mLocationClient = null; 
	private CallbackContext callbackContext;

		@Override
		public boolean execute(String action, JSONArray args,
				CallbackContext callbackContext) throws JSONException {
			this.callbackContext=callbackContext;
			try {
				if(action.equals("startLocation")){	
					mLocationClient = new LocationClient(cordova.getActivity().getApplicationContext());     //声明LocationClient类
				    mLocationClient.registerLocationListener( this );    //注册监听函数
				    
				    LocationClientOption option = new LocationClientOption();
				    option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
				    option.setCoorType(args.getString(0));//返回的定位结果是百度经纬度,默认值gcj02
				    option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
				    option.setIsNeedAddress(args.getBoolean(1));//返回的定位结果包含地址信息
				    option.setNeedDeviceDirect(false);//返回的定位结果包含手机机头的方向
				    mLocationClient.setLocOption(option);
				    mLocationClient.start();
				    mLocationClient.requestLocation();
				    PluginResult result=new PluginResult(PluginResult.Status.NO_RESULT);
				    result.setKeepCallback(true);
				    callbackContext.sendPluginResult(result);
					return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				callbackContext.error("定位失败");
			}
			return false;
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
						callbackContext.error("定位失败");
					}
				}else { 
					callbackContext.error("定位失败");
				} 
			}else {
				callbackContext.error("定位失败");
			} 
			mLocationClient.stop();
			mLocationClient.unRegisterLocationListener(this);
		} 
}

