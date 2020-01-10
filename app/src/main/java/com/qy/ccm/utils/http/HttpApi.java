package com.qy.ccm.utils.http;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/***
 * 存放所有的Api
 * @author xq
 */

public interface HttpApi {
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> postDataForQuery(@Url String url, @Field("addr") String addr);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> postDataForMap(@Url String url, @FieldMap Map<String, Object> map);

    @GET
    Observable<ResponseBody> getDataForMap(@Url String url, @QueryMap Map<String, Object> map);

    @GET
    Call<ResponseBody> getCallForMap(@Url String url, @QueryMap Map<String, String> map);

    @Multipart
    @POST
    Observable<ResponseBody> uploadSingle(@Url String url, @QueryMap Map<String, String> map, @Part MultipartBody.Part files);

}
