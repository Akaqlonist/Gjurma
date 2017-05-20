/*
package com.nonnegotiables.nonnegotiable.Activity;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nonnegotiables.nonnegotiable.Extra.FavListSearchTextChangeListener;
import com.nonnegotiables.nonnegotiable.Extra.FragmentChangeListener;
import com.nonnegotiables.nonnegotiable.Extra.LatLongListener;
import com.nonnegotiables.nonnegotiable.Extra.SetUserOnline;
import com.nonnegotiables.nonnegotiable.Extra.SetUserOnlineAll;
import com.nonnegotiables.nonnegotiable.Fragment.AboutApplication_Fragment;
import com.nonnegotiables.nonnegotiable.Fragment.Chat_Fragment;
import com.nonnegotiables.nonnegotiable.Fragment.ContactUs_Fragment;
import com.nonnegotiables.nonnegotiable.Fragment.Favourite_Fragment;
import com.nonnegotiables.nonnegotiable.Fragment.GPS_Dialog;
import com.nonnegotiables.nonnegotiable.Fragment.Help_Fragment;
import com.nonnegotiables.nonnegotiable.Fragment.Logout_dailog;
import com.nonnegotiables.nonnegotiable.Fragment.PrivatePolicy_Fragment;
import com.nonnegotiables.nonnegotiable.Fragment.Profile_Fragment;
import com.nonnegotiables.nonnegotiable.Fragment.SearchNearBy_Fragment;
import com.nonnegotiables.nonnegotiable.Fragment.Setting_Fragment;
import com.nonnegotiables.nonnegotiable.Fragment.TermOfService_Fragment;
import com.nonnegotiables.nonnegotiable.Helper.SharedPrefHelper;
import com.nonnegotiables.nonnegotiable.Interfaces.LastChatMessageListner;
import com.nonnegotiables.nonnegotiable.Interfaces.SetNotificationCountRecent;
import com.nonnegotiables.nonnegotiable.Model.NNUser;
import com.nonnegotiables.nonnegotiable.Model.OnlineStatus;
import com.nonnegotiables.nonnegotiable.NonNegotiable;
import com.nonnegotiables.nonnegotiable.R;
import com.nonnegotiables.nonnegotiable.Rest.ApiInterface;

import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

*/
/**
 * Created by osp68 on 2/7/16.
 *//*


public class MainPage_Activity extends AppCompatActivity implements GPS_Dialog.OnQuantitySelectedListener,FragmentChangeListener,LatLongListener,SearchView.OnQueryTextListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private String TAG = "MainPage_Activity";
    private NNUser mUserData;
    private int mCurretView;
    private Menu menu;
    private double mLatitude;
    private double mLongitude;
    //    SocketConnection sc;
    private Socket mSocket;
    private SetUserOnline mChangedOnlineStatus;
    private SetUserOnlineAll mChangedOnlineStatusAll;
    private SetNotificationCountRecent mNotificationCountRecent;
    private LastChatMessageListner mLastMessage;
    private FavListSearchTextChangeListener favListSearchTextChangeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        NonNegotiable.getInstance().setUserOnline();

        registerFCMToken();
        getIntentData();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initNavigationDrawer();
        DisplayView(0);
        mCurretView=0;

    }










    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    int id = menuItem.getItemId();

                    switch (id){
                        case R.id.menuChat:
                            DisplayView(1);
                            mCurretView=1;
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.menuSearch:
                            DisplayView(2);
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.menuFavourite:
                            DisplayView(3);
                            mCurretView=3;
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.menuSetting:
                            DisplayView(4);
                            mCurretView=4;
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.menuLogout:
                            drawerLayout.closeDrawers();
                            FragmentManager fm = getFragmentManager();
                            Logout_dailog cd = new Logout_dailog();
                            cd.show(fm, "");
                            break;
                    }
                    return true;
                }
            });
        }
        View header = navigationView.getHeaderView(0);
        final RelativeLayout ryMenu = (RelativeLayout) header.findViewById(R.id.rl_menu);
        ImageView ivAvatar = (ImageView) header.findViewById(R.id.iv_user_avatar);
        TextView tvUserName = (TextView) header.findViewById(R.id.tv_user_name);
        TextView tvLocation = (TextView) header.findViewById(R.id.tv_age);
        TextView tvAge = (TextView) header.findViewById(R.id.tv_location);

        tvUserName.setText(mUserData.mFistName+" "+mUserData.mLastName);
        tvLocation.setText(mUserData.mCity+"");
        tvAge.setText(getAge(mUserData.mBirthdate)+" yrs");

        String mDrawableName = "avatar" + mUserData.mAvatar;
        int resID = getResources().getIdentifier(mDrawableName, "drawable", this.getPackageName());
        ivAvatar.setImageResource(resID);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayView(0);
                mCurretView=0;
                drawerLayout.closeDrawers();
            }
        });

        ryMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainPage_Activity.this, ryMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_nav_setting, popup.getMenu());
                popup.setGravity(Gravity.END);

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        switch (id){
                            case R.id.menuSettingAboutApp:
                                DisplayView(5);
                                mCurretView=5;
                                drawerLayout.closeDrawers();
                                break;
                            case R.id.menuSettingContactus:
                                DisplayView(6);
                                mCurretView=6;
                                drawerLayout.closeDrawers();
                                break;
                            case R.id.menuSettingHelp:
                                DisplayView(7);
                                mCurretView=7;
                                drawerLayout.closeDrawers();
                                break;
                            case R.id.menuSettingTerm:
                                DisplayView(8);
                                mCurretView=8;
                                drawerLayout.closeDrawers();
                                break;
                            case R.id.menuSettingPolicy:
                                DisplayView(9);
                                mCurretView=9;
                                drawerLayout.closeDrawers();
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        View v = navigationView.getMenu().getItem(0).getActionView();
        TextView tvNotification= (TextView) v.findViewById(R.id.tv_notification);
        tvNotification.setVisibility(View.GONE);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

}
*/
