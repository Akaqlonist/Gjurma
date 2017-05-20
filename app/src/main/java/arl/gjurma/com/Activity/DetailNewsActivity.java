package arl.gjurma.com.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import arl.gjurma.com.Extra.Constants;
import arl.gjurma.com.Gjurma;
import arl.gjurma.com.Models.RecentPost;
import arl.gjurma.com.R;
import arl.gjurma.com.Rest.ApiInterface;
import arl.gjurma.com.Rest.GjurmaApiClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import us.feras.mdv.MarkdownView;

/**
 * Created by KRYTON on 26-09-2016.
 */
public class DetailNewsActivity extends AppCompatActivity {
    @Bind(R.id.header)
    ImageView header;
    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    int mutedColor = R.attr.colorPrimary;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_author)
    TextView tvAuthor;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_description)
    TextView tvDescription;
    @Bind(R.id.btn_fav)
    FloatingActionButton btnFav;
    @Bind(R.id.markdownView)
    MarkdownView markdownView;
    @Bind(R.id.progress_view)
    CircularProgressView progressView;
    /*@Bind(R.id.videoview)
    FullscreenVideoLayout videoview;*/
    @Bind(R.id.tv_header)
    MarkdownView tvHeader;
    @Bind(R.id.tv_footer)
    MarkdownView tvFooter;
    @Bind(R.id.iv_video)
    ImageView ivVideo;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.rl_video)
    RelativeLayout rlVideo;
    /*@Bind(R.id.custom_videoplayer_standard)
    JCVideoPlayerStandard customVideoplayerStandard;*/
    private RecentPost mRecentPost;
    private int mIndex;
    private String TAG = "DetailNewsActivity";
    SharedPreferences prefs;
    SharedPreferences.Editor sEdit;
    Set<String> jsonData;
    private ApiInterface apiService;

    //    Set<String> title;
//    Set<String> author;
//    Set<String> date;
//    Set<String> description;
//    Set<String> url;
    private boolean isFav = false;
    private int mSavedIndex = 0;
    ArrayList<String> myJson;
    ArrayList<String> myTitle = new ArrayList<>();
    //    ArrayList<String> myAuthor;
