package com.echoesnet.multiprocesselixir;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.echoesnet.elixir.aidl.IElixir;
import com.echoesnet.elixir.aidl.IElixirListener;

public class ElixirService extends Service {
    private IElixirListener mElixirListener;


    IElixir.Stub mStub = new IElixir.Stub() {


        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String getVersionCode(String partOfName) throws RemoteException {
            if(mElixirListener!=null){
                mElixirListener.onElixirChange("IElixir.getVersionCode", 0, "0");
            }
            return "getVersionCode";
        }

        @Override
        public String getCacheData(String cmd, int owner) throws RemoteException {

            return "getCacheData";
        }

        @Override
        public String callback(String cmd, int owner) throws RemoteException {
//            if(null!=cb){
//                cb.callback("callback!!!!!");
//            }
            return "callback";
        }

        @Override
        public void registerListener(IElixirListener listener) throws RemoteException {
            mElixirListener = listener;
            mElixirListener.onElixirChange("IElixir.registerListener", 0, "0");
        }

        @Override
        public String obeyCommend(int aOwnerID, String aCmdType, String aCmd) throws RemoteException {
            Log.i(""+aOwnerID,String.format("type:%s | cmd:%s",aCmdType,aCmd));

            return null;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }
}
