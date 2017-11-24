package com.echoesnet.crowdfunding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.echoesnet.multiprocesselixir.Elixir;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button btnDownloadUrl;
    private EditText edtUrl;

    private String cppString = stringFromJNI();
    private String strBigGiftUrl = "";
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUrl = findViewById(R.id.edtUrl);
        btnDownloadUrl = findViewById(R.id.btnDownloadUrl);

        btnDownloadUrl.setOnClickListener(this);

        Log.i(this.getClass().getName(),"fadsfasdfasdfasdf"+hashCode());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnDownloadUrl:
                strBigGiftUrl = edtUrl.getText().toString();
                try {
                    mIElixir.obeyCommend(hashCode(), Elixir.GLOBAL, Elixir.DOWNLOAD+Elixir.Args+strBigGiftUrl);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(this,MainActivity.class));
                break;
            default:
        }
    }
}