//    ArrayList<String> myDate;
//    ArrayList<String> myDescription;
//    ArrayList<String> myUrl;
    private String mTitle;
    private String mAuthor;
    private String mDate;
    private String mDescription;
    private String mURL;
    private String mVideo;
    private String mHeader;
    private String mFooter;
    private AdView mAdView;
    InterstitialAd mInterstitialAd;

    // Code by James
    private class Timer extends Handler {
        @Override
        public void handleMessage(Message msg) {

            //mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));


            if(isBehind == false) {
                mInterstitialAd.loadAd(adRequest);
            }

            this.sendEmptyMessageDelayed(0, Constants.adLoadingDelay);
        }
    };

    private Timer adTimer;
    AdRequest adRequest;
    private boolean isBehind = false;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        ButterKnife.bind(this);
        progressView.setVisibility(View.GONE);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs = getSharedPreferences("FavPost", Context.MODE_PRIVATE);
        sEdit = prefs.edit();
        jsonData = prefs.getStringSet("json", null);

        /****************** Admob banner added here***************/
        mAdView = (AdView) findViewById(R.id.adView);


        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("CF428596370A102513B75B837816D1DB")
                .build();
        mAdView.loadAd(adRequest);

        /*******************************************************/
        /************* Admob Interstitial added here **********/
        mInterstitialAd = new InterstitialAd(this);


        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        /*
        // Load ads into Interstitial Ads , Modified By James
    //    Gjurma global = (Gjurma)(this.getApplication());
    //    if(global.isReadyToLoadAds() == true) {
        mInterstitialAd.loadAd(adRequest);

      //      global.triggerTimer();
      //  }
        */
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });


        //Code by James
        adTimer = new Timer();
        adTimer.sendEmptyMessage(0);

        /*****************************************************/


        if (jsonData != null) {
            myJson = new ArrayList<String>(jsonData);
//            myTitle = new ArrayList<String>(title);
//            myAuthor = new ArrayList<String>(author);
//            myDate = new ArrayList<String>(date);
//            myDescription = new ArrayList<String>(description);
//            myUrl = new ArrayList<String>(url);
        } else {
            myJson = new ArrayList<>();
        }

        for (int i = 0; i < myJson.size(); i++) {
            JSONObject obj = null;
            try {
                obj = new JSONObject(myJson.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                myTitle.add(obj.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getIntentData();
        collapsingToolbar.setTitle(" ");
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.e(TAG, verticalOffset + "*****" + scrollRange + "*****" + (scrollRange + verticalOffset));
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("" + Html.fromHtml(mTitle));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                } else if ((scrollRange + verticalOffset) < (scrollRange / 2)) {
                    collapsingToolbar.setTitle("" + Html.fromHtml(mTitle));
                } else {
                    collapsingToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        Log.e(TAG, "Dimen*****" + getResources().getDimensionPixelSize(R.dimen._220sdp));
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Click"+isFav);
                if (isFav) {
                    myJson.remove(mSavedIndex);
//                    myTitle.remove(mSavedIndex);
//                    myAuthor.remove(mSavedIndex);
//                    myDate.remove(mSavedIndex);
//                    myDescription.remove(mSavedIndex);
//                    myUrl.remove(mSavedIndex);

//                    Set<String> set1 = new HashSet<String>();
//                    Set<String> set2 = new HashSet<String>();
//                    Set<String> set3 = new HashSet<String>();
//                    Set<String> set4 = new HashSet<String>();
//                    Set<String> set5 = new HashSet<String>();
                    Set<String> set6 = new HashSet<String>();

//                    set1.addAll(myTitle);
//                    set2.addAll(myAuthor);
//                    set3.addAll(myDate);
//                    set4.addAll(myDescription);
//                    set5.addAll(myUrl);
                    set6.addAll(myJson);

//                    sEdit.putStringSet("title", set1);
//                    sEdit.putStringSet("author", set2);
//                    sEdit.putStringSet("date", set3);
//                    sEdit.putStringSet("description", set4);
//                    sEdit.putStringSet("url", set5);
                    sEdit.putStringSet("json", set6);
                    sEdit.commit();
                    isFav = false;
                    btnFav.setImageResource(R.drawable.ic_favourite_unselect);
                } else {
                    if (myJson == null) {
                        Log.e(TAG, "Array list is null");
                        myJson = new ArrayList<String>();
//                        myTitle = new ArrayList<String>();
//                        myAuthor = new ArrayList<String>();
//                        myDate = new ArrayList<String>();
//                        myDescription = new ArrayList<String>();
//                        myUrl = new ArrayList<String>();
                    }
                    Random r = new Random();
                    int i1 = r.nextInt(999999 - 0) + 0;
//                    Log.e(TAG,i1+"      BEFORE SIZE = "+myAuthor.size());

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("title", mTitle+"");
                        obj.put("author", mAuthor+"");
                        obj.put("date", mDate+"");
                        obj.put("description", mDescription+"");
                        obj.put("url", mURL+"");
                        obj.put("header", mHeader+"");
                        obj.put("footer", mFooter+"");
                        obj.put("video", mVideo+"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    myJson.add(obj + "");
                    Log.e(TAG,"obj="+obj);
//                    myTitle.add(mTitle);
//                    myAuthor.add(i1+":"+mAuthor);
//                    myDate.add(mDate);
//                    myDescription.add(mDescription);
//                    myUrl.add(mURL);
//                    Log.e(TAG, "AFTER SIZE = " + myAuthor.size());

//                    Set<String> set1 = new HashSet<String>();
//                    Set<String> set2 = new HashSet<String>();
//                    Set<String> set3 = new HashSet<String>();
//                    Set<String> set4 = new HashSet<String>();
//                    Set<String> set5 = new HashSet<String>();
                    Set<String> set6 = new HashSet<String>();

//                    set1.addAll(myTitle);
//                    set2.addAll(myAuthor);
//                    set3.addAll(myDate);
//                    set4.addAll(myDescription);
//                    set5.addAll(myUrl);
                    set6.addAll(myJson);

//                    sEdit.putStringSet("title", set1);
//                    sEdit.putStringSet("author", set2);
//                    sEdit.putStringSet("date", set3);
//                    sEdit.putStringSet("description", set4);
//                    sEdit.putStringSet("url", set5);
                    sEdit.putStringSet("json", set6);
                    sEdit.commit();
                    isFav = true;
                    btnFav.setImageResource(R.drawable.ic_favourite_select);
                }
            }
        });

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }

        //Code by James
        this.isBehind = true;

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }

        //Code By James
        this.isBehind = false;
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @OnClick({R.id.iv_video, R.id.iv_play})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video:
                Intent i = new Intent(DetailNewsActivity.this, VideoPlayerActivity.class);
                i.putExtra("link", mVideo);
                startActivity(i);
                break;
            case R.id.iv_play:
                Intent i1 = new Intent(DetailNewsActivity.this, VideoPlayerActivity.class);
                i1.putExtra("link", mVideo);
                startActivity(i1);
                break;
        }
    }


    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id;
            if (source.equals("hughjackman.jpg")) {
                id = R.drawable.lggjurma;
            } else {
                return null;
            }

            Drawable d = getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
        }
    }

    ;

    private void getIntentData() {
        if (getIntent().getIntExtra("type", 0) == 5) {//website
            String uri = getIntent().getStringExtra("uri");
            Log.e(TAG, "" + uri);
            getPost(uri);
        } else if (getIntent().getIntExtra("type", 0) == 10) {//notification
            Log.e(TAG, "API Link=" + getIntent().getStringExtra("link"));
        } else {
            mTitle = getIntent().getStringExtra("title");
            mAuthor = getIntent().getStringExtra("author");
            mDate = getIntent().getStringExtra("date");
            mDescription = getIntent().getStringExtra("description");
            mURL = getIntent().getStringExtra("url");
            mVideo = getIntent().getStringExtra("video");
            mHeader = getIntent().getStringExtra("header");
            mFooter = getIntent().getStringExtra("footer");
            setData();
        }

    }

    private void getPost(String uri) {
        String postTitle[] = uri.split("/");
        int index = postTitle.length;
        String query = postTitle[index - 1];
        Log.e(TAG, "Query = " + query);
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
        apiService = GjurmaApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.getPost(query);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "All " + response.body());
                if (response.code() == 200) {
                    JsonObject jo = response.body().get("post").getAsJsonObject();
                    mTitle = jo.get("title").getAsString();
                    mAuthor = jo.get("author").getAsJsonObject().get("name").getAsString();
                    mDate = jo.get("date").getAsString();
                    mDescription = jo.get("content").getAsString();
                    if(jo.get("custom_fields").getAsJsonObject().get("footer")!=null)
                        mFooter = jo.get("custom_fields").getAsJsonObject().get("footer").getAsJsonArray().get(0).getAsString();
                    if(jo.get("custom_fields").getAsJsonObject().get("header")!=null)
                        mHeader = jo.get("custom_fields").getAsJsonObject().get("header").getAsJsonArray().get(0).getAsString();
                    if(jo.get("custom_fields").getAsJsonObject().get("video")!=null)
                        mVideo = jo.get("custom_fields").getAsJsonObject().get("video").getAsJsonArray().get(0).getAsString();

                    String url = null;
                    Matcher m = Pattern.compile(" (src)=\"([^\"]+)").matcher(mDescription);
                    while (m.find()) {
                        url = m.group(2);
                    }
                    mURL = url;
                    Log.e(TAG, "" + mTitle);
                    Log.e(TAG, "" + mAuthor);
                    Log.e(TAG, "" + mDate);
                    Log.e(TAG, "" + mDescription);
                    Log.e(TAG, "" + mURL);
                    Log.e(TAG,"**************"+mFooter);
                    Log.e(TAG,"**************"+mHeader);
                    Log.e(TAG,"**************"+mVideo);
                    setData();
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onResponse: fail");
                progressView.stopAnimation();
                progressView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void setData() {
        tvTitle.setText("" + Html.fromHtml(mTitle));
        tvAuthor.setText("by " + mAuthor);
        tvAuthor.setVisibility(View.GONE);
        tvDate.setText("" + mDate);
//        tvDescription.setText(Html.fromHtml(mDescription));
        tvDescription.setVisibility(View.GONE);
        markdownView.loadMarkdown(mDescription);
        markdownView.cancelLongPress();
        markdownView.setClickable(false);
        markdownView.setLongClickable(false);
        markdownView.setPressed(false);
//        markdownView.callOnClick();
        /*videoview.setActivity(this);
        if (mVideo == null || mVideo.equals("")) {
            videoview.setVisibility(View.GONE);
        } else {
            videoview.setVisibility(View.VISIBLE);

            Uri videoUri = Uri.parse(mVideo);
            try {
                videoview.setVideoURI(videoUri);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }*/

        if (mVideo == null || mVideo.equals("") || mVideo.equals("null")) {
//            ivVideo.setVisibility(View.GONE);
            rlVideo.setVisibility(View.GONE);
        } else {
            rlVideo.setVisibility(View.VISIBLE);
//            ivVideo.setVisibility(View.VISIBLE);
           /* Log.e(TAG, "Video=" + mVideo);
            try {
                ivVideo.setImageBitmap(retriveVideoFrameFromVideo(mVideo));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }*/
        }

       /* customVideoplayerStandard.setUp(mVideo
                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");*/

        Log.e(TAG,"Detail="+mHeader+"*****"+mFooter+"*****"+mVideo);
        if (mHeader == null || mHeader.equals("") || mHeader.equals("null"))
            tvHeader.setVisibility(View.GONE);
        else
            tvHeader.loadMarkdown(mHeader);

        if (mFooter == null || mFooter.equals("") || mFooter.equals("null"))
            tvFooter.setVisibility(View.GONE);
        else
            tvFooter.loadMarkdown(mFooter);

        if (mURL != null) {
            Picasso.with(this)
                    .load(mURL)
                    .into(header);
        } else {
            header.setBackground(getResources().getDrawable(R.drawable.lggjurma));
        }

        if (myTitle != null) {
            if (myTitle.contains(mTitle)) {
                isFav = true;
                mSavedIndex = myTitle.indexOf(mTitle);
                btnFav.setImageResource(R.drawable.ic_favourite_select);
            }
        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}
