package arl.gjurma.com.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import arl.gjurma.com.Holders.RecentNewsHolder;
import arl.gjurma.com.Holders.RecentNewsMoreHolder;
import arl.gjurma.com.Models.RecentNewsFilter;
import arl.gjurma.com.R;

/**
 * Created by KRYTON on 25-09-2016.
 */
public class RecentNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContex;
    private List<RecentNewsFilter> mList;
    private String TAG = "RecentNewsAdapter";
    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_HEADER = 1;
    private boolean isSearch = false;

    public RecentNewsAdapter(Context context,List<RecentNewsFilter> recentPost) {
        this.mContex = context;
        this.mList = recentPost;
        /*for(int i =0;i<mList.size();i++)
            Log.e(TAG,""+mList.get(i).getmTitle());*/
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_NORMAL) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recent_news, parent, false);
            return new RecentNewsHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_more_search,parent,false);
            return new RecentNewsMoreHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);
        if (itemType == ITEM_TYPE_NORMAL) {
            if(position<mList.size()){
                Picasso.with(mContex)
                        .load(mList.get(position).getmImage())
                        .into(((RecentNewsHolder)holder).ivImage);

                ((RecentNewsHolder)holder).tvAuthor.setVisibility(View.VISIBLE);
                ((RecentNewsHolder)holder).tvAuthor.setText("Gjurma.com");

                /*if(mList.get(position).getmAuthor()!=null){
                    ((RecentNewsHolder)holder).tvAuthor.setVisibility(View.VISIBLE);
                    ((RecentNewsHolder)holder).tvAuthor.setText(""+mList.get(position).getmAuthor());
                }
                else
                    ((RecentNewsHolder)holder).tvAuthor.setVisibility(View.GONE);*/

                if(mList.get(position).getmDate()!=null){
                    ((RecentNewsHolder)holder).tvDate.setVisibility(View.VISIBLE);
                    ((RecentNewsHolder)holder).tvDate.setText(mList.get(position).getmDate());
                }else
                    ((RecentNewsHolder)holder).tvDate.setVisibility(View.GONE);

                ((RecentNewsHolder)holder).tvTitle.setText(Html.fromHtml(mList.get(position).getmTitle()));
            }

        } else  {
            Log.e(TAG,"Search More"+isSearch);
            if(isSearch)
                ((RecentNewsMoreHolder)holder).tvTitle.setText("Më Shumë REZULTATE...");
            else
                ((RecentNewsMoreHolder)holder).tvTitle.setText("MË SHUMË LAJME...");
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.e(TAG,"getItemViewType "+isSearch+"****"+position+"****"+mList.size());
        if(position==(mList.size()))
            return ITEM_TYPE_HEADER;
        else
            return ITEM_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        int mSize = (mList.size()+1);
//        Log.e(TAG,"isSearch = "+isSearch+"****"+mList.size()+"*****"+(mList.size()+1)+"*****"+mSize);
        return mSize;
    }


    public void isSearchEnable(String query){
        if(query==null || query.equals("")){
            isSearch=false;
            Log.e(TAG, "query = " + query);
        }else{
            isSearch=true;
            Log.e(TAG,"query = "+query);
        }
        notifyDataSetChanged();
    }

    public void animateTo(List<RecentNewsFilter> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<RecentNewsFilter> newModels) {
        for (int i = mList.size() - 1; i >= 0; i--) {
            final RecentNewsFilter model = mList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<RecentNewsFilter> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final RecentNewsFilter model = newModels.get(i);
            if (!mList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<RecentNewsFilter> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final RecentNewsFilter model = newModels.get(toPosition);
            final int fromPosition = mList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public RecentNewsFilter removeItem(int position) {
        final RecentNewsFilter model = mList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, RecentNewsFilter model) {
        mList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final RecentNewsFilter model = mList.remove(fromPosition);
        mList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
