package arl.gjurma.com.Fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import arl.gjurma.com.Adapter.HomeAdapter;
import arl.gjurma.com.Interfaces.ChangeMenuListner;
import arl.gjurma.com.R;

/**
 * Created by KRYTON on 25-09-2016.
 */
public class Home_Fragment extends Fragment implements TabLayout.OnTabSelectedListener{
    private TabLayout tabLayout;

    private String TAG = "Chat_Fragment";
    private ViewPager viewPager;
    private ChangeMenuListner mMenuChange;
    public Home_Fragment(){

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMenuChange = (ChangeMenuListner) getActivity();
        setTabs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        return rootView;
    }


    private void setTabs(){

        tabLayout.addTab(tabLayout.newTab().setText("Të Fundit"));
        tabLayout.addTab(tabLayout.newTab().setText("Kategoritë"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        tabLayout.setTabTextColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimary));



        HomeAdapter adapter = new HomeAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition(position, 0, true);
                tabLayout.setSelected(true);
                Log.e(TAG,""+position);
                if (position==1)
                    mMenuChange.onMenuChange(1);
                else
                    mMenuChange.onMenuChange(0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setOnTabSelectedListener(this);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        Log.d(TAG,""+tab.getPosition());
        if(tab.getPosition()==1)
            mMenuChange.onMenuChange(1);
        else
            mMenuChange.onMenuChange(0);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
