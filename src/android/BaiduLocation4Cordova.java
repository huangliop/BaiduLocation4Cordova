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
 * ʹ�ðٶ�Android��λ SDK5 
 * @version Create��2015.2.2  
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
					mLocationClient = new LocationClient(cordova.getActivity().getApplicationContext());     //����LocationClient��
				    mLocationClient.registerLocationListener( this );    //ע���������
				    
				    LocationClientOption option = new LocationClientOption();
				    option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ
				    option.setCoorType(args.getString(0));//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
				    option.setScanSpan(5000);//���÷���λ����ļ��ʱ��Ϊ5000ms
				    option.setIsNeedAddress(args.getBoolean(1));//���صĶ�λ���������ַ��Ϣ
				    option.setNeedDeviceDirect(false);//���صĶ�λ��������ֻ���ͷ�ķ���
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
				callbackContext.error("��λʧ��");
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
						callbackContext.error("��λʧ��");
					}
				}else { 
					callbackContext.error("��λʧ��");
				} 
			}else {
				callbackContext.error("��λʧ��");
			} 
			mLocationClient.stop();
			mLocationClient.unRegisterLocationListener(this);
		} 
}

