package arl.gjurma.com.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import arl.gjurma.com.R;


/**
 * Created by KRYTON on 27-09-2016.
 */
public class CategoryHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle;
    public ImageView ivLogo;

    public CategoryHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        ivLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
    }
}
