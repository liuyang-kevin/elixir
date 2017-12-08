package com.echoesnet.crowdfunding.net;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CommonQueueService {
//    @Headers({"Content-Type: application/json","Accept: application/json"})

    @POST("queue")
    Observable<ResponseBody> postRxBody(@Body Map<String, Object> reqParamMap);

//
//    @POST("queue")
//    Observable<EAMHttpResult<String>> postRxString(@Body Map<String, Object> reqParamMap);


//    @FormUrlEncoded
//    @POST("queue")
//    Observable<EAMHttpAsynResult<String>> postRxString(@FieldMap Map<String, String> reqParamMap);
}
