package arl.gjurma.com.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
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

import arl.gjurma.com.Activity.DetailNewsActivity;
import arl.gjurma.com.Activity.MainActivity;
import arl.gjurma.com.Adapter.RecentNewsAdapter;
import arl.gjurma.com.Extra.RecyclerItemClickListener;
import arl.gjurma.com.Interfaces.FilterListner;
import arl.gjurma.com.Models.CategoryResponse;
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
 * Created by KRYTON on 25-09-2016.
 */
public class Recent_Fragment extends Fragment {

    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.progress_view)
    CircularProgressView progressView;
    private ApiInterface apiService;
    private RecentNewsAdapter adapter;
    private RecentPost mRecentPost;
    private List<RecentNewsFilter> mList = new ArrayList<>();
    private List<RecentNewsFilter> mListFilter = new ArrayList<>();
    private String TAG = "Recent_Fragment";
    private CategoryResponse mCategory;
    Map<String, String> params = new HashMap<String, String>();
    private String mSearchQuery;
    private boolean isSearch = false;
    public Recent_Fragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRecentNewsType();

        ((MainActivity) getActivity()).updateList(new FilterListner() {
            @Override
            public boolean onfilterlistner(String query, int page) {
                Log.e(TAG, "Query " + query + "   " + page);
                if(query==null || query.equals("")){
                    isSearch=false;
                }
                else{
                    isSearch=true;
                }
                mSearchQuery = query;
                if (page == 0) {
                    final List<RecentNewsFilter> filteredModelList = filter(mListFilter, query);
                    adapter.animateTo(filteredModelList);
                    list.scrollToPosition(0);
                    adapter.isSearchEnable(query);
                    return true;
                }
                return false;
            }
        });
    }

    private void getRecentNewsType() {
        progressView.startAnimation();
        apiService = GjurmaApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.getRecentNewsType();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "All " + response.body());
                if (response.code() == 200) {

                    Gson g = new Gson();
                    mCategory = g.fromJson(response.body(), CategoryResponse.class);
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonArray = (JsonArray) jsonParser.parse(response.body().get("categories").getAsJsonArray().get(0).getAsJsonObject().get("query") + "");

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject rec = jsonArray.get(i).getAsJsonObject();
                        if (rec.has("category")) {
                            String tmp = rec.get("category").toString();
                            tmp = tmp.replace("\"", "");
                            params.put("category_id", tmp);
//                            Log.e(TAG, i + "****" + rec + "****" + tmp + "****Category");
                        }
                        if (rec.has("include")) {
                            String tmp = rec.get("include").toString();
                            tmp = tmp.replace("\"", "");
                            params.put("include", tmp);
//                            Log.e(TAG, i + "****" + rec + "****" + tmp + "****include");
                        }
                        if (rec.has("count")) {
                            String tmp = rec.get("count").toString();
                            tmp = tmp.replace("\"", "");
                            params.put("count", tmp);
                            Log.e(TAG, i + "****" + rec + "****" + tmp + "****count");
                        }
//                        Log.e(TAG, "count" + rec);
                    }
//                    getRecentNews();
                    getVolley(response.body().get("categories").getAsJsonArray().get(0).getAsJsonObject().get("link").getAsString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onResponse: fail");
            }
        });
    }

    private void getVolley(String asString) {
        StringRequest stringRequest = new StringRequest(asString, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"**********************"+response);
                Gson g = new Gson();
                mRecentPost = g.fromJson(response, RecentPost.class);
                progressView.stopAnimation();
                progressView.setVisibility(View.GONE);
                diplayNews();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"**********************"+error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private List<RecentNewsFilter> filter(List<RecentNewsFilter> models, String query) {
        query = query.toLowerCase();
        Log.e(TAG, "filter " + query + " Size=" + models.size());
        final List<RecentNewsFilter> filteredModelList = new ArrayList<>();
        for (RecentNewsFilter model : models) {
            final String text = model.getmTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void getRecentNews() {
        apiService = GjurmaApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.getRecentNews(params);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "All " + response.body());
                if (response.code() == 200) {
                    Gson g = new Gson();
                    mRecentPost = g.fromJson(response.body(), RecentPost.class);
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);
                    diplayNews();
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

    private void diplayNews() {
        for (int i = 0; i < mRecentPost.getPosts().size(); i++) {
            RecentNewsFilter filter = new RecentNewsFilter();
            filter.setmTitle(mRecentPost.getPosts().get(i).getTitle());
            filter.setmAuthor("Gjurma.com");
            filter.setmDate(mRecentPost.getPosts().get(i).getDate());
            filter.setmDescription(mRecentPost.getPosts().get(i).getContent());
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

            String url = null;
            String imgRegex = "(?i)<img[^>]+?src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

            Pattern p = Pattern.compile(imgRegex);
            Matcher m = p.matcher(mRecentPost.getPosts().get(i).getContent());

            while (m.find()) {
                String imgSrc = m.group(1);
                url = imgSrc;
            }

            String imgRegex1 = "(?i)<video[^>]+?src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

            Pattern p1 = Pattern.compile(imgRegex1);
            Matcher m1 = p1.matcher(mRecentPost.getPosts().get(i).getContent());

            String videoUrl = null;
            while (m1.find()) {
                String imgSrc = m1.group(1);
                videoUrl = imgSrc;
                Log.e(TAG,"URL="+videoUrl +"*****"+m1.group(2)+"*****"+m1.group(0));
            }

            String Content = mRecentPost.getPosts().get(i).getContent();

            if (Content.contains("a href="))
            {
                int indexoff = mRecentPost.getPosts().get(i).getContent().indexOf("<source type=\\\"video/mp4\\\"");
                int indexofff = mRecentPost.getPosts().get(i).getContent().indexOf("\\\"/></video");
            }
            filter.setmImage(url);
            mList.add(filter);
            mListFilter.add(filter);
        }

        Log.e(TAG,"Size="+mList.size());
        /*for(int i =0;i<mList.size();i++)
            Log.e(TAG,""+mList.get(i).getmTitle());*/
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecentNewsAdapter(getActivity(), mList);
        list.setAdapter(adapter);

        list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, mList.size()+"*****" + position+"****"+(mList.size()+1));

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
                    Log.e(TAG,"Intent");
                    Intent i = new Intent(getActivity(), DetailNewsActivity.class);
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
                /*if(!isSearch){
                    Intent i = new Intent(getActivity(), DetailNewsActivity.class);
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
                }else {
                    if((mList.size())==position){
                        String url = "http://gjurma.com/?s="+mSearchQuery;
                        Log.e(TAG,"More click "+url);
                        Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent1);
                    }else {
                        Intent i = new Intent(getActivity(), DetailNewsActivity.class);
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
                }*/
            }
        }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recent_news, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
