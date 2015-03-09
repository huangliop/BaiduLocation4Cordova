   var exec = require('cordova/exec');
   module.exports = {
       startLocation: function (success, error, options) {
           options = options || {};
           var coorType = options.CoorType ? options.CoorType : 'bd09ll';
           var isNeedAddress = options.IsNeedAddress ? options.IsNeedAddress : false;
           var args = [coorType, isNeedAddress];
           exec(success, error, "BaiduLocation4Cordova", "startLocation", args);
       }
   };
