package arl.gjurma.com.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import arl.gjurma.com.Activity.DetailNewsActivity;
import arl.gjurma.com.Adapter.FavouriteAdapter;
import arl.gjurma.com.Extra.RecyclerItemClickListener;
import arl.gjurma.com.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by KRYTON on 25-09-2016.
 */
public class Favourite_Fragment extends Fragment {
    @Bind(R.id.recyclerView_Favourite)
    RecyclerView recyclerViewFavourite;
    private FavouriteAdapter adapter;
    ArrayList<String> myTitle;
    ArrayList<String> myAuthor;
    ArrayList<String> myDate;
    ArrayList<String> myDescription;
    ArrayList<String> myUrl;
    ArrayList<String> myFooter;
    ArrayList<String> myHeader;
    ArrayList<String> myVideo;
    private String TAG = "Favourite_Fragment";

    public Favourite_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myTitle = new ArrayList<String>();
        myAuthor = new ArrayList<String>();
        myDate = new ArrayList<String>();
        myDescription = new ArrayList<String>();
        myUrl = new ArrayList<String>();
        myFooter = new ArrayList<String>();
        myHeader = new ArrayList<String>();
        myVideo = new ArrayList<String>();
        getIntentData();
    }

    private void getIntentData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            myTitle = bundle.getStringArrayList("title");
            myAuthor = bundle.getStringArrayList("author");
            myDate = bundle.getStringArrayList("date");
            myDescription = bundle.getStringArrayList("description");
            myUrl = bundle.getStringArrayList("url");
            myFooter = bundle.getStringArrayList("footer");
            myHeader = bundle.getStringArrayList("header");
            myVideo = bundle.getStringArrayList("video");
        }
        for(int i=0;i<myTitle.size();i++)
            Log.e(TAG,myHeader.get(i)+"*****"+myFooter.get(i)+"******"+myVideo.get(i));
        setListView();
    }

    private void setListView() {
        recyclerViewFavourite.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FavouriteAdapter(getActivity(),myTitle,myAuthor,myDate,myDescription,myUrl);
        recyclerViewFavourite.setAdapter(adapter);

        recyclerViewFavourite.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "" + position);
                Intent i = new Intent(getActivity(), DetailNewsActivity.class);
                i.putExtra("title", myTitle.get(position));
                i.putExtra("author", myAuthor.get(position));
                i.putExtra("date", myDate.get(position));
                i.putExtra("description", myDescription.get(position));
                i.putExtra("url", myUrl.get(position));
                i.putExtra("header", myHeader.get(position));
                i.putExtra("footer", myFooter.get(position));
                i.putExtra("video", myVideo.get(position));
                i.putExtra("type", 1);
                startActivity(i);
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        SharedPreferences prefs = getActivity().getSharedPreferences("FavPost", Context.MODE_PRIVATE);
        Set<String> jsonList = prefs.getStringSet("json", null);
        ArrayList<String> myJson;
        ArrayList<String> myTitle = new ArrayList<>();
        ArrayList<String> myAuthor = new ArrayList<>();
        ArrayList<String> myDate = new ArrayList<>();
        ArrayList<String> myDescription = new ArrayList<>();
        ArrayList<String> myUrl = new ArrayList<>();
        ArrayList<String> myHeader = new ArrayList<>();
        ArrayList<String> myFooter = new ArrayList<>();
        ArrayList<String> myVideo = new ArrayList<>();
        if(jsonList==null){
            myJson = new ArrayList<String>();
        }else {
            myJson = new ArrayList<String>(jsonList);
        }
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
                myHeader.add(obj.getString("header"));
                myFooter.add(obj.getString("footer"));
                myVideo.add(obj.getString("video"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter.refreshList(myTitle,myAuthor,myDate,myDescription,myUrl);
    }
}
