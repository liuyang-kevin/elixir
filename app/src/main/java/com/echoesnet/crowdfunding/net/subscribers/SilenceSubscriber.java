package com.echoesnet.crowdfunding.net.subscribers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 这个可以作为基类
 * Created by kevin on 16/3/10.
 */
public class SilenceSubscriber<T> implements Observer<T> {
    public final static String TAG = SilenceSubscriber.class.getSimpleName();

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private SubscriberOnErrorListener mSubscriberOnErrorListener;
    private SubscriberOnCompletedListener mSubscriberOnCompletedListener;
    private InterceptorOnErrorListener mInterceptorOnErrorListener;

    private Context context;

    public SilenceSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
    }

    public SilenceSubscriber(SubscriberOnNextListener mSubscriberOnNextListener,SubscriberOnErrorListener mSubscriberOnErrorListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.mSubscriberOnErrorListener = mSubscriberOnErrorListener;
        this.context = context;
    }


    public SilenceSubscriber(SubscriberOnNextListener mSubscriberOnNextListener,SubscriberOnErrorListener mSubscriberOnErrorListener,SubscriberOnCompletedListener mSubscriberOnCompletedListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.mSubscriberOnErrorListener = mSubscriberOnErrorListener;
        this.mSubscriberOnCompletedListener = mSubscriberOnCompletedListener;
        this.context = context;
    }


    public SilenceSubscriber(SubscriberOnNextListener mSubscriberOnNextListener,SubscriberOnCompletedListener mSubscriberOnCompletedListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.mSubscriberOnCompletedListener = mSubscriberOnCompletedListener;
        this.context = context;
    }


    public SilenceSubscriber(SubscriberOnCompletedListener mSubscriberOnCompletedListener) {
        this.mSubscriberOnCompletedListener = mSubscriberOnCompletedListener;
    }

    /**
     * 谨慎使用，打断onError时候的错误输出。某些特殊位置可以使用。<b color="#ff0000">T 完全打断，F 继续执行</b><br>
     *     <b color="#ff00ff">返回了this，为了可以使用链式语法</b>
     *
     */
    public SilenceSubscriber<T> setInterceptorOnErrorListener(InterceptorOnErrorListener mInterceptorOnErrorListener) {
        this.mInterceptorOnErrorListener = mInterceptorOnErrorListener;
        return this;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onComplete() {
        if (mSubscriberOnCompletedListener != null) {
            mSubscriberOnCompletedListener.onCompleted();
        }
    }


    /**
     * 对错误进行统一处理<br>
     *     *如果需要添加一个时间戳间隔判断，可以解决toast大量显示问题。
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (mInterceptorOnErrorListener != null) {
            if(mInterceptorOnErrorListener.onError(e)){return;}
        }
        if (e instanceof SocketTimeoutException) {
//            L.eSysInfo("TTTTS","网络中断，请检查您的网络状态");

            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
//            L.e(TAG,"网络中断，请检查您的网络状态");
//            L.eSysInfo("TTTTE","网络中断，请检查您的网络状态1");

        } else if (e instanceof ConnectException) {
//            L.eSysInfo("TTTTS","网络中断，请检查您的网络状态");

            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
//            L.e(TAG,"网络中断，请检查您的网络状态");

//            L.eSysInfo("TTTTE","网络中断，请检查您的网络状态1");

        }
//        else if(e instanceof ApiException) {
//
//            //登录失效 踢
//            // USR_NOT_EXITS =》 USR_NOT_REGISTER ，not exits 改为 查询用户错误码
//            if("USR_NOT_REGISTER".equals(e.getMessage()) ||
//                    "USR_LOGIN_TIMEOUT".equals(e.getMessage()) ||
//                    "USR_NO_LOGIN".equals(e.getMessage()))
//                EamApplication.getInstance().logoutEam();
//
//
//
//            if(!"IMUINFO_NULL".equals(e.getMessage()))  //屏蔽了缺少环信账号的toast
//
//            {
//                L.eSysInfo("TTTTS","dfsdfsdf");
//
//                Toast.makeText(context, TransErrorCode.parseErrorCode(e.getMessage()), Toast.LENGTH_SHORT).show();
//                L.eSysInfo("TTTTE","dfsdfsdf");
//
//            }
//
//
//            L.e(TAG, e.getMessage());
//        }
        else {
//            Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.getMessage());
        }







        if (mSubscriberOnErrorListener != null) {
            mSubscriberOnErrorListener.onError(e);
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }


}