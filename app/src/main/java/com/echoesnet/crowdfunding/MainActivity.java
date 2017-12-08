package com.echoesnet.crowdfunding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.echoesnet.crowdfunding.net.HttpMethods;
import com.echoesnet.crowdfunding.net.subscribers.SilenceSubscriber;
import com.echoesnet.crowdfunding.net.subscribers.SubscriberOnCompletedListener;
import com.echoesnet.crowdfunding.net.subscribers.SubscriberOnErrorListener;
import com.echoesnet.crowdfunding.net.subscribers.SubscriberOnNextListener;
import com.echoesnet.multiprocesselixir.Elixir;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import okhttp3.ResponseBody;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button btnDownloadUrl;
    private EditText edtUrl;
    private TextView tvHttpsContent;

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
        tvHttpsContent = findViewById(R.id.tvHttpsContent);

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
//                strBigGiftUrl = edtUrl.getText().toString();
//                try {
//                    mIElixir.obeyCommend(hashCode(), Elixir.GLOBAL, Elixir.DOWNLOAD+Elixir.Args+strBigGiftUrl);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//
//                startActivity(new Intent(this,MainActivity.class));


                HttpMethods.getInstance().runServerUrlKey(new SilenceSubscriber<ResponseBody>(new SubscriberOnNextListener<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody body) {
                        String strjson = charStream2String(body.charStream());
                        Log.i(this.toString(),strjson);
                    }
                }, new SubscriberOnErrorListener<Throwable>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.i(this.toString(),"fdasfasdfasdfasfas");

                        Log.i(this.toString(),e.getMessage());
                    }
                }, new SubscriberOnCompletedListener() {
                    @Override
                    public void onCompleted() {
                        Log.i(this.toString(),"ccc");
                    }
                }, this),"TestC/abc","test","123");

                break;
            default:
        }
    }


    public static String charStream2String(Reader reader) {
        BufferedReader r = new BufferedReader(reader);
        StringBuilder b = new StringBuilder();
        String line;
        try {
            while((line=r.readLine())!=null) {
                b.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b.toString();
    }
}
