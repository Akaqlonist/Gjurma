package arl.gjurma.com.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KRYTON on 29-09-2016.
 */
public class CategoryResponse implements Parcelable{
    private List<CategoryNewPOJO> categories = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.categories);
    }

    public CategoryResponse() {
    }

    protected CategoryResponse(Parcel in) {
        this.categories = in.createTypedArrayList(CategoryNewPOJO.CREATOR);
    }

    public static final Creator<CategoryResponse> CREATOR = new Creator<CategoryResponse>() {
        @Override
        public CategoryResponse createFromParcel(Parcel source) {
            return new CategoryResponse(source);
        }

        @Override
        public CategoryResponse[] newArray(int size) {
            return new CategoryResponse[size];
        }
    };

    public List<CategoryNewPOJO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryNewPOJO> categories) {
        this.categories = categories;
    }
}
