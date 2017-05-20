package arl.gjurma.com.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KRYTON on 27-09-2016.
 */
public class CategoryPOJO implements Parcelable{
    private String id;
    private String slug;
    private String title;
    private String description;
    private String post_count;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.slug);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.post_count);
    }

    public CategoryPOJO() {
    }

    protected CategoryPOJO(Parcel in) {
        this.id = in.readString();
        this.slug = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.post_count = in.readString();
    }

    public static final Creator<CategoryPOJO> CREATOR = new Creator<CategoryPOJO>() {
        @Override
        public CategoryPOJO createFromParcel(Parcel source) {
            return new CategoryPOJO(source);
        }

        @Override
        public CategoryPOJO[] newArray(int size) {
            return new CategoryPOJO[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPost_count() {
        return post_count;
    }

    public void setPost_count(String post_count) {
        this.post_count = post_count;
    }
}
