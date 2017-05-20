package arl.gjurma.com.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import arl.gjurma.com.Adapter.RecentNewsAdapter;
import arl.gjurma.com.Extra.RecyclerItemClickListener;
import arl.gjurma.com.Gjurma;
import arl.gjurma.com.Models.CategoryNewPOJO;
import arl.gjurma.com.Models.RecentNewsFilter;
import arl.gjurma.com.Models.RecentPost;
import arl.gjurma.com.R;
import arl.gjurma.com.Rest.ApiInterface;
import arl.gjurma.com.Rest.GjurmaApiClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by KRYTON on 30-09-2016.
 */
public class ListByCategoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerViewCategory)
    RecyclerView recyclerViewCategory;
    @Bind(R.id.progress_view)
    CircularProgressView progressView;
    private CategoryNewPOJO mCategory;
    private ApiInterface apiService;
    private RecentNewsAdapter adapter;
    private RecentPost mRecentPost;
    private List<RecentNewsFilter> mList = new ArrayList<>();
    private List<RecentNewsFilter> mListFilter = new ArrayList<>();
    private String TAG = "ListByCategoryActivity";
    private String mQuery;
    private String mCategoryStrng = "";
    private String mIncludeStrng = "";
    private String mCountStrng = "";
    Map<String, String> params = new HashMap<String, String>();
    private AdView mAdView;
    InterstitialAd mInterstitialAd;
    private boolean isSearch = false;
    private String mSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        ButterKnife.bind(this);
        mCategory = getIntent().getParcelableExtra("category");
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(mCategory.getQueryArray());

        getVolley(mCategory.getLink());
        /****************** Admob banner added here***************/
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("CF428596370A102513B75B837816D1DB")
                .build();
        mAdView.loadAd(adRequest);

        /*******************************************************/
        /************* Admob Interstitial added here **********/
        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        // Load ads into Interstitial Ads , Modified By James
        /*
        Gjurma global = (Gjurma)(this.getApplication());
        if(global.isReadyToLoadAds() == true) {
            mInterstitialAd.loadAd(adRequest);

            global.triggerTimer();
        }
        */

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
        /*****************************************************/


//        String jsonQuery = mCategory.getQueryArray();
//        jsonQuery = jsonQuery.replace("[","");
//        jsonQuery = jsonQuery.replace("]","");
//        String queryArray[] = jsonQuery.split("},");
//        Log.e(TAG,queryArray.length+"**********"+jsonQuery);

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject rec = jsonArray.get(i).getAsJsonObject();
            if (rec.has("category")) {
                String tmp = rec.get("category").toString();
                tmp = tmp.replace("\"", "");
                mCategoryStrng = tmp;
//                params.put("category_id", tmp);
                Log.e(TAG, i + "****" + rec + "****" + tmp + "****Category");
            }
            if (rec.has("include")) {
                String tmp = rec.get("include").toString();
                tmp = tmp.replace("\"", "");
                mIncludeStrng = tmp;
//                params.put("include", tmp);
                Log.e(TAG, i + "****" + rec + "****" + tmp + "****include");
            }
            if (rec.has("count")) {
                String tmp = rec.get("count").toString();
                tmp = tmp.replace("\"", "");
                mCountStrng = tmp;
//                params.put("count", tmp);
                Log.e(TAG, i + "****" + rec + "****" + tmp + "****count");
            }
            Log.e(TAG, "count" + rec);
        }


        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // do stuff
            Log.e(TAG, "Key=" + key + "****Value=" + value);
        }


