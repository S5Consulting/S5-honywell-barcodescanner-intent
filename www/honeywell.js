var execute = require("cordova/exec");

var honeywell = {

    listenForScans:function(success,failure){
      return execute(success,failure,'HoneywellScannerPlugin', 'listenForScans', []);
    },
    nativeReleaseScanner:function(success,failure){
        return execute(success,failure,'HoneywellScannerPlugin', 'nativeReleaseScanner', []);
    }
};
module.exports = honeywell;
