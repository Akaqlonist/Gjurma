package arl.gjurma.com.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import arl.gjurma.com.Fragment.Category_Fragment;
import arl.gjurma.com.Fragment.Recent_Fragment;


/**
 * Created by KRYTON on 25-09-2016.
 */
public class HomeAdapter extends FragmentStatePagerAdapter  {
    int tabCount;
    public HomeAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Recent_Fragment tab1 = new Recent_Fragment();
                Bundle bundle = new Bundle();
                /*bundle.putStringArrayList("touser", (ArrayList<String>) mTOUser);
                bundle.putStringArrayList("msg", (ArrayList<String>) mMsg);
                bundle.putStringArrayList("name", (ArrayList<String>) mName);
                bundle.putIntegerArrayList("avatar", (ArrayList<Integer>) mAvatar);
                bundle.putIntegerArrayList("notification", (ArrayList<Integer>) mNotification);
                bundle.putParcelable("userdata", mUserData);*/
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                Category_Fragment tab2 = new Category_Fragment();
                Bundle bundle1 = new Bundle();
                /*bundle1.putParcelable("alluser", favUser);
                bundle1.putParcelable("userdata", mUserData);*/
                tab2.setArguments(bundle1);
                return tab2;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return tabCount;
    }
}
