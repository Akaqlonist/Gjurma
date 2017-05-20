package arl.gjurma.com;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by KRYTON on 28-09-2016.
 */
public class Gjurma extends Application{
    private static Gjurma mInstance = new Gjurma();
    private static Context mAppContext;
    public static final String TAG = Gjurma.class.getSimpleName();
    private RequestQueue mRequestQueue;

    public static Gjurma getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    //Code by James
    /*
    private boolean isReadyToLoadAds = false;
    private int adLoadingDelay = 240000;

    private void allowAds() { isReadyToLoadAds = true;};
    private void blockAds() { isReadyToLoadAds = false;};

    public boolean isReadyToLoadAds(){return isReadyToLoadAds;};

    public void triggerTimer() {

        blockAds();
        this.globalTimer.sendEmptyMessageDelayed(0, adLoadingDelay);};

    private class TimingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {

            allowAds();
            //this.sendEmptyMessageDelayed(0, adLoadingDelay);
        }
    } ;
    private TimingHandler globalTimer;
    */
    /////////////////////////////////////////////////

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        mAppContext=this.getApplicationContext();

        /*
        //Code by James follows
        globalTimer = new TimingHandler();
        allowAds();
        */
        //////////////////////////////////////////
//        FontCache.getInstance().addFont("arki-bold","Arkitech-Bold.TTF");
//        FontCache.getInstance().addFont("myriad","MyriadPro-BoldCond.OTF");
//        FontCache.getInstance().addFont("myriadRegular","MyriadPro-Regular.OTF");
//        FontCache.getInstance().addFont("myriadCond","MyriadPro-Cond.OTF");
//        Picasso p=new Picasso.Builder(this).defaultBitmapConfig(Bitmap.Config.RGB_565).build();
//        Picasso.setSingletonInstance(p);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

}
