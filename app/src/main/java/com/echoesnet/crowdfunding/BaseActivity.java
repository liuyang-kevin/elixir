package com.echoesnet.crowdfunding;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.echoesnet.elixir.aidl.IElixir;
import com.echoesnet.elixir.aidl.IElixirListener;
import com.echoesnet.multiprocesselixir.ElixirService;

public class BaseActivity extends Activity implements ServiceConnection {
    protected IElixir mIElixir;
    protected IElixirListener mIElixirListener = new IElixirListener.Stub() {
        @Override
        public void onElixirChange(String cmd, int id, String result) throws RemoteException {
            Log.i(BaseActivity.this.getClass().getName(),"msg: "+cmd);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindService(new Intent(BaseActivity.this, ElixirService.class), this, BIND_AUTO_CREATE);

    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        mIElixir = IElixir.Stub.asInterface(service);

        try {
            mIElixir.registerListener(mIElixirListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }


//    protected int getHashToInt(){
//        return Integer.parseInt(toString().replace(this.getClass().getName()+"@",""), 16);
//    }
}
