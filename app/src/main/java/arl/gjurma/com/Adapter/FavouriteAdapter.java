package arl.gjurma.com.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import arl.gjurma.com.Holders.RecentNewsHolder;
import arl.gjurma.com.R;

/**
 * Created by KRYTON on 28-09-2016.
 */
public class FavouriteAdapter extends RecyclerView.Adapter<RecentNewsHolder> {

    private Context mContex;
    private List<String> myTitle = new ArrayList<>();
    private List<String> myAuthor = new ArrayList<>();
    private List<String> myDate = new ArrayList<>();
    private List<String> myDescription = new ArrayList<>();
    private List<String> myUrl = new ArrayList<>();
    public FavouriteAdapter(Context context, List<String> title,List<String> author, List<String> date, List<String> description, List<String> url) {
        this.mContex = context;
        this.myTitle = title;
        this.myAuthor = author;
        this.myDate = date;
        this.myDescription = description;
        this.myUrl = url;

    }

    @Override
    public RecentNewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recent_news,parent,false);
        RecentNewsHolder holder = new RecentNewsHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecentNewsHolder holder, int position) {
        Log.e("TAG","T="+myTitle.size()+" A="+myAuthor.size()+" D="+myDate.size()+" DS="+myDescription.size()+" U="+myUrl.size());
        if(myTitle.size()>0){
            if(myUrl.get(position)!=null){
                Picasso.with(mContex)
                        .load(myUrl.get(position))
                        .into(holder.ivImage);
            }else {
                Picasso.with(mContex)
                        .load(R.drawable.yourlogohere)
                        .into(holder.ivImage);
            }
        }

        if(myAuthor.get(position)!=null){
            holder.tvAuthor.setVisibility(View.VISIBLE);
            holder.tvAuthor.setText(""+myAuthor.get(position));
        }
        else
            holder.tvAuthor.setVisibility(View.GONE);

        if(myDate.get(position)!=null){
            holder.tvDate.setVisibility(View.VISIBLE);
            holder.tvDate.setText(myDate.get(position));
        }else
            holder.tvDate.setVisibility(View.GONE);

        holder.tvTitle.setText(Html.fromHtml(myTitle.get(position)));
//        Log.e("TAG",""+mRecentPost.getPosts().get(position).getAttachments().get(0).getUrl());
    }

    public void refreshList(List<String> title,List<String> author, List<String> date, List<String> description, List<String> url){
        this.myTitle = title;
        this.myAuthor = author;
        this.myDate = date;
        this.myDescription = description;
        this.myUrl = url;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return myTitle.size();
    }
}
