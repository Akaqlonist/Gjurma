package arl.gjurma.com.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import arl.gjurma.com.Holders.CategoryHolder;
import arl.gjurma.com.Models.CategoryNewPOJO;
import arl.gjurma.com.R;

/**
 * Created by KRYTON on 27-09-2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
    private Context mContex;
//    private CategoryResponseOld mCategoryOld;
//    private CategoryResponse mCategory;
    private List<CategoryNewPOJO> mList;


    public CategoryAdapter(Context context,List<CategoryNewPOJO> mList) {
        this.mContex = context;
        this.mList = mList;
    }
    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category,parent,false);
        CategoryHolder holder = new CategoryHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        holder.tvTitle.setText(""+mList.get(position).getTitle());
        Picasso.with(mContex)
                .load(mList.get(position).getIcon())
                .into(holder.ivLogo);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void animateTo(List<CategoryNewPOJO> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<CategoryNewPOJO> newModels) {
        for (int i = mList.size() - 1; i >= 0; i--) {
            final CategoryNewPOJO model = mList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<CategoryNewPOJO> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final CategoryNewPOJO model = newModels.get(i);
            if (!mList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<CategoryNewPOJO> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final CategoryNewPOJO model = newModels.get(toPosition);
            final int fromPosition = mList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public CategoryNewPOJO removeItem(int position) {
        final CategoryNewPOJO model = mList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, CategoryNewPOJO model) {
        mList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final CategoryNewPOJO model = mList.remove(fromPosition);
        mList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
