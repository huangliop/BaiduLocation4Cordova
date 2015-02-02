   var exec = require('cordova/exec');
   module.exports = {
       startLocation: function (success, error) {
           exec(success, error, "BaiduLocation4Cordova", "startLocation", []);
       }
   };