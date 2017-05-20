package arl.gjurma.com.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KRYTON on 27-09-2016.
 */
public class CategoryResponseOld implements Parcelable{
    private List<CategoryPOJO> categories = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.categories);
    }

    public CategoryResponseOld() {
    }

    protected CategoryResponseOld(Parcel in) {
        this.categories = in.createTypedArrayList(CategoryPOJO.CREATOR);
    }

    public static final Creator<CategoryResponseOld> CREATOR = new Creator<CategoryResponseOld>() {
        @Override
        public CategoryResponseOld createFromParcel(Parcel source) {
            return new CategoryResponseOld(source);
        }

        @Override
        public CategoryResponseOld[] newArray(int size) {
            return new CategoryResponseOld[size];
        }
    };

    public List<CategoryPOJO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryPOJO> categories) {
        this.categories = categories;
    }
}
