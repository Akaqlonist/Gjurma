package arl.gjurma.com.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import arl.gjurma.com.R;

/**
 * Created by KRYTON on 11-10-2016.
 */
public class RecentNewsMoreHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle;

    public RecentNewsMoreHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_search_more);
    }
}
