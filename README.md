#使用的百度Android定位SDK 6.2.3 
       
##安装方法

    cordova plugin add https://github.com/huangliop/BaiduLocation4Cordova.git --variable API_KEY=your_baidu_key  //your_baidu_key为你的百度申请的key

##调用方式  

    window.baiduLocation.startLocation(  
        function (success) {  
                alert(success.latitude + "," + success.longitude+","+success.address);  
            }, function (error) {  
                /*
                     error={
                            code:   //code=-1,为本地错误,code>0为百度定位的错误码
                            msg:  //错误描述
                     }
                */
            },{//这个参数也可以不传  
                CoorType:'bd09ll', //设置坐标系默认'bd09ll'  
                IsNeedAddress:false //是否需要返回坐标的地址信息，默认是false  
            });    

##返回的结果格式    

    {  
        latitude: 13.4324, //纬度  
        longitude:23.4324, //经度  
        address:"北京北京" /地址信息  
    }
