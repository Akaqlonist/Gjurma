package arl.gjurma.com.Rest;

import com.google.gson.JsonObject;

import java.util.Map;

import arl.gjurma.com.Models.CategoryResponseOld;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by KRYTON on 25-09-2016.
 */
public interface ApiInterface {
//    @FormUrlEncoded
    @GET("category1.json")
    Call<JsonObject> getRecentNewsType();

    @GET("api/get_recent_posts/")
    Call<JsonObject> getRecentNews(@QueryMap Map<String, String> options);
    @GET("api/get_recent_posts/")
    Call<JsonObject> getRecentNewsTest(@Query("count") int index);

    @GET("api/get_category_index/")
    Call<CategoryResponseOld> getCategoryOld();

    @GET("category.json")
    Call<JsonObject> getCategory();

    @GET("api/get_category_posts/")
    Call<JsonObject> getByCategory(@QueryMap Map<String, String> options);
//    Call<JsonObject> getByCategory(@Query(value = "category_id", encoded = false) String mIndex);

    @GET("api/get_post/")
    Call<JsonObject> getPost(@Query(value = "post_slug", encoded = true) String post);

    @GET("popup.json")
    Call<JsonObject> isBlock();

    @FormUrlEncoded
    @POST("insertToken.php")
    Call<JsonObject> insertToken(@Field("token_id") String tokenID);
}
