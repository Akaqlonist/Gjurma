package arl.gjurma.com.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import arl.gjurma.com.Rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by KRYTON on 26-09-2016.
 */
public class SplashActivity extends AppCompatActivity {
    private Retrofit retrofit = null;
    private ApiInterface apiService;
    private String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(arl.gjurma.com.R.layout.activity_splash);
        Log.e(TAG, "FCM TOKEN " + FirebaseInstanceId.getInstance().getToken());
        postToken();
        /*new Handler().postDelayed(new Runnable() {

            *//*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             *//*

            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 600);*/

        retrofit = new Retrofit.Builder()
                .baseUrl("http://globisoft.net/app/android/gjurma/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiInterface.class);

        Call<JsonObject> call = apiService.isBlock();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "All " + response.body());
                if (response.code() == 200) {
                    if(response.body().has("updateLink")){
                        Intent i = new Intent(SplashActivity.this,BlockPOPUpActivity.class);
                        i.putExtra("link",response.body().get("updateLink").getAsString());
                        i.putExtra("title",response.body().get("messageHead").getAsString());
                        i.putExtra("message",response.body().get("message").getAsString());
                        startActivity(i);
                        finish();
                    }else {
                        Intent i = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }else{
                    Intent i = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onResponse: fail");
                Intent i = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void postToken() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://globisoft.net/app/android/gjurma/fcm/")
//                .baseUrl("http://192.168.43.253/fcm/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiInterface.class);

        Call<JsonObject> call = apiService.insertToken(FirebaseInstanceId.getInstance().getToken());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "All " + response.body());
                if (response.code() == 200) {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onResponse: fail");
            }
        });
    }
}
