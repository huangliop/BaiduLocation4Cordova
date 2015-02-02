package com.cordova.plugin.baidulocation;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin; 

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
 

/** 
 * 使用百度Android定位 SDK5 
 * @version Create：2015.2.2  
 */
public class BaiduLocation4Cordova extends CordovaPlugin {
		@Override
		public boolean execute(String action, JSONArray args,
				CallbackContext callbackContext) throws JSONException {
			
			if(action.equals("startLocation")){	
				
				return true;
			}
			 
			return false;
		} 
}