//        if(jsonArray.contains("df")){
//
//        }
        /*JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(jsonQuery);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonObj.has("category")){
            try {
                params.put("category_id", jsonObj.get("category").toString());
                Log.e(TAG, "*******" + jsonObj.get("category").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObj.has("include")){
            try {
                params.put("include", jsonObj.get("include").toString());
                Log.e(TAG, "*******" + jsonObj.get("include").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObj.has("count")){
            try {
                params.put("count", jsonObj.get("count").toString());
                Log.e(TAG,"*******"+jsonObj.get("count").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        toolbar.setTitle("" + mCategory.getTitle());
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String CurrentString = mCategory.getLink();
        String[] separated = CurrentString.split("category_id=");
        mQuery = separated[1];
//        for(int i=0;i<separated.length-1;i++){
//            mQuery = mQuery+""+separated[i+1];
//        }
//        mQuery = separated[1];
        Log.e(TAG, "" + mCategory.getTitle());
        Log.e(TAG, separated.length + "---" + mQuery + "******************" + CurrentString);
//        getList();

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
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void getVolley(String asString) {
        StringRequest stringRequest = new StringRequest(asString, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson g = new Gson();
                mRecentPost = g.fromJson(response, RecentPost.class);
                Log.d(TAG, "Size=" + mRecentPost.getPosts().size());
                diplayNews();
                progressView.stopAnimation();
                progressView.setVisibility(View.GONE);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"**********************"+error);
                progressView.stopAnimation();
                progressView.setVisibility(View.GONE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getList() {


        if (!mCategoryStrng.equals(""))
            params.put("category_id", mCategoryStrng);

        if (!mIncludeStrng.equals(""))
            params.put("include", mIncludeStrng);

        if (!mCountStrng.equals(""))
            params.put("count", mCountStrng);

//        params.put("category_id", "11");
//        params.put("include", "title,date,content");
//        params.put("count", "10");

//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mQuery);
        progressView.startAnimation();
        apiService = GjurmaApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.getByCategory(params);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "All " + response.body());
                if (response.code() == 200) {

                    Gson g = new Gson();
                    mRecentPost = g.fromJson(response.body(), RecentPost.class);
                    Log.d(TAG, "Size=" + mRecentPost.getPosts().size());
                    diplayNews();
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

        /*JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                mCategory.getLink(), null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String name = response.getString("category");
//                    String email = response.getString("email");
//                    JSONObject phone = response.getJSONObject("phone");
//                    String home = phone.getString("home");
//                    String mobile = phone.getString("mobile");
                    Gson g = new Gson();
                    mRecentPost = g.fromJson(response.toString(), RecentPost.class);
                    Log.d(TAG, "Size=" + mRecentPost.getPosts().size());
                    diplayNews();
                    Log.e(TAG,name+"*****************************"+response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });
        Gjurma.getInstance().addToRequestQueue(jsonObjReq);*/
    }

    private void diplayNews() {
        for (int i = 0; i < mRecentPost.getPosts().size(); i++) {
            RecentNewsFilter filter = new RecentNewsFilter();
            if (mRecentPost.getPosts().get(i).getTitle() == null) {
                filter.setmTitle("");
            } else {
                filter.setmTitle(mRecentPost.getPosts().get(i).getTitle());
            }


            if (mRecentPost.getPosts().get(i).getAuthor() == null) {
                filter.setmAuthor("");
            } else {
                filter.setmAuthor(mRecentPost.getPosts().get(i).getAuthor().getName());
            }


            if (mRecentPost.getPosts().get(i).getDate() == null) {
                filter.setmDate("");
            } else {
                filter.setmDate(mRecentPost.getPosts().get(i).getDate());
            }


            if (mRecentPost.getPosts().get(i).getContent() == null) {
                filter.setmDescription("");
            } else {
                filter.setmDescription(mRecentPost.getPosts().get(i).getContent());
            }

            if(mRecentPost.getPosts().get(i).getCustom_fields().getVideo()!=null)
                filter.setmVideo(mRecentPost.getPosts().get(i).getCustom_fields().getVideo()[0]);
            else
                filter.setmVideo(null);

            if(mRecentPost.getPosts().get(i).getCustom_fields().getHeader()!=null)
                filter.setmHeader(mRecentPost.getPosts().get(i).getCustom_fields().getHeader()[0]);
            else
                filter.setmHeader(null);

            if(mRecentPost.getPosts().get(i).getCustom_fields().getFooter()!=null)
                filter.setmFooter(mRecentPost.getPosts().get(i).getCustom_fields().getFooter()[0]);
            else
                filter.setmFooter(null);
            /*if(mRecentPost.getPosts().get(i).getAttachments().size()>0){
                if(mRecentPost.getPosts().get(i).getAttachments().get(0).getUrl()!=null){
                    filter.setmImage(mRecentPost.getPosts().get(i).getAttachments().get(0).getUrl());
                }else {
                    filter.setmImage("null");
                }
            }else {
                if(mRecentPost.getPosts().get(i).getThumbnail()!=null){
                    filter.setmImage(mRecentPost.getPosts().get(i).getThumbnail());
                }else {
                    filter.setmImage("null");
                }
            }*/

            String url = null;
            String imgRegex = "(?i)<img[^>]+?src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

            Pattern p = Pattern.compile(imgRegex);
            Matcher m = p.matcher(mRecentPost.getPosts().get(i).getContent());

            while (m.find()) {
                String imgSrc = m.group(1);
                url = imgSrc;
//                String imagePath = "file://" + base + "/test.jpg";
//                imgSrc=imgSrc.replace(imgSrc,imagePath);

                Log.e(TAG, "++++++" + imgSrc);


            }
            /*Matcher m = Pattern.compile(" (src)=\"([^\"]+)").matcher(mRecentPost.getPosts().get(i).getContent());
            while (m.find()) {
//                System.out.println(m.group(1));
//                Log.e("TAG","****************"+m.group(2)+"   "+m.groupCount());
                url = m.group(2);
            }*/
            filter.setmImage(url);

            mList.add(filter);
            mListFilter.add(filter);
        }

        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecentNewsAdapter(this, mList);
        recyclerViewCategory.setAdapter(adapter);

        recyclerViewCategory.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "" + position);
                if(position==(mList.size())){
                    Log.e(TAG,"More");
                    if(isSearch){
                        String url = "http://gjurma.com/?s="+mSearchQuery;
                        Log.e(TAG,"More click "+url);
                        Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent1);
                    }else {
                        String url = "http://gjurma.com/more.php";
                        Log.e(TAG,"More click "+url);
                        Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent1);
                    }
                }else {
                    Intent i = new Intent(ListByCategoryActivity.this, DetailNewsActivity.class);

                    String mTitle = mList.get(position).getmTitle();
                    String mAuthor = mList.get(position).getmAuthor();
                    String mDate = mList.get(position).getmDate();
                    String mDescription = mList.get(position).getmDescription();
                    String mURL = mList.get(position).getmImage();
                    String mvideo = mList.get(position).getmVideo();
                    String mheader = mList.get(position).getmHeader();
                    String mfooter = mList.get(position).getmFooter();

                    i.putExtra("title", mTitle);
                    i.putExtra("author", mAuthor);
                    i.putExtra("date", mDate);
                    i.putExtra("description", mDescription);
                    i.putExtra("url", mURL);
                    i.putExtra("video", mvideo);
                    i.putExtra("header", mheader);
                    i.putExtra("footer", mfooter);
                    startActivity(i);
                }
            }
        }));
    }

    private List<RecentNewsFilter> filter(List<RecentNewsFilter> models, String query) {
        query = query.toLowerCase();

        final List<RecentNewsFilter> filteredModelList = new ArrayList<>();
        for (RecentNewsFilter model : models) {
            final String text = model.getmTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onQueryTextChange(String query) {
        Log.e("TAG", "" + query);
        if(query==null || query.equals("")){
            isSearch=false;
        }
        else{
            isSearch=true;
        }
        mSearchQuery = query;
        adapter.isSearchEnable(query);
        final List<RecentNewsFilter> filteredModelList = filter(mListFilter, query);
        adapter.animateTo(filteredModelList);
        recyclerViewCategory.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
