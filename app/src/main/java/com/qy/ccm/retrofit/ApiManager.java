package com.qy.ccm.retrofit;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;


/**
 * Created by zhoufan on 2018/1/3.
 * Retrofit的请求
 * <p>
 * 注意点：
 * 1》在进行Post请求的时候,注解 @FormUrlEncoded 和  @Multipart只能选择一个
 * 因为二者都属于表单提交，不过 @Multipart 是支持文件上传的表单
 * 2》注解 @FormUrlEncoded 代表的是纯字符串的Post请求
 * 3》注解 @Multipart 代表的是包含文件的Post请求
 * 4》使用注解 @Multipart 的时候注意 不允许使用 @FieldMap 注解 ，否则会提示
 * <p>
 * FieldMap parameters can only be used with form encoding 错误
 * 5》使用 @Multipart + @PartMap 的使用注意， @PartMap里面不能写成 Map<String, Object>
 * 因为服务器端会给传递过去的参数加上双引号，导致字段无法被解析
 * 解决方法是 将Map<String, Object> 转换成 Map<String, RequestBody> maps即可
 * 6》 @Field @FieldMap @Part @PartMap 用于表单提交
 * Field @FieldMap 与  @FormUrlEncoded 配合使用
 * Part @PartMap   与  @Multipart 配合使用
 * FieldMap 的接受类型是 Map<String, String>
 * PartMap 的接受类型是 Map<String, RequestBody>
 * 7》 @Path @Query @QueryMap @Url  用于Url
 */

public interface ApiManager {
    // GET请求(无参，无api)
    @GET("mobile/common/{api}")
    Observable<String> getData(@Path("api") String api);

    // GET请求(有参，无api)
    @GET("mobile/common/{api}")
    Observable<String> getData(@Path("api") String api, @QueryMap TreeMap<String, Object> map);

    // GET请求(有参，有api)
    @GET("mobile/common/")
    Observable<String> getData(@QueryMap TreeMap<String, Object> map);

    // Post请求(无参，无api)
    @POST("mobile/common/{api}")
    Observable<String> postData(@Path("api") String api);

    // Post请求(有参，无api)
    @POST("mobile/common/{api}")
    Observable<String> postData(@Path("api") String api, @Body RequestBody requestBody);

    // Post请求(有参，有api)
    @FormUrlEncoded
    @POST("mobile/common/")
    Observable<String> postData(@Body RequestBody requestBody);

    // Post请求
    @FormUrlEncoded
    @POST("mobile/common/")
    Observable<String> postData(@FieldMap Map<String, Object> maps, @Query("meta[]") String... linked);

    //    需要登录-------------------------------------------------------
    // GET请求(无参，无api)
    @GET("mobile/{api}")
    Observable<String> getLoginData(@Path("api") String api);

    // GET请求(有参，无api)
    @GET("mobile/{api}")
    Observable<String> getLoginData(@Path("api") String api, @QueryMap TreeMap<String, Object> map);

    // GET请求(有参，有api)
    @GET("mobile/")
    Observable<String> getLoginData(@QueryMap TreeMap<String, Object> map);

    // Post请求(无参，无api)
    @POST("mobile/{api}")
    Observable<String> postLoginData(@Path("api") String api);

    // Post请求(有参，无api)

    @POST("mobile/common/{api}")
    Observable<String> postLoginData(@Path("api") String api, @QueryMap Map<String, Object> maps);

    // Post请求(有参，有api)
    @FormUrlEncoded
    @POST("mobile/")
    Observable<String> postLoginData(@Body RequestBody requestBody);

    // Post请求
    @FormUrlEncoded
    @POST("mobile/")
    Observable<String> postLoginData(@FieldMap Map<String, Object> maps, @Query("meta[]") String... linked);

    //    查询余额
// Post请求(有参，无api)

    @Headers("Authorization: Basic b21uaWNvcmU6ZjZ2aDEzdzZJVWdlTmhxMUdHMUk=")
    @GET("{api}")
    Observable<String> commonGetData(@Path("api") String api, @QueryMap TreeMap<String, Object> map);

    // Post请求(有参，无api)
    @Headers({
            "Authorization: Basic b21uaWNvcmU6ZjZ2aDEzdzZJVWdlTmhxMUdHMUk=",
            "Accept: application/json, text/plain, */*",
            "Content-Type: application/json;charset=utf-8"
    })
    @POST("{api}")
    Observable<String> commonPostData(@Path("api") String api, @Body RequestBody requestBody);

}
