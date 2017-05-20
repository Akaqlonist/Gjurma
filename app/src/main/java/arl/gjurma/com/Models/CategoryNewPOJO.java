package arl.gjurma.com.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by KRYTON on 29-09-2016.
 */
public class CategoryNewPOJO implements Parcelable{
    private String id;
    private String title;
    private String icon;
    private String link;
    private String QueryArray;
    private List<QueryPOJO> query;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.icon);
        dest.writeString(this.link);
        dest.writeString(this.QueryArray);
        dest.writeTypedList(this.query);
    }

    public CategoryNewPOJO() {
    }

    protected CategoryNewPOJO(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.icon = in.readString();
        this.link = in.readString();
        this.QueryArray = in.readString();
        this.query = in.createTypedArrayList(QueryPOJO.CREATOR);
    }

    public static final Creator<CategoryNewPOJO> CREATOR = new Creator<CategoryNewPOJO>() {
        @Override
        public CategoryNewPOJO createFromParcel(Parcel source) {
            return new CategoryNewPOJO(source);
        }

        @Override
        public CategoryNewPOJO[] newArray(int size) {
            return new CategoryNewPOJO[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getQueryArray() {
        return QueryArray;
    }

    public void setQueryArray(String queryArray) {
        QueryArray = queryArray;
    }

    public List<QueryPOJO> getQuery() {
        return query;
    }

    public void setQuery(List<QueryPOJO> query) {
        this.query = query;
    }
}
