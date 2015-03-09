使用的百度Android定位SDK 5.01  
在AndroidManifest.xml中的"com.baidu.lbsapi.API_KEY" 修改为自己申请的百度Key  

调用方式  
window.baiduLocation.startLocation(  
    function (success) {  
            alert(success.latitude + "," + success.longitude+","+success.address);  
        }, function (error) {  
            alert(error);  
        },{//这个参数也可以不传  
            CoorType:'bd09ll', //设置坐标系默认'bd09ll'  
            IsNeedAddress:false //是否需要返回坐标的地址信息，默认是false  
        });    
返回的结果格式    
{  
    latitude: 13.4324, //纬度  
    longitude:23.4324, //经度  
    address:"北京北京" /地址信息  
}
