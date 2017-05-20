package arl.gjurma.com.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import arl.gjurma.com.Activity.ListByCategoryActivity;
import arl.gjurma.com.Activity.MainActivity;
import arl.gjurma.com.Adapter.CategoryAdapter;
import arl.gjurma.com.Extra.RecyclerItemClickListener;
import arl.gjurma.com.Interfaces.ChangeMenuListner;
import arl.gjurma.com.Interfaces.FilterChangeListnerCategory;
import arl.gjurma.com.Models.CategoryNewPOJO;
import arl.gjurma.com.Models.CategoryResponse;
import arl.gjurma.com.Models.CategoryResponseOld;
import arl.gjurma.com.R;
import arl.gjurma.com.Rest.ApiInterface;
import arl.gjurma.com.Rest.GjurmaApiClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by KRYTON on 25-09-2016.
 */
public class Category_Fragment extends Fragment {

    @Bind(R.id.recyclerView_category)
    RecyclerView recyclerViewCategory;
    @Bind(R.id.progress_view)
    CircularProgressView progressView;
    private String TAG = "Category_Fragment";
    private ApiInterface apiService;
    private CategoryAdapter adapter;
    private CategoryResponseOld mCategoryOld;
    private CategoryResponse mCategory;
    private List<CategoryNewPOJO> mList = new ArrayList<>();
    private List<CategoryNewPOJO> mListFilter = new ArrayList<>();
    private ChangeMenuListner mMenuChange;

    public Category_Fragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMenuChange = (ChangeMenuListner) getActivity();
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        getCategory();
        recyclerViewCategory.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "" + position);
                Intent i = new Intent(getActivity(), ListByCategoryActivity.class);
                i.putExtra("category", mListFilter.get(position));
                startActivity(i);
            }
        }));
        ((MainActivity) getActivity()).updateListCategory(new FilterChangeListnerCategory() {
            @Override
            public boolean onfilterlistner(String query, int page) {
                Log.e(TAG, "" + query + "   " + page);
                if (page == 1) {
                    final List<CategoryNewPOJO> filteredModelList = filter(mListFilter, query);
                    adapter.animateTo(filteredModelList);
                    recyclerViewCategory.scrollToPosition(0);
                    return true;
                }
                return false;
            }
        });
    }

    private void getCategory() {
        progressView.startAnimation();
        apiService = GjurmaApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.getCategory();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "All " + response.body());
                if (response.code() == 200) {
                    Gson g = new Gson();
                    mCategory = g.fromJson(response.body(), CategoryResponse.class);
                    Log.e(TAG, "Size=" + mCategory.getCategories().size());

                    for (int i = 0; i < mCategory.getCategories().size(); i++) {
                        CategoryNewPOJO filter = new CategoryNewPOJO();
                        filter.setId(mCategory.getCategories().get(i).getId());
                        filter.setTitle(mCategory.getCategories().get(i).getTitle());
                        filter.setIcon(mCategory.getCategories().get(i).getIcon());
                        filter.setLink(mCategory.getCategories().get(i).getLink());
                        filter.setQueryArray(response.body().get("categories").getAsJsonArray().get(i).getAsJsonObject().get("query") + "");
                        Log.e(TAG, response.body().get("categories").getAsJsonArray().get(i).getAsJsonObject().get("query") + "*****" + "LINK=" + mCategory.getCategories().get(i).getLink());
                        mList.add(filter);
                        mListFilter.add(filter);
                    }
                    progressView.setVisibility(View.GONE);
                    adapter = new CategoryAdapter(getActivity(), mList);
                    recyclerViewCategory.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onResponse: fail");
                progressView.setVisibility(View.GONE);
            }
        });
    }

    private List<CategoryNewPOJO> filter(List<CategoryNewPOJO> models, String query) {
        query = query.toLowerCase();
        Log.e(TAG, "filter " + query + " Size=" + models.size());
        final List<CategoryNewPOJO> filteredModelList = new ArrayList<>();
        for (CategoryNewPOJO model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    private void getCategory1() {
        apiService = GjurmaApiClient.getClient().create(ApiInterface.class);
        Call<CategoryResponseOld> call = apiService.getCategoryOld();
        call.enqueue(new Callback<CategoryResponseOld>() {
            @Override
            public void onResponse(Call<CategoryResponseOld> call, Response<CategoryResponseOld> response) {
                Log.d(TAG, "All " + response.body());
                if (response.code() == 200) {
                    mCategoryOld = response.body();
                    Log.e(TAG, "Size=" + mCategoryOld.getCategories().size());
//                    adapter = new CategoryAdapter(getActivity(),mCategoryOld);
                    recyclerViewCategory.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<CategoryResponseOld> call, Throwable t) {
                Log.d(TAG, "onResponse: fail");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
