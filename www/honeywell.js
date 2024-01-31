var execute = require("cordova/exec");

var honeywell = {
  /**
   * Listens for scan events from the Honeywell scanner
   * @param {*} success
   * @param {*} failure
   * @returns
   */
  listenForScans: function (success, failure) {
    return execute(
      success,
      failure,
      "HoneywellScannerPlugin",
      "listenForScans"
    );
  },
  nativeReleaseScanner: function (success, failure) {
    return execute(
      success,
      failure,
      "HoneywellScannerPlugin",
      "nativeReleaseScanner",
      []
    );
  },
};
module.exports = honeywell;
