package com.echoesnet.crowdfunding.net;

import android.util.Log;

import com.echoesnet.crowdfunding.BuildConfig;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpMethods {
    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private Gson g = new Gson();

    //构造方法私有
    private HttpMethods() {
        HttpsTrustManager trustManager = new HttpsTrustManager();

        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

//        builder.addInterceptor(chain -> {
//            Request request = chain.request().newBuilder()
//                    .addHeader("X-CRM-Application-Id", RequestConstants.APPLICATION_ID)
//                    .addHeader("X-CRM-Version", RequestConstants.VERSION)
//                    .addHeader("Content-Type", RequestConstants.JSON_TYPE)
//                    .build();
//            return chain.proceed(request);
//        });

        builder.sslSocketFactory(trustManager.sslSocketFactory,trustManager.trustManager);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });



//        builder.certificatePinner()
//        builder.sslSocketFactory()

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())  //gson 转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // Rx 工厂。
                .baseUrl(BuildConfig.BASE_URL)
                .build();
    }


    //构造方法私有
    private HttpMethods(String url) {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }


    //-----------------------------------------

    /**
     * this is made for APR structure;
     * @param subscriber
     * @param address
     * @param params
     */
    public void runServerUrlKey(Observer<ResponseBody> subscriber, String address, Object... params){
        if (params.length % 2 != 0){
            Log.e("runServerUrlKey", "[ERROR]params count cant devide by 2");
        }

        Map<String, Object> reqParamMap = new HashMap<>();
        if (params != null) {
            for (int i = 0; i < params.length; i += 2) {
                reqParamMap.put((String)params[i], params[i + 1]);
            }
        }

        CommonQueueService service = retrofit.create(CommonQueueService.class);
//        Observable observable = service.postRxString(commonReqBody(address,reqParamMap)).map(new EAMHttpResultFunc<String>());
        Observable observable = service.postRxBody(commonReqBody(address,reqParamMap));
        toSubscribe(observable, subscriber);
    }


//----------------------------------------------------------------------------------------------------------------------------------------

//  普通转换器
//    /**
//     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
//     *
//     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
//     */
//    private class EAMHttpResultFunc<T> implements Func1<EAMHttpResult<T>, T> {
//
//        @Override
//        public T call(EAMHttpResult<T> httpResult) {
//            if (httpResult.getStatus() == 1) {
//                String str = (String)httpResult.getBody();
//                String codestr = (String)httpResult.getCode();
//                throw new ApiException(codestr == null ? "" : codestr ,str == null ? "" : str);
//
////                throw new ApiException(httpResult.getCode(),(String)httpResult.getBody());
//            }
//            return httpResult.getBody();
//        }
//    }
//
    /**
     * 组装消息体
     */
    public Map<String, Object> commonReqBody(String path, Map<String, Object> params){
        Map<String, Object> m = new HashMap<>();
        Map<String, Object> clsm = new HashMap<>();
        clsm.put("reqName", path);
        m.put("head",clsm);
        m.put("body",params);
        return  m;
    }

//      中间件 --> 转换器






    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
//    private class EAMHttpResultFunc<T> implements Func1<EAMHttpAsynResult<String>, T> {
//
//        @Override
//        public T call(EAMHttpAsynResult<String> httpResult) {
////            {"message":"non-standard access",
////             "messageJson":"{\"RESPONSE_IDENTITY\":\"10.19.175.167-echoServer-1580091486795084931\",\"code\":\"NO_REALNAME\",\"status\":\"1\"}",
////             "status":"1"}
//            //中间件的status == 1 判断。貌似是后来改的。原来写的时候中间件通过貌似是0。现在貌似与接口一致了？？
////            if(httpResult.getStatus() == 1){
////                throw new ApiException(httpResult.getMessage());
////            }
//
//            String messageJson = httpResult.getMessageJson();
//            if(TextUtils.isEmpty(messageJson)){
//                throw new ApiException(httpResult.getMessage());
//            }
//
//            EAMHttpResult<T> ehr = g.fromJson(httpResult.getMessageJson(),EAMHttpResult.class);
//
//            if (null == ehr || ehr.getStatus() == 1) {
//                String str = (String)ehr.getBody();
//                String codestr = (String)ehr.getCode();
//                throw new ApiException(codestr == null ? "" : codestr ,str == null ? "" : str);
//            }
//            return ehr.getBody()==null? (T)"" :ehr.getBody();
//        }
//    }
//
//
//
//    /**
//     * 组装消息体
//     */
//    public Map<String, String> commonReqBody(String path, Map<String, Object> params){
//        String bCode = TransBusinessCode.businessCode(path);
//
//        Map<String, String> encryptedMap = new HashMap<>();
//        Map<String, Object> m = new HashMap<>();
//        Map<String, Object> clsm = new HashMap<>();
//        clsm.put("reqName", path);
//        m.put("head",clsm);
//        m.put("body",params);
//
//
//        String jsonStr = g.toJson(m);
//        L.i("KHTTP_ARGS  ",jsonStr);
//        L.i("KHTTP_md5   ", MD5Util.MD5(jsonStr+BuildConfig.MD5_KEY));
//
//
//        encryptedMap.put("businessName", bCode);
//        encryptedMap.put("syncFlag", "1");
//        encryptedMap.put("appKey", BuildConfig.EAM_APP_KEY);
//        encryptedMap.put("md5", MD5Util.MD5(jsonStr+BuildConfig.MD5_KEY));
//        encryptedMap.put("messageJson", jsonStr);
//
//        return encryptedMap;
//    }







//----------------------------------------------------------------------------------------------------------------------------------------


//观察者启动器
    private <T> void toSubscribe(Observable<T> o, Observer<T> s){
        o.subscribeOn(Schedulers.io()) //绑定在io
                .observeOn(AndroidSchedulers.mainThread()) //返回 内容 在Android 主线程
                .subscribe(s);  //放入观察者
    }






}
