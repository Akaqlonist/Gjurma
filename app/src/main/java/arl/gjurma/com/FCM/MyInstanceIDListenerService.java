package arl.gjurma.com.FCM;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;

import arl.gjurma.com.Rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vishal on 14/9/16.
 */
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    private ApiInterface apiService;
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
        postToken();
    }

    private void sendRegistrationToServer(String refreshedToken) {

    }

    private void postToken() {
        Retrofit retrofit = new Retrofit.Builder()
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
