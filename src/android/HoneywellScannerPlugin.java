package com.icsfl.rfsmart.honeywell;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;


public class HoneywellScannerPlugin extends CordovaPlugin  {
    private static final String TAG = "HoneywellScanner";
    private static final String ACTION_BARCODE_DATA="com.honeywell.action.BARCODE_DATA";
    private static final String ACTION_CLAIM_SCANNER="com.honeywell.aidc.action.ACTION_CLAIM_SCANNER";
    private static final String ACTION_RELEASE_SCANNER="com.honeywell.aidc.action.ACTION_RELEASE_SCANNER";
    private static final String EXTRA_PROFILE="com.honeywell.aidc.extra.EXTRA_PROFILE";
    private static final String EXTRA_PROPERTIES="com.honeywell.aidc.extra.EXTRA_PROPERTIES";
    private static BarcodeReader barcodeReader;
    private AidcManager manager;
    private CallbackContext callbackContext;
    private BroadcastReceiver barcodeDataReceiver;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {

        super.initialize(cordova, webView);

        barcodeDataReceiver = new BroadcastReceiver(){
          @Override
          public void onReceive(Context context,Intent intent){
            if(ACTION_BARCODE_DATA.equals(intent.getAction())){
              int version = intent.getIntExtra("version",0);
              if(version>= 1){
                setScannedData(intent.getStringExtra("data"));
              }
            }
          }
        };


    }

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext)
    throws JSONException {
        if (action.equals("listenForScans")) {
            this.callbackContext=callbackContext;
            PluginResult result =new PluginResult(PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true);
            this.callbackContext.sendPluginResult(result);
            if(barcodeDataReceiver!=null){
              getActivity().getApplicationContext().registerReceiver(barcodeDataReceiver,new IntentFilter(ACTION_BARCODE_DATA));
              claimScanner();
              Log.d(TAG,"Registered uccessfully")
            }
        }
         if (action.equals("nativeReleaseScanner")) {
           this.callbackContext=callbackContext;
           PluginResult result =new PluginResult(PluginResult.Status.NO_RESULT);
           result.setKeepCallback(true);
           this.callbackContext.sendPluginResult(result);
           nativeReleaseScanner();
        }
        return true;
    }



    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        getActivity().getApplicationContext().registerReceiver(barcodeDataReceiver,new IntentFilter(ACTION_BARCODE_DATA));
        claimScanner();
    }

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        try{
          if(barcodeDataReceiver!=null){
            getActivity().getApplicationContext().unregisterReceiver(barcodeDataReceiver);
            releaseScanner();
          }
        }catch(IllegalArgumentException e){
          e.printStackTrace();

        }
    }
    private Activity getActivity(){
      return this.cordova.getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void NotifyError(String error) {
        if (this.callbackContext != null) {
            PluginResult result = new PluginResult(PluginResult.Status.ERROR, error);
            result.setKeepCallback(true);
            this.callbackContext.sendPluginResult(result);
        }
    }
    private void setScannedData(String data){
      if(this.callbackContext!=null){
        PluginResult result = new PluginResult(PluginResult.Status.OK, data);
        result.setKeepCallback(true);
        this.callbackContext.sendPluginResult(result);
      }
    }
    private void claimScanner(){
      Bundle properties = new Bundle();
      properties.putBoolean("DPR_DATA_INTENT",true);
      properties.putString("DPR_DATA_INTENT_ACTION",ACTION_BARCODE_DATA);
      getActivity().getApplicationContext().sendBroadcast(new Intent(ACTION_CLAIM_SCANNER).putExtra(EXTRA_PROFILE,"DEFAULT").putExtra(EXTRA_PROPERTIES,properties));
    }
    private void releaseScanner(){
      getActivity().getApplicationContext().sendBroadcast(new Intent(ACTION_RELEASE_SCANNER));
    }
    private void nativeReleaseScanner(){
      try{
        if(barcodeDataReceiver!=null){
          getActivity().getApplicationContext().unregisterReceiver(barcodeDataReceiver);
          releaseScanner();
        }
      }catch(IllegalArgumentException e){
        e.printStackTrace();
      }
    }
}
