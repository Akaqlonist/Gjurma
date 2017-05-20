package arl.gjurma.com.Activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import arl.gjurma.com.Fragment.About_Fragment;
import arl.gjurma.com.Fragment.Dialog_Exit;
import arl.gjurma.com.Fragment.Favourite_Fragment;
import arl.gjurma.com.Fragment.Home_Fragment;
import arl.gjurma.com.Interfaces.ChangeMenuListner;
import arl.gjurma.com.Interfaces.ExitAppListner;
import arl.gjurma.com.Interfaces.FilterChangeListnerCategory;
import arl.gjurma.com.Interfaces.FilterListner;
import arl.gjurma.com.Models.RecentNewsFilter;
import arl.gjurma.com.R;
import arl.gjurma.com.Rest.ApiInterface;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ExitAppListner, SearchView.OnQueryTextListener,ChangeMenuListner {
    @Bind(arl.gjurma.com.R.id.toolbar)
    Toolbar toolbar;
    @Bind(arl.gjurma.com.R.id.frame_container)
    FrameLayout frameContainer;
    @Bind(arl.gjurma.com.R.id.navigation_view)
    NavigationView navigationView;
    @Bind(arl.gjurma.com.R.id.drawer)
    DrawerLayout drawer;
    private DrawerLayout drawerLayout;
    private String TAG = "MainActivity";
    private int mCurrentDisplay;
    private Menu menu;
    public FilterListner mFilterChange;
    public FilterChangeListnerCategory mFilterChangeCategory;
    private int mPage=0;
    private Retrofit retrofit = null;
    private ApiInterface apiService;
    private AdView mAdView;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(arl.gjurma.com.R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle("GJURMA.COM");
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "FCM TOKEN " + FirebaseInstanceId.getInstance().getToken());


        /****************** Admob banner added here***************/
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("CF428596370A102513B75B837816D1DB")
                .build();
        mAdView.loadAd(adRequest);

        /*******************************************************/
        /************* Admob Interstitial added here **********/
       /* mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });*/
        /*****************************************************/

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            checkPopUp();
           /* Uri uri = intent.getData();
            String valueOne = uri.getQueryParameter("keyOne");
            String valueTwo = uri.getQueryParameter("keyTwo");
            Log.e(TAG,valueOne+"**********"+valueTwo+""+uri.getPath());
            Intent i = new Intent(MainActivity.this,DetailNewsActivity.class);
            i.putExtra("type",5);
            i.putExtra("uri",uri.getPath());
            startActivity(i);*/
        }

        setSupportActionBar(toolbar);
        DisplayView(0);
        mCurrentDisplay = 0;
        initNavigationDrawer();
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

    private void checkPopUp() {
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
                        Intent i = new Intent(MainActivity.this,BlockPOPUpActivity.class);
                        i.putExtra("link",response.body().get("updateLink").getAsString());
                        i.putExtra("title",response.body().get("messageHead").getAsString());
                        i.putExtra("message",response.body().get("message").getAsString());
                        startActivity(i);
                        finish();
                    }else {
                        Intent intent = getIntent();
                        Uri uri = intent.getData();
                        String valueOne = uri.getQueryParameter("keyOne");
                        String valueTwo = uri.getQueryParameter("keyTwo");
                        Log.e(TAG,valueOne+"**********"+valueTwo+""+uri.getPath());
                        Intent i = new Intent(MainActivity.this,DetailNewsActivity.class);
                        i.putExtra("type",5);
                        i.putExtra("uri", uri.getPath());
                        startActivity(i);
                    }
                }else{
                    Intent intent = getIntent();
                    Uri uri = intent.getData();
                    String valueOne = uri.getQueryParameter("keyOne");
                    String valueTwo = uri.getQueryParameter("keyTwo");
                    Log.e(TAG,valueOne+"**********"+valueTwo+""+uri.getPath());
                    Intent i = new Intent(MainActivity.this,DetailNewsActivity.class);
                    i.putExtra("type",5);
                    i.putExtra("uri", uri.getPath());
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onResponse: fail");
                Intent intent = getIntent();
                Uri uri = intent.getData();
                String valueOne = uri.getQueryParameter("keyOne");
                String valueTwo = uri.getQueryParameter("keyTwo");
                Log.e(TAG, valueOne + "**********" + valueTwo + "" + uri.getPath());
                Intent i = new Intent(MainActivity.this,DetailNewsActivity.class);
                i.putExtra("type",5);
                i.putExtra("uri", uri.getPath());
                startActivity(i);
            }
        });
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(arl.gjurma.com.R.id.navigation_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    int id = menuItem.getItemId();

                    switch (id) {
                        case arl.gjurma.com.R.id.menuHome:
                            DisplayView(0);
                            mCurrentDisplay = 0;
                            drawerLayout.closeDrawers();
                            break;
                        case arl.gjurma.com.R.id.menuFavourite:
                            DisplayView(1);
                            mCurrentDisplay = 1;
                            drawerLayout.closeDrawers();
                            break;
                        case arl.gjurma.com.R.id.menuRate:
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gjurma.com/raporto.php"));
                            startActivity(browserIntent);
                            drawerLayout.closeDrawers();
                            break;
                        case arl.gjurma.com.R.id.menuAbout:
                            Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gjurma.com"));
                            startActivity(browserIntent1);
//                            DisplayView(2);
//                            mCurrentDisplay = 2;
                            drawerLayout.closeDrawers();
                            break;
                        case arl.gjurma.com.R.id.menuExit:
                            drawerLayout.closeDrawers();
                            FragmentManager fm = getFragmentManager();
                            Dialog_Exit cd = new Dialog_Exit();
                            cd.show(fm, "");
//                            finish();
                            break;
                    }
                    return true;
                }
            });
        }
        /*View header = navigationView.getHeaderView(0);
        final RelativeLayout ryMenu = (RelativeLayout) header.findViewById(R.id.rl_menu);
        ImageView ivAvatar = (ImageView) header.findViewById(R.id.iv_user_avatar);
        TextView tvUserName = (TextView) header.findViewById(R.id.tv_user_name);
        TextView tvLocation = (TextView) header.findViewById(R.id.tv_age);
        TextView tvAge = (TextView) header.findViewById(R.id.tv_location);*/
        drawerLayout = (DrawerLayout)findViewById(arl.gjurma.com.R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, arl.gjurma.com.R.string.drawer_open, arl.gjurma.com.R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout = (DrawerLayout) findViewById(arl.gjurma.com.R.id.drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void DisplayView(int position){
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Home_Fragment();
                break;
            case 1:
                fragment = new Favourite_Fragment();
                break;
            case 2:
                fragment = new About_Fragment();
                break;
            default:
                break;
        }
        SharedPreferences prefs = getSharedPreferences("FavPost", Context.MODE_PRIVATE);
        Set<String> jsonList = prefs.getStringSet("json", null);
//        Set<String> title = prefs.getStringSet("title", null);
//        Set<String> author = prefs.getStringSet("author", null);
//        Set<String> date = prefs.getStringSet("date", null);
//        Set<String> description = prefs.getStringSet("description", null);
//        Set<String> url = prefs.getStringSet("url", null);
        ArrayList<String> myJson;
        ArrayList<String> myTitle = new ArrayList<>();
        ArrayList<String> myAuthor = new ArrayList<>();
        ArrayList<String> myDate = new ArrayList<>();
        ArrayList<String> myDescription = new ArrayList<>();
        ArrayList<String> myUrl = new ArrayList<>();
        ArrayList<String> myFooter = new ArrayList<>();
        ArrayList<String> myHeader = new ArrayList<>();
        ArrayList<String> myVideo = new ArrayList<>();
        if(jsonList==null){
            myJson = new ArrayList<String>();
//            myTitle = new ArrayList<String>();
//            myAuthor = new ArrayList<String>();
//            myDate = new ArrayList<String>();
//            myDescription = new ArrayList<String>();
//            myUrl = new ArrayList<String>();
        }else {
            myJson = new ArrayList<String>(jsonList);
//            myTitle = new ArrayList<String>(title);
//            myAuthor = new ArrayList<String>(author);
//            myDate = new ArrayList<String>(date);
//            myDescription = new ArrayList<String>(description);
//            myUrl = new ArrayList<String>(url);
//            Log.e("TAG","T="+myTitle.size()+" A="+myAuthor.size()+" D="+myDate.size()+" DS="+myDescription.size()+" U="+myUrl.size());
        }

        Log.e(TAG,"SIZE="+myJson.size());
        for(int i=0;i<myJson.size();i++){
            JSONObject obj = null;
            try {
                obj = new JSONObject(myJson.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                myTitle.add(obj.getString("title"));
                myAuthor.add(obj.getString("author"));
                myDate.add(obj.getString("date"));
                myDescription.add(obj.getString("description"));
                myUrl.add(obj.getString("url"));
                myFooter.add(obj.getString("footer"));
                myHeader.add(obj.getString("header"));
                myVideo.add(obj.getString("video"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (fragment != null) {
            Bundle bundle = new Bundle();
            /*bundle.putParcelable("userdata", mUserData);*/
            /**Array list for favourite fragment only**/
            bundle.putStringArrayList("title",myTitle);
            bundle.putStringArrayList("author",myAuthor);
            bundle.putStringArrayList("date",myDate);
            bundle.putStringArrayList("description",myDescription);
            bundle.putStringArrayList("url",myUrl);
            bundle.putStringArrayList("header",myHeader);
            bundle.putStringArrayList("footer",myFooter);
            bundle.putStringArrayList("video",myVideo);
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(arl.gjurma.com.R.id.frame_container, fragment).commit();

        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating fragment");
        }
    }

    public void updateList(FilterListner listener) {
        mFilterChange = listener;
    }

    public void updateListCategory(FilterChangeListnerCategory listener) {
        mFilterChangeCategory = listener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(arl.gjurma.com.R.menu.main, menu);
        final MenuItem item = menu.findItem(arl.gjurma.com.R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if(mPage==0){
            return mFilterChange.onfilterlistner(query,0);
        }else {
            return mFilterChangeCategory.onfilterlistner(query,1);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<RecentNewsFilter> filter(ArrayList<RecentNewsFilter> models, String query) {
        query = query.toLowerCase();

        final ArrayList<RecentNewsFilter> filteredModelList = new ArrayList<>();
        for (RecentNewsFilter model : models) {
            final String text = model.getmTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void closeApp(int id) {
        if(id==1){
            finish();
        }
    }

    @Override
    public void onMenuChange(int index) {
        MenuInflater inflater = getMenuInflater();
        if (menu!=null){
            switch (index){
                case 0:
                    mPage=0;
                    menu.clear();
                    inflater.inflate(arl.gjurma.com.R.menu.main, menu);
                    final MenuItem item = menu.findItem(arl.gjurma.com.R.id.action_search);
                    final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                    searchView.setOnQueryTextListener(this);
                    break;
                case 1:
                    mPage=1;
                    menu.clear();
                    /*inflater.inflate(R.menu.main, menu);
                    final MenuItem item1 = menu.findItem(R.id.action_search);
                    final SearchView searchView1 = (SearchView) MenuItemCompat.getActionView(item1);
                    searchView1.setOnQueryTextListener(this);*/
            }
        }
    }
}
