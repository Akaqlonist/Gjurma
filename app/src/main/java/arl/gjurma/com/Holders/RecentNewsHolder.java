package arl.gjurma.com.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import arl.gjurma.com.R;


/**
 * Created by KRYTON on 25-09-2016.
 */
public class RecentNewsHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle;
    public TextView tvAuthor;
    public TextView tvDate;
    public ImageView ivImage;

    public RecentNewsHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
    }
}
