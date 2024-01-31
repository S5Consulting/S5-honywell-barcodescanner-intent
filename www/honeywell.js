var execute = require("cordova/exec");

var honeywell = {
  /**
   * Listens for scan events from the Honeywell scanner
   * @param {*} success
   * @param {*} failure
   * @param {Array} args
   * @returns
   */
  listenForScans: function (success, failure, args) {
    return execute(
      success,
      failure,
      "HoneywellScannerPlugin",
      "listenForScans",
      args
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
